package com.gci.aptsserver.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gci.aptsserver.service.BugProcessService;


@Service(("bugProcessService"))
public class BugProcessServiceImpl implements BugProcessService{

	@Resource
	protected JdbcTemplate jdbcTemplate;
	
	@Override
	public void process(String redisData,String sqlData,String errorMessage) {
		// TODO Auto-generated method stub
		

	 if(redisData != null){
		 redisData.replaceAll("\"", "\\\\\"");
		 redisData.replaceAll("'", "\'");
		 redisData = redisData.substring(0,1000);
		 
	 }
	 if(sqlData != null){
		 redisData.replaceAll("\"", "\\\\\"");
		 redisData.replaceAll("'", "\'");
		 //sqlData = sqlData.substring(0,1000);	
	 }
	 if(errorMessage != null){
		 redisData.replaceAll("\"", "\\\\\"");
		 redisData.replaceAll("'", "\'");
		 sqlData = sqlData.substring(0,1000);			 
		 errorMessage = errorMessage.substring(0,1000);
	 }
	 
	 
   	 StringBuilder stringBuilder = new StringBuilder();

     stringBuilder.append("insert into dy_bug ");
     stringBuilder.append("(redis_data, sql_data, error_message) ");
     stringBuilder.append(" values ");
     stringBuilder.append("('"+redisData+"','"+sqlData+"','"+errorMessage+"')");
     
     System.out.println(stringBuilder.toString());
     
     //jdbcTemplate.execute(stringBuilder.toString());
		
	}

}
