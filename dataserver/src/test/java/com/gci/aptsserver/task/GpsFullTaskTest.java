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

public class GpsFullTaskTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	/*
	 * 生成配置文件
	 */
	@Test
	public void testGenerateGpsFullXML() {
		
		//File file = new File("d:/redis2.xml");

	
		List<String> keyList = new ArrayList<String>();
		keyList.add("id");	
		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator("assigned");
		primaryKey.setKey(keyList);		
		
		DbTable dbTable = new DbTable();

		dbTable.setTableName("dy_gps_full");
		dbTable.setPrimaryKey(primaryKey);
		dbTable.setUpdate(false);
		
		

		String[] tableFields = {"id","data_serial","obuid","package_type","hex","gps_type",
								"gps_unit","gps_valid","gps_time","latitude","longitude","ns_direction",
								"ew_direction","speed","direction","gps_mileage","satellite_count","route_code",
								"service","runningboard","running_no","additional","is_gps_valid","gpskey",
								"is_additional"};
		
		
		String[] redisFields = {"id","data_serial","obuid","package_type","hex","gps_type",
								"gps_unit","gps_valid","gps_time","latitude","longitude","ns_direction",
								"ew_direction","speed","direction","gps_mileage","satellite_count","route_code",
								"service","runningboard","running_no","additional","is_gps_valid","gpskey",
								"is_additional"};
		

		String[] tableFieldType = {"string", "integer","string", "string", "string" ,"string",
								   "string","integer","date","string","string","string",
								   "string","string","integer","string","string","string",
								   "string","string","string","string","string","string",
								   "string"};
							

		if(tableFields.length != redisFields.length || redisFields.length != tableFieldType.length)
			fail("parameter wrong");
		
		

		for(int i = 0 ; i < tableFields.length; i++){
			DbField field = new DbField();
			field.setDbField(tableFields[i]);
			field.setRedisField(redisFields[i]);
			field.setDbType(tableFieldType[i]);
			if("date".equals(tableFieldType[i])){
				field.setFormatter("yyyy-MM-dd HH24:mi:ss");
			}
			dbTable.getFields().put(field.getDbField(), field);
		}			

		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey("dpdb.queue.full.05");

		redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);

		RedisParseConfig conf = new RedisParseConfig();
		conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		File file2 = new File("d:/dy_gps_full.xml");
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
