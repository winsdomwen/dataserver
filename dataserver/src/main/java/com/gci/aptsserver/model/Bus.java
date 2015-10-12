package com.gci.aptsserver.model;

import java.util.Date;

public class Bus {
  
	Long id;//车辆id
    Long equipmentId;//终端id,对应终端表的主键id
    String obuChipCode;//终端芯片编码
    String obuId;//OBUID

    Long simId;//simID,对应SIM卡表主键ID
    String simCode;//SIM卡编号
    String phoneNumber;//SIM卡号码
    String simChipCode;//SIM卡芯片编码


    String busName;//车辆名称
    String busCode;//车辆编码
    String numberPlate;//车辆牌照
    String busType;//车辆类型
    Date dateCreated;//创建日期
    Date lastUpdated;//最修改日期
    String createUser;//起草人
    String remark;//备注
    String isActive;//是否激活,用于删除操作(0:未激活,1:激活)
    
    
    public Long getId() {
  		return id;
  	}
  	public void setId(Long id) {
  		this.id = id;
  	}
  	public Long getEquipmentId() {
  		return equipmentId;
  	}
  	public void setEquipmentId(Long equipmentId) {
  		this.equipmentId = equipmentId;
  	}
  	public String getObuChipCode() {
  		return obuChipCode;
  	}
  	public void setObuChipCode(String obuChipCode) {
  		this.obuChipCode = obuChipCode;
  	}
  	public String getObuId() {
  		return obuId;
  	}
  	public void setObuId(String obuId) {
  		this.obuId = obuId;
  	}
  	public Long getSimId() {
  		return simId;
  	}
  	public void setSimId(Long simId) {
  		this.simId = simId;
  	}
  	public String getSimCode() {
  		return simCode;
  	}
  	public void setSimCode(String simCode) {
  		this.simCode = simCode;
  	}
  	public String getPhoneNumber() {
  		return phoneNumber;
  	}
  	public void setPhoneNumber(String phoneNumber) {
  		this.phoneNumber = phoneNumber;
  	}
  	public String getSimChipCode() {
  		return simChipCode;
  	}
  	public void setSimChipCode(String simChipCode) {
  		this.simChipCode = simChipCode;
  	}
  	public String getBusName() {
  		return busName;
  	}
  	public void setBusName(String busName) {
  		this.busName = busName;
  	}
  	public String getBusCode() {
  		return busCode;
  	}
  	public void setBusCode(String busCode) {
  		this.busCode = busCode;
  	}
  	public String getNumberPlate() {
  		return numberPlate;
  	}
  	public void setNumberPlate(String numberPlate) {
  		this.numberPlate = numberPlate;
  	}
  	public String getBusType() {
  		return busType;
  	}
  	public void setBusType(String busType) {
  		this.busType = busType;
  	}
  	public Date getDateCreated() {
  		return dateCreated;
  	}
  	public void setDateCreated(Date dateCreated) {
  		this.dateCreated = dateCreated;
  	}
  	public Date getLastUpdated() {
  		return lastUpdated;
  	}
  	public void setLastUpdated(Date lastUpdated) {
  		this.lastUpdated = lastUpdated;
  	}
  	public String getCreateUser() {
  		return createUser;
  	}
  	public void setCreateUser(String createUser) {
  		this.createUser = createUser;
  	}
  	public String getRemark() {
  		return remark;
  	}
  	public void setRemark(String remark) {
  		this.remark = remark;
  	}
  	public String getIsActive() {
  		return isActive;
  	}
  	public void setIsActive(String isActive) {
  		this.isActive = isActive;
  	}
    
    
    
    

}
