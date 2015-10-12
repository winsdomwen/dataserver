package com.gci.aptsserver.task;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.PrimaryKey;
import com.gci.aptsserver.parse.RedisParseConfig;
import com.gci.aptsserver.parse.RedisQueue;
import com.gci.aptsserver.util.XmlUtil;


public class AbnormalTaskTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	/*
	 * 
	 * 生成XML文件时，记得把系统默认插入的字段去掉，如DATE_CREATED
	 */
	
	@Test
	public void testGenerateXML() {
		
		
		//设置主键
		List<String> keyList = new ArrayList<String>();
		keyList.add("data_serial");	
		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator("assigned");
		primaryKey.setKey(keyList);		
		
		
		DbTable table = new DbTable();

		table.setTableName("dy_abnormal");
		table.setPrimaryKey(primaryKey);
		table.setUpdate(false);

		String[] dbFields = { "data_serial", "anti_rule_type", "route_code", "service","busstop_code","bus_id",
				            "obu_id","station_id","route_sta_id","trip_id","order_number","mileage","longitude",
				            "latitude","runningboard","gps_unit","gps_time","gpskey","route_id","routesub_id",
				            "service_type","gps_mileage"};
		
		String[] redisFields = { "data_serial", "anti_rule_type", "route_code", "service","busstop_code","bus_id",
	            "obu_id","station_id","route_sta_id","trip_id","order_number","mileage","longitude",
	            "latitude","runningboard","gps_unit","gps_time","gpskey","route_id","routesub_id",
	            "service_type","gps_mileage"};

		String[] fieldType = { "string", "string", "string", "string" ,"string","integer",
							   "string","integer","integer","integer","integer","double","double",
							   "double","string","string","date","string","integer","integer",
							   "string","double"};


		for(int i = 0 ; i < dbFields.length; i++){
			DbField field = new DbField();
			field.setIndex(i+1);
			field.setDbField(dbFields[i]);
			field.setRedisField(redisFields[i]);
			field.setDbType(fieldType[i]);
			if("date".equals(fieldType[i])){
				field.setFormatter("yyyyMMdd HH24miss");
			}
			table.getFields().put(field.getDbField(), field);
		}		
		//redisType.getDbTables().add(table);
		//redisType.setTable(table);
	

		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey("dpdb.queue.income.0b");
		//redisQueue.setTypeKey("type");
		//redisQueue.setValueKey("value");
		//redisQueue.getRedisTypes().put(redisType.getTypeKey(), redisType);
		redisQueue.getRedisTables().put(table.getTableName(), table);

		RedisParseConfig conf = new RedisParseConfig();
		Map<String, RedisQueue> map = conf.getRedisQueues();
		map.put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		File file2 = new File("d:/abnormal.xml");
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
