package com.gci.aptsserver.parse;

/**
 * 数据库字段类型定义
 * 
 * @ClassName: FieldBean
 * @Description: TODO
 * @author Kuhn
 * @date Dec 27, 2012 12:47:54 PM
 * 
 */
public class DbField {

	private int index;
	private String dbField;
	private String redisField;
	private String dbType;
	private String formatter;	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getDbField() {
		return dbField;
	}

	public void setDbField(String dbField) {
		this.dbField = dbField;
	}

	public String getRedisField() {
		return redisField;
	}

	public void setRedisField(String redisField) {
		this.redisField = redisField;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
}
