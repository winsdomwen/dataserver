package com.gci.aptsserver.station.bean;

import java.util.Date;

public class BusBean {

	public Long busId;// 车辆id
	public Long stationId;// 当前站台
	public Long routeStationId;// 当前站点
	public Long routeSubId;// 子线路
	public String adFlag;// 进出站标志
	public Date adTime;// 进出站时间
	public Long orderNumber;// 当前站的站序
	public Long adMileage;
	public Long gpsMileage;
	
	@Override
	public String toString() {
		return "BusBean [busId=" + busId + ", stationId=" + stationId + ", routeStationId=" + routeStationId + ", routeSubId=" + routeSubId + ", adFlag=" + adFlag + ", adTime=" + adTime
				+ ", orderNumber=" + orderNumber + ", adMileage=" + adMileage + ", gpsMileage=" + gpsMileage + "]";
	}
	
	
}
