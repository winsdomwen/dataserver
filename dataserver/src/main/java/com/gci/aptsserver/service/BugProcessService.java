package com.gci.aptsserver.service;

public interface BugProcessService {
	
	public void  process(String redisRecord,String sql_data,String errorMessage);

}
