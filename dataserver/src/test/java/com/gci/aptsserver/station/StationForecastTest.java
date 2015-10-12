package com.gci.aptsserver.station;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.gci.aptsserver.station.bean.BusBean;
import com.gci.aptsserver.station.bean.StationInfo;
import com.gci.aptsserver.station.bean.StationPara;
import com.gci.aptsserver.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class StationForecastTest {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private JedisPool jedisPool;

	private String routeId = "98";

	// 车辆
	private List<BusBean> busList() {
		List<BusBean> busList = new ArrayList<BusBean>();
		Jedis jedis = jedisPool.getResource();
		Map<String, String> map = jedis.hgetAll("dpdb.rt.route_id=" + routeId);
		ObjectMapper mapper = new ObjectMapper();

		for (String rb : map.values()) {
			try {
				@SuppressWarnings("rawtypes")
				Map redisBus = mapper.readValue(rb, Map.class);

				BusBean busBean = new BusBean();
				busBean.busId = Long.parseLong(redisBus.get("bus_id").toString());
				busBean.adFlag = redisBus.get("ad_flag").toString();
				busBean.adTime = DateUtil.parseDate(redisBus.get("ad_time").toString(), "yyyyMMdd HHmmss");
				busBean.stationId = Long.valueOf(redisBus.get("station_id").toString());
				busBean.routeStationId = Long.valueOf(redisBus.get("route_sta_id").toString());
				busBean.routeSubId = Long.valueOf(redisBus.get("routesub_id").toString());
				busBean.orderNumber = Long.valueOf(redisBus.get("order_number").toString());
				busBean.gpsMileage = Long.valueOf(redisBus.get("gps_mileage").toString());
				busBean.adMileage = Long.valueOf(redisBus.get("ad_mileage").toString());
				busList.add(busBean);
				break;
			} catch (Exception e) {
				// 没用的车
			}

		}
		System.out.println("bus == " + busList.size());
		for (BusBean bus : busList) {
			System.out.println(bus.toString());
		}
		return busList;
	}

	// 报站信息
	private Map<Long, StationInfo> infoMap() {
		Map<Long, StationInfo> infoMap = new HashMap<Long, StationInfo>();
		List<Map<String, Object>> dbInfoList = jdbcTemplate.queryForList("select * from srv_station_info t where t.route_id=" + routeId);

		System.out.println("select * from srv_station_info t where t.route_id=" + routeId);

		for (Map<String, Object> dbInfo : dbInfoList) {
			StationInfo info = new StationInfo();
			try {

				info.id = Long.valueOf(dbInfo.get("station_info_id").toString());
				info.stationId = Long.valueOf(dbInfo.get("station_id").toString());
				info.routesubId = Long.valueOf(dbInfo.get("routesub_id").toString());
				info.routeStationId = Long.valueOf(dbInfo.get("route_station_id").toString());
				info.orderNumber = Long.valueOf(dbInfo.get("order_number").toString());
				info.distance = Double.valueOf(dbInfo.get("distance").toString());
				info.needTime = Double.valueOf(dbInfo.get("need_time").toString());
				info.routeName = dbInfo.get("route_name").toString();
				info.stationName = dbInfo.get("station_name").toString();
				// info.stationCode = dbInfo.get("stationCode").toString();
				info.computerTime = new Date();
				infoMap.put(info.routeStationId, info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("info == " + infoMap.size());
		return infoMap;
	}

	// 参数信息
	private List<StationPara> paraList() {
		List<StationPara> paraList = new ArrayList<StationPara>();

		List<Map<String, Object>> dbParaList = jdbcTemplate.queryForList("select * from srv_station_para t where t.route_id=" + routeId);
		System.out.println("select * from srv_station_para t where t.route_id=" + routeId);

		for (Map<String, Object> dbPara : dbParaList) {
			StationPara para = new StationPara();

			para.id = Long.valueOf(dbPara.get("id").toString());
			para.routeId = Long.valueOf(dbPara.get("route_id").toString());
			para.routesubId = Long.valueOf(dbPara.get("routesub_id").toString());
			para.routeStationId = Long.valueOf(dbPara.get("route_station_id").toString());
			para.stationId = Long.valueOf(dbPara.get("station_id").toString());
			para.orderNumber = Long.valueOf(dbPara.get("order_number").toString());
			para.preStationTimeD = Double.valueOf(dbPara.get("PRE_STATION_TIME_D").toString());
			para.preStationTimeA = Double.valueOf(dbPara.get("PRE_STATION_TIME_A").toString());
			para.distance = Double.valueOf(dbPara.get("distance").toString());
			paraList.add(para);
		}
		System.out.println("para == " + paraList.size());
		return paraList;
	}

	@Test
	public void testForecast() throws ParseException, JsonParseException, JsonMappingException, IOException {
		Map<Long, StationInfo> infoMap = infoMap();

		StationForecast.computer(busList(), infoMap, paraList());
		List<StationInfo> infoList = new ArrayList<StationInfo>();
		infoList.addAll(infoMap.values());

		Collections.sort(infoList);
		for (StationInfo info : infoList) {

			System.out.println(info.toString());
		}

	}
}
