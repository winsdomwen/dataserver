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



public class RunningBusTaskTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	
	@Test
	public void testGenerateRunningBus() {
		
		List<String> keyList = new ArrayList<String>();
		keyList.add("bus_id");	
		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator("assigned");
		primaryKey.setKey(keyList);	
		
		
		DbTable dbTable = new DbTable();

		dbTable.setTableName("dy_runningbus");
		dbTable.setPrimaryKey(primaryKey);
		dbTable.setUpdate(false);

		String[] tableFields = {"run_date","bus_id", "obu_id","bus_code","number_plate","employee_id",
									 "employee_name","operate_type","route_sta_id","route_station_name","trip_log_id","direction",
									 "latest_ad_time","from_station_id","from_station_name","to_station_id","to_station_name","plan_arrive_time",
									 "plan_begin_time","real_begin_time","real_end_time","route_id","route_name","route_id_run",
									 "route_name_run","route_sub_id","route_sub_name","service_type","run_status","running_no",
									 "service_number","control_status","longitude","latitude","bus_speed","bus_direction","gpskey",
									 "distance_curr","distance_next","distance_run","gps_station_id","gps_station_name","station_id",
									 "station_name","current_trip_log_id","route_name_belongto","order_number","ip",
									 "obu_time","redis_time"};
		
		
		String[] redisFields = {"run_date","bus_id", "obu_id","bus_code","number_plate","employee_id",
				 "employee_name","operate_type","route_sta_id","route_station_name","trip_log_id","routesub_direction",
				 "ad_time","first_station_id","first_station_name","last_station_id","last_station_name","plan_arrive_time",
				 "plan_begin_time","real_begin_time","real_end_time","route_id_belongto","route_name","route_id",
				 "route_name_run","routesub_id","route_sub_name","service_type","run_status","runningboard",
				 "service_number","control_status","longitude","latitude","speed","direction","gpskey",
				 "distance_curr","distance_next","distance_run","gps_station_id","gps_station_name","station_id",
				 "station_name","current_trip_log_id","route_name_belongto","order_number","ip",
				 "gps_time","redis_time"};		
		

		String[] tableFieldType = { "date", "integer", "string", "string" ,"string","integer",
										 "string","string","integer","string","integer","string",
										 "date","integer","string","integer","string","date",
										 "date","date","date","integer","string","integer",
										 "string","integer","string","string","string","string",
										 "string","string","double","double","double","string","string",
										 "double","double","double","integer","string","integer",
										 "string","integer","string","integer","string",
										 "date","date"};


		for(int i = 0 ; i < tableFields.length; i++){
			DbField field = new DbField();
			field.setDbField(tableFields[i]);
			field.setRedisField(redisFields[i]);
			field.setDbType(tableFieldType[i]);
			if("date".equals(tableFieldType[i])){
				field.setFormatter("yyyyMMdd HH24miss");
			}
			dbTable.getFields().put(field.getDbField(), field);
		}			

		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey("dpdb.rt.bus_id=");

		redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);

		RedisParseConfig conf = new RedisParseConfig();
		conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		File file2 = new File("d:/runningbus.xml");
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
