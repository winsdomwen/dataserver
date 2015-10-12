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

public class TripLogBeginTaskTest {

	
	@Test
	public void testGenerateXML() {
		
		//File file = new File("d:/redis2.xml");

		List<String> keyList = new ArrayList<String>();
		keyList.add("triplog_id");	
		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator("assigned");
		primaryKey.setKey(keyList);		
		
		// obuversion
		DbTable dbTable = new DbTable();

		dbTable.setTableName("dy_triplog_begin");
		dbTable.setPrimaryKey(primaryKey);
		dbTable.setUpdate(false);
		
		

		String[] tableFields = {"obu_id","data_serial","route_code","running_no","obu_time","trip_code",
				                "redis_time","triplog_id","bus_id","bus_code","bus_name","number_plate","organ_id",
				                "organ_code","organ_name","route_id_belong_to","route_name_belongto","route_id",
				                "route_name","direction","route_sub_id","route_sub_name","from_station_id",
				                "from_station_name","to_station_id","to_station_name","service_number","service_name",
				                "employee_id","employee_name","qualification","gpskey"};
		
		
		String[] redisFields = {"obuid","data_serial","route_code","trip_code","local_time","oper_content",
				                "redis_time","_id_","bus_id","bus_code","bus_name","number_plate","organ_id",
				                "organ_code","organ_name","route_id_belongto","route_name_belongto","route_id",
				                "route_name","routesub_direction","routesub_id","route_sub_name","first_station_id",
				                "first_station_name","last_station_id","last_station_name","service_number","service_name",
				                "employee_id","employee_name","qualification","gpskey"};	
		

		String[] tableFieldType = { "string","integer","string","string","date","string",
									"date","string","string","string","string","string","integer",
									"string","string","integer","string","integer","string",
									"string","integer","string","integer","string",
									"integer","string","string","string","integer",
									"string","string","string"};
							

		System.out.println(tableFields.length+","+ redisFields.length+","+tableFieldType.length);
		
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
		redisQueue.setRedisQueueKey("dpdb.queue.40.01");
		
		redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);
		

		RedisParseConfig conf = new RedisParseConfig();
		conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		File file2 = new File("d:/dy_triplog_begin.xml");
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
