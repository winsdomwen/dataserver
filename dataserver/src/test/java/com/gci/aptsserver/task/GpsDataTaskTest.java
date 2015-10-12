package com.gci.aptsserver.task;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.PrimaryKey;
import com.gci.aptsserver.parse.RedisParseConfig;
import com.gci.aptsserver.parse.RedisQueue;
import com.gci.aptsserver.util.XmlUtil;


public class GpsDataTaskTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	
	/*
	 * 生成配置文件
	 */
	@Test
	public void testGenerateGpsDataXML() {
		
	
		List<String> keyList = new ArrayList<String>();
		keyList.add("obuid");	
		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator("assigned");
		primaryKey.setKey(keyList);		


		// obuversion
		DbTable dbTable = new DbTable();

		dbTable.setTableName("dy_gps");
		dbTable.setPrimaryKey(primaryKey);
		dbTable.setUpdate(false);
		
		

		String[] tableFields = {"obuid","bus_id", "revtime","obutime","longitude","latitude",
									 "gpskey","direction","speed","running_no","remark",
									 "data_serial","gps_mileage","satellite_count","route_code","service","additional",				
									 "gps_valid","trip_id","longitude1","latitude1"};
		
		
		String[] redisFields = {"obuid","bus_id", "revtime","obutime","longitude","latitude",
								 "gpskey","direction","speed","running_no","remark",
								 "data_serial","gps_mileage","satellite_count","route_code","service","additional",				
								 "gps_valid","trip_id","longitude1","latitude1"};	
		

		String[] tableFieldType = { "string", "integer", "date", "date" ,"double","double",
										 "string","integer","double","string","string",
										 "string","integer","integer","string","string","string",
										 "string","integer","double","double"};
							

		if(tableFields.length != redisFields.length || redisFields.length != tableFieldType.length)
			fail("parameter wrong");
		
		

		for(int i = 0 ; i < tableFields.length; i++){
			DbField field = new DbField();
			field.setIndex(i+1);
			field.setDbField(tableFields[i]);
			field.setRedisField(redisFields[i]);
			field.setDbType(tableFieldType[i]);
			if("date".equals(tableFieldType[i])){
				field.setFormatter("yyyy-MM-dd HH24:mi:ss");
			}
			dbTable.getFields().put(field.getRedisField(), field);
		}		
		//redisType.getDbTables().add(obuVersion);
	

		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey("dpdb.queue.income.05");
		//redisQueue.setTypeKey("type");
		//redisQueue.setValueKey("value");
		//redisQueue.getRedisTypes().put(redisType.getTypeKey(), redisType);
		redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);

		RedisParseConfig conf = new RedisParseConfig();
		conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		File file2 = new File("d:/dy_gps.xml");
		DataOutputStream output;
		try {
			output = new DataOutputStream(new FileOutputStream(file2));
	        output.write(xml.getBytes());
	        output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
	}


}
