package com.gci.aptsserver.station.bean;

public class StationPara implements Comparable<StationPara> {

	public Long id;
	public Long routeId;
	public Long routesubId;
	public Long routeStationId;
	public Long stationId;
	public Long orderNumber;
	public Double preStationTimeD;
	public Double preStationTimeA;
	public Double distance;

	@Override
	public int compareTo(StationPara o) {
		if (o != null) {
			return this.orderNumber.compareTo(o.orderNumber);
		}
		return 0;
	}
}
