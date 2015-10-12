package com.gci.aptsserver.station;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gci.aptsserver.station.bean.BusBean;
import com.gci.aptsserver.station.bean.StationInfo;
import com.gci.aptsserver.station.bean.StationPara;
import com.gci.aptsserver.util.DateUtil;

public class StationForecast {

	private static int maxNeedTimeRate = 2;// 到站所需最大时间
	private static float mileageRate = 0.3f;// 里程占比例

	private static String DDL_ADFLAG_IN = "1";
	private static String DDL_ADFLAG_OUT = "0";

	// 是否更新站台数据
	private static boolean checkUpdate(StationInfo info, Double needTime) {
		if (needTime < 0)
			return false; // 计算结果无效
		if (info.needTime < 0)
			return true; // 本站无报站信息，
		if (info.needTime > needTime)
			return true;// 本站有报站信息，使用最小的报站信息
		return false;
	}

	// 更新报站数据
	private static StationInfo updateInfo(StationInfo info, Double needTime, Date baseTime, Double distance) {
		// if (needTime < minNeedTime) needTime = minNeedTime;
		if (checkUpdate(info, needTime)) {
			// if (needTime < minNeedTime) needTime = minNeedTime;//最小报站时间
			info.needTime = needTime.doubleValue();
			info.estimateAdTime = DateUtil.dateComputer(baseTime, Calendar.SECOND, needTime.intValue());
			info.distance = distance;
		}
		return info;
	}

	// 根据里程计算
	private static StationInfo computerByMileage(BusBean bus, StationInfo info, StationPara para) {

		Long mileage = bus.gpsMileage - bus.adMileage;// 车辆行驶距离
		if (mileage > 0 && para.distance > mileage) { // 里程有效

			Double distance = Double.valueOf(para.distance.longValue() - mileage);// 车辆到下一站距离
			Double needTime1 = para.preStationTimeD / para.distance * distance;// 按里程比例计算需要的时间
			Double needTime2 = (new Date().getTime() - bus.adTime.getTime()) / 1000 / mileage * distance;// 按照速度计算需要的时间

			Double needTime = needTime1 > needTime2 ? needTime1 : needTime2;
			if (needTime1 > 0 && needTime2 > 0)
				needTime = needTime1 * mileageRate + needTime2 * (1 - mileageRate);// 按照30%的里程和70%的速度计算剩余时间

			if (needTime > needTime1 * maxNeedTimeRate)
				needTime = needTime1 * maxNeedTimeRate;// 如果时间超过参数时间过多，则截取

			return updateInfo(info, needTime, new Date(), distance);
		}
		// 里程计算失败，根据出站时间计算
		Double needTime = para.preStationTimeD - (new Date().getTime() - bus.adTime.getTime()) / 1000;
		Double distance = para.distance / para.preStationTimeD * needTime;

		return updateInfo(info, needTime, bus.adTime, distance);
	}

	// 根据进站时间计算
	private static StationInfo computerByAdTime(BusBean bus, StationInfo info, StationPara para) {

		Double needTime = para.preStationTimeA - (new Date().getTime() - bus.adTime.getTime()) / 1000;

		if (needTime < para.preStationTimeD)
			needTime = para.preStationTimeD;
		Double distance = para.distance;
		return updateInfo(info, needTime, bus.adTime, distance);
	}

	// 根据上一站报站信息，计算本站站台信息
	private static StationInfo computerInfoByPreStationInfo(StationInfo info, StationInfo preInfo, StationPara para) {
		if (preInfo.needTime < 0) {// 上一站无进站信息，本站不修改

		} else {// 累加上一站信息
			Double needTime = preInfo.needTime + para.preStationTimeA;
			Double distance = preInfo.distance + para.distance;
			if (checkUpdate(info, needTime)) {
				info.needTime = needTime;
				info.estimateAdTime = DateUtil.dateComputer(preInfo.estimateAdTime, Calendar.SECOND, para.preStationTimeA.intValue());
				info.distance = distance;
			}
		}
		return info;
	}

	/**
	 * 通过修改每辆车影响的站台信息，来实现
	 * 
	 * @param busList
	 *            车辆列表
	 * @param infoMap
	 *            报站信息
	 * @param paraMap
	 *            参数信息
	 * @return
	 */
	private static void computerByBus(List<BusBean> busList, Map<Long, StationInfo> infoMap, Map<Long, List<StationPara>> paraMap) {

		// 遍历车辆，更新站台报站信息
		for (BusBean bus : busList) {
			if (paraMap.containsKey(bus.routeSubId)) {// 存在车辆当前任务的参数信息
				List<StationPara> paraList = paraMap.get(bus.routeSubId);// 该子线路的参数信息
				for (int i = 1; i < paraList.size(); i++) {// 遍历该子线路的每个站台，更新信息
					StationPara para = paraList.get(i);
					if (para.orderNumber == 1) {// 首站不计算
						continue;
					}
					if (bus.orderNumber > para.orderNumber) {// 车辆所在站台的前面站台,不做处理
						continue;
					}

					if (bus.orderNumber == para.orderNumber && bus.routeStationId == para.routeStationId) { // 更新车辆当前站台信息
						StationInfo info = infoMap.get(bus.routeStationId);
						if (bus.adFlag.equals(DDL_ADFLAG_IN)) {// 车辆进站
							info.needTime = 0D;
							info.distance = 0D;
							info.estimateAdTime = bus.adTime;
						} else {// 出站不做处理
						}
					}

					if (bus.orderNumber < para.orderNumber) { // 更新车辆所在站台后面的站台信息
						StationPara prePara = paraList.get(i - 1);
						StationInfo preInfo = infoMap.get(prePara.routeStationId);
						StationInfo info = infoMap.get(para.routeStationId);

						if (bus.routeStationId == preInfo.routeStationId) {// 车辆在上一站出站,按照里程计算,进站,根据时间计算
							StationInfo computerInfo = bus.adFlag.equals(DDL_ADFLAG_OUT) ? computerByMileage(bus, info, para) : computerByAdTime(bus, info, para);
							infoMap.put(computerInfo.routeStationId, computerInfo);
						} else {// 根据上一站计算本站报站信息
							StationInfo computerInfo = computerInfoByPreStationInfo(info, preInfo, para);
							infoMap.put(computerInfo.routeStationId, computerInfo);
						}
					}
				}
			}
		}

	}

	// 初始化参数数据
	private static void initPara(Map<Long, StationInfo> infoMap, Map<Long, List<StationPara>> paraMap, List<StationPara> paraList) {
		// 初始化站台数据
		for (StationInfo info : infoMap.values()) {
			info.needTime = -1D;
			info.distance = -1D;
		}
		for (StationPara para : paraList) {// 按照子线路分组
			if (!paraMap.containsKey(para.routesubId)) {
				paraMap.put(para.routesubId, new ArrayList<StationPara>());
			}
			paraMap.get(para.routesubId).add(para);
		}

		for (List<StationPara> list : paraMap.values()) {
			Collections.sort(list);
		}
	}

	/**
	 * 计算报站信息，按照线路运算
	 * 
	 * @param busList
	 *            线路上的全部车
	 * @param infoMap
	 *            线路上的站点报站信息
	 * @param paraList
	 *            线路上站点的参数信息
	 * @return
	 */
	public static void computer(List<BusBean> busList, Map<Long, StationInfo> infoMap, List<StationPara> paraList) {
		Map<Long, List<StationPara>> paraMap = new HashMap<Long, List<StationPara>>();
		initPara(infoMap, paraMap, paraList);// 初始化参数数据
		computerByBus(busList, infoMap, paraMap);
	}
}
