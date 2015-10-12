package com.gci.aptsserver.parse;

import java.util.Map;

public interface IDatabase {
	
	public void  setTable(DbTable table);	
	public String sqlQuery(Map<String, String> record);
	public String sqlInsert(Map<String, String> record);
	public String getInsertScript();
	public String sqlUpdate(Map<String, String> record);	

}
