package com.gci.aptsserver.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TripBegin {
	
	Long busId;
    String obuId;
    String direction;
    String dispatcherUser;
    Long employeeId;
    String employeeName;
    String numberPlate;
    String planTime;
    Long routeIdBelongTo;
    Long routeIdRun;
    String routeNameBelongTo;
    String routeNameRun;
    Long routeSubId;
    String routeSubName;
    Date runDate;
    String serviceType;
    Long originStatus;
    String busCode;
    String qualification;
    
    
    public String getInertSql(){
    	
    	 Date date = new Date();
    	 
    	 //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	 //String plan_time = sdf.format(date);
    	 if(planTime == null){
    		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
    		 planTime = sdf.format(date);
    	 }    	 
    	 
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	 String run_date = sdf.format(date);
    	 
    	 this.originStatus = (long) 0;
    	 
    	
    	 StringBuilder stringBuilder = new StringBuilder();

         stringBuilder.append(" insert into dy_triplog_begin_tmp ");
         stringBuilder.append(" (triplog_id, version, bus_id,obu_id, direction, dispatcher_user, employee_id, employee_name, number_plate, plan_time, route_id_belong_to, route_id_run, route_name_belong_to, route_name_run, route_sub_id, route_sub_name, run_date, service_type, origin_status, bus_code, qualification) ");
         stringBuilder.append(" values ");
         stringBuilder.append("(SEQ_TRIPLOG_ID.Nextval, 0, ");
         stringBuilder.append(busId+",");
         stringBuilder.append("'"+obuId+"',");
         stringBuilder.append("'"+direction+"',");
         stringBuilder.append("'"+dispatcherUser+"',");
         stringBuilder.append(employeeId+",");
         stringBuilder.append("'"+employeeName+"',");
         stringBuilder.append("'"+numberPlate+"',");
         stringBuilder.append("to_date('"+planTime+"','yyyymmdd hh24miss'),");
         stringBuilder.append(routeIdBelongTo+",");
         stringBuilder.append(routeIdRun+",");
         stringBuilder.append("'"+routeNameBelongTo+"',");
         stringBuilder.append("'"+routeNameRun+"',");
         stringBuilder.append(routeSubId+",");
         stringBuilder.append("'"+routeSubName+"',");       
         stringBuilder.append("to_date('"+run_date+"','yyyyMMdd'),");
         stringBuilder.append("'"+serviceType+"',"); 
         stringBuilder.append(originStatus+",");
         stringBuilder.append("'"+busCode+"',");
         stringBuilder.append("'"+qualification+"')");
         
         return stringBuilder.toString();
    }
    
    
    public Long getBusId() {
		return busId;
	}
	public void setBusId(Long busId) {
		this.busId = busId;
	}
	public String getObuId() {
		return obuId;
	}
	public void setObuId(String obuId) {
		this.obuId = obuId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getDispatcherUser() {
		return dispatcherUser;
	}
	public void setDispatcherUser(String dispatcherUser) {
		this.dispatcherUser = dispatcherUser;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getNumberPlate() {
		return numberPlate;
	}
	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	public Long getRouteIdBelongTo() {
		return routeIdBelongTo;
	}
	public void setRouteIdBelongTo(Long routeIdBelongTo) {
		this.routeIdBelongTo = routeIdBelongTo;
	}
	public Long getRouteIdRun() {
		return routeIdRun;
	}
	public void setRouteIdRun(Long routeIdRun) {
		this.routeIdRun = routeIdRun;
	}
	public String getRouteNameBelongTo() {
		return routeNameBelongTo;
	}
	public void setRouteNameBelongTo(String routeNameBelongTo) {
		this.routeNameBelongTo = routeNameBelongTo;
	}
	public String getRouteNameRun() {
		return routeNameRun;
	}
	public void setRouteNameRun(String routeNameRun) {
		this.routeNameRun = routeNameRun;
	}
	public Long getRouteSubId() {
		return routeSubId;
	}
	public void setRouteSubId(Long routeSubId) {
		this.routeSubId = routeSubId;
	}
	public String getRouteSubName() {
		return routeSubName;
	}
	public void setRouteSubName(String routeSubName) {
		this.routeSubName = routeSubName;
	}
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Long getOriginStatus() {
		return originStatus;
	}
	public void setOriginStatus(Long originStatus) {
		this.originStatus = originStatus;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	
}
