package com.gci.aptsserver.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据库表格式定义
 * 
 * @ClassName: TableBean
 * @Description: TODO
 * @author Kuhn
 * @date Dec 27, 2012 12:47:20 PM
 * 
 */
public class DbTable {

	private String tableName;
	private PrimaryKey primaryKey;
	private String introduction;

	private boolean update = false;
	private Map<String, DbField> fields = new HashMap<String, DbField>();//Map主键对应redisField	

	
	/*
	 * 判断主键是否存在
	 */
	public boolean hasPrimaryKey(Map<String,String>  record){
		
		List<String> keyList = primaryKey.getKey();
		
		for(String key : keyList){
			String data = record.get(key);
			if(null ==  data  || "".equals(data) ){
				return false;
			}
		}
		return true;	
	}	
	
	/*
	 * 判断某个cloumn是否是主键
	 */
	public boolean isPrimaryKey(String cloumn){
		
		List<String> keyList = primaryKey.getKey();
		
		for(String key : keyList){
			if(key.equals(cloumn)){
				return true;
			}
		}
		return false;	
	}
	
	
	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

	public Map<String, DbField> getFields() {
		return fields;
	}

	public void setFields(Map<String, DbField> fields) {
		this.fields = fields;
	}
	
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}


	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
