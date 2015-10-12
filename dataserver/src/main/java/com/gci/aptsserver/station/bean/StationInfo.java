package com.gci.aptsserver.station.bean;

import java.util.Date;

public class StationInfo implements Comparable<StationInfo> {

	public Long id;// 主键ID，自增
	public Long routeId;
	public Long stationId;
	public Double distance;
	public Double needTime;
	public Long routesubId;
	public Long busId;
	public String routeName;
	public String stationName;
	public Date computerTime;
	public String stationMark;
	public Long routeStationId;
	public Long orderNumber;
	public String stationCode;

	public Date estimateAdTime;// 预计到达本站时间

	@Override
	public int compareTo(StationInfo o) {
		if (o != null && o.stationMark != null && this.stationMark != null) {
			if (o.stationMark.equals(this.stationMark))
				return o.stationMark.compareTo(this.stationMark);
			return o.orderNumber.compareTo(this.orderNumber);
		}
		return 0;
	}

	@Override
	public String toString() {
		return "StationInfo [stationId=" + stationId + ", distance=" + distance + ", needTime=" + needTime + ", routesubId=" + routesubId + ", busId=" + busId + ", stationName=" + stationName
				+ ", stationMark=" + stationMark + ", orderNumber=" + orderNumber + ", estimateAdTime=" + estimateAdTime + "]";
	}

}
