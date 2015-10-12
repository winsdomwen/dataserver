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

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.PrimaryKey;
import com.gci.aptsserver.parse.RedisParseConfig;
import com.gci.aptsserver.parse.RedisQueue;
import com.gci.aptsserver.util.SpringUtil;
import com.gci.aptsserver.util.XmlUtil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer; 


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class DataStoreTaskTest {
	
	@Resource
	DataStoreTask dataStore;

	// @Test
	public void test() {
		//fail("Not yet implemented");
		
		dataStore = (DataStoreTask) SpringUtil.getBean("dataStoreTask");
				
		List<String> list = new ArrayList<String>();
		
		String data ="{\"type\":\"0a\",\"operation\":\"update\",\"value\":{\"obuid\":\"310051\",\"dr_status\":49,\"dr_mode\":55}}";
		list.add(data);
		
		//dataStore.save(list);
		
	}
	
	//@Test
	public void TestRapidSave(){
		
		List<String> list = new ArrayList<String>();
		
		String data ="{\"type\":\"0a\",\"operation\":\"update\",\"value\":{\"obuid\":\"310051\",\"dr_status\":49,\"dr_mode\":55}}";
		//list.add(data);
		
		Map map = (Map)JSON.parseObject(data);
		
		System.out.println(map.get("type"));	
		
	}
	
	
	
	    @Test
		public void testGenerateRealIpXML() {
			
			
			List<String> keyList = new ArrayList<String>();
			keyList.add("obuid");	
			
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setGenerator("assigned");
			primaryKey.setKey(keyList);	
			
			DbTable dbTable = new DbTable();

			dbTable.setTableName("dy_real_ip");
			dbTable.setPrimaryKey(primaryKey);
			dbTable.setUpdate(true);

			String[] tableFields = { "obuid", "bus_id", "ip", "last_updated"};
			String[] redisFields = { "obuid", "bus_id", "ip", "last_updated"};
			

			String[] tableFieldType = { "string", "integer", "string", "date" };


			if(tableFields.length != redisFields.length || redisFields.length != tableFieldType.length)
				fail("parameter wrong");
			
			for(int i = 0 ; i < tableFields.length; i++){
				DbField field = new DbField();
				field.setIndex(i+1);
				field.setDbField(tableFields[i]);
				field.setRedisField(redisFields[i]);
				field.setDbType(tableFieldType[i]);
				if("date".equals(tableFieldType[i])){
					field.setFormatter("yyyyMMdd HH24miss");
				}
				dbTable.getFields().put(field.getRedisField(), field);
			}	
			
			RedisQueue redisQueue = new RedisQueue();
			redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
			redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);
			

			RedisParseConfig conf = new RedisParseConfig();
			conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
			
			
			String xml = XmlUtil.simpleobject2xml(conf);
			System.out.println(xml);
			
			File file2 = new File("d:/dy_real_ip.xml");
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
		
		@Test
		public void testGenerateDoorXML() {
			
			
			List<String> keyList = new ArrayList<String>();
			keyList.add("data_serial");	
			
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setGenerator("assigned");
			primaryKey.setKey(keyList);				
			
			DbTable dbTable = new DbTable();

			dbTable.setTableName("dy_door");
			dbTable.setPrimaryKey(primaryKey);
			dbTable.setUpdate(false);

			String[] tableFields = { "data_serial", "door_type", "route_code", "service","door_open_time","door_close_time",
					                      "additional","is_additional","bus_id","obu_id","bus_stop_code","station_id","route_sta_id",
					                      "trip_id","order_number","date_created"};
			
			String[] redisFields = { "data_serial", "door_type", "route_code", "service","door_open_time","door_close_time",
	                "additional","is_additional","bus_id","obu_id","bus_stop_code","station_id","route_sta_id",
	                "trip_id","order_number","date_created"};		
			

			String[] tableFieldType = { "integer", "string", "string", "string" ,"date","date",
											 "string","string","integer","string","string","integer","integer",
											 "integer","integer","date"};


			if(tableFields.length != redisFields.length || redisFields.length != tableFieldType.length)
				fail("parameter wrong");
			
			for(int i = 0 ; i < tableFields.length; i++){
				DbField field = new DbField();
				field.setIndex(i+1);
				field.setDbField(tableFields[i]);
				field.setRedisField(redisFields[i]);
				field.setDbType(tableFieldType[i]);
				if("date".equals(tableFieldType[i])){
					field.setFormatter("yyyyMMdd HH24miss");
				}
				dbTable.getFields().put(field.getRedisField(), field);
			}	
			
			RedisQueue redisQueue = new RedisQueue();
			redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
			redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);

			RedisParseConfig conf = new RedisParseConfig();
			conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
			
			
			String xml = XmlUtil.simpleobject2xml(conf);
			System.out.println(xml);
			
			File file2 = new File("d:/dy_door.xml");
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
		
		@Test
		public void testGenerateTripLogXML(){
			
			List<String> keyList = new ArrayList<String>();
			keyList.add("trip_id");	
			
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setGenerator("assigned");
			primaryKey.setKey(keyList);			
			
			DbTable dbTable = new DbTable();

			dbTable.setTableName("dy_trip_log");
			dbTable.setPrimaryKey(primaryKey);
			dbTable.setUpdate(false);
			
			

			String[] tableFields = {"trip_id","obuid","bus_id","route_id", "route_code","route_name","service",
										 "route_sub_id","route_sub_name","organ_id","number_plate","employee_id","employee_name",
										 "qualification","trip_begin_time","trip_end_time","trip_begin_routestaid","trip_end_routestaid",
										 "trip_begin_stationid","trip_end_stationid","trip_begin_stationname","trip_end_stationname"};
			
			
			String[] redisFields = {"trip_id","obuid","bus_id", "route_id","route_code","route_name","service",
									 "routesub_id","route_sub_name","organ_id","number_plate","employee_id","employee_name",
									 "qualification","trip_begin_time","trip_end_time","trip_begin_routestaid","trip_end_routestaid",			
									 "trip_begin_stationid","trip_end_stationid","trip_begin_stationname","trip_end_stationname"};	
			

			String[] tableFieldType = { "integer", "integer", "string", "integer" ,"string","string","string",
										"integer","string","integer","string","integer","string",
											 "string","date","date","integer","integer",
											 "integer","integer","string","string"};
								

			if(tableFields.length != redisFields.length || redisFields.length != tableFieldType.length)
				fail("parameter wrong");
			
			

			for(int i = 0 ; i < tableFields.length; i++){
				DbField field = new DbField();
				field.setIndex(i+1);
				field.setDbField(tableFields[i]);
				field.setRedisField(redisFields[i]);
				field.setDbType(tableFieldType[i]);
				if("date".equals(tableFieldType[i])){
					field.setFormatter("yyyyMMdd HH24miss");
				}
				dbTable.getFields().put(field.getDbField(), field);
			}				
			
			RedisQueue redisQueue = new RedisQueue();
			redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
			redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);

			RedisParseConfig conf = new RedisParseConfig();
			conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
			
			
			String xml = XmlUtil.simpleobject2xml(conf);
			System.out.println(xml);
			
			File file2 = new File("d:/dy_trip_log.xml");
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
		
		@Test
		public void testGenerateObuStatusXML() {
			
			
			List<String> keyList = new ArrayList<String>();
			keyList.add("bus_id");	
			
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setGenerator("assigned");
			primaryKey.setKey(keyList);	
			

			DbTable dbTable = new DbTable();

			dbTable.setTableName("dy_bus_online_hist");
			dbTable.setPrimaryKey(primaryKey);
			dbTable.setUpdate(false);

			String[] tableFields = { "obuid", "bus_id", "ip", "up_time", "up_gpskey", "up_longitude", "up_latitude", "down_time",
					"down_gpskey", "down_longitude", "down_latitude","last_updated", "version"};

			String[] redisFields = { "obuid", "bus_id", "ip", "up_time", "up_gpskey", "up_longitude", "up_latitude", "down_time",
					"down_gpskey", "down_longitude", "down_latitude","last_updated", "version"};
					
			
			String[] tableFieldType = { "string", "integer", "string", "date", "string", "double", "double", "date",
					 "string", "double", "double", "date", "integer" };		
			
			
			for(int i = 0 ; i < tableFields.length; i++){
				DbField field = new DbField();
				field.setIndex(i+1);
				field.setDbField(tableFields[i]);
				field.setRedisField(redisFields[i]);
				field.setDbType(tableFieldType[i]);
				if("date".equals(tableFieldType[i])){
					field.setFormatter("yyyyMMdd HH24miss");
				}
				dbTable.getFields().put(field.getDbField(), field);
			}				
			
			RedisQueue redisQueue = new RedisQueue();
			redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
			redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);

			RedisParseConfig conf = new RedisParseConfig();
			conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
			
			
			String xml = XmlUtil.simpleobject2xml(conf);
			System.out.println(xml);
			
			File file2 = new File("d:/obu_status.xml");
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

		@Test
		public void testGenerateObuVersionXML() {
			
			
			List<String> keyList = new ArrayList<String>();
			keyList.add("obuid");	
			
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setGenerator("assigned");
			primaryKey.setKey(keyList);	
			
			DbTable dbTable = new DbTable();

			dbTable.setTableName("dy_obu_version");
			dbTable.setPrimaryKey(primaryKey);
			dbTable.setUpdate(true);

			String[] tableFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn", "version",
					 "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" };

			
			String[] redisFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn", "version",
					 "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" };
			
			
			String[] tableFieldType = { "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "integer",
					  "string", "string", "string", "string", "string", "string" };

			
			for(int i = 0 ; i < tableFields.length; i++){
				DbField field = new DbField();
				field.setIndex(i+1);
				field.setDbField(tableFields[i]);
				field.setRedisField(redisFields[i]);
				field.setDbType(tableFieldType[i]);
				if("date".equals(tableFieldType[i])){
					field.setFormatter("yyyyMMdd HH24miss");
				}
				dbTable.getFields().put(field.getDbField(), field);
			}				
			
			RedisQueue redisQueue = new RedisQueue();
			redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
			redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);
			
			RedisParseConfig conf = new RedisParseConfig();
			conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
			
			String xml = XmlUtil.simpleobject2xml(conf);
			System.out.println(xml);
			
			File file2 = new File("d:/dy_obu_version.xml");
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
		
		@Test
		public void testGenerateObuVersionHistXML() {
			
			List<String> keyList = new ArrayList<String>();
			keyList.add("obuid");	
			
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setGenerator("assigned");
			primaryKey.setKey(keyList);	
			
			DbTable dbTable = new DbTable();

			dbTable.setTableName("dy_obu_version_hist");
			dbTable.setPrimaryKey(primaryKey);
			dbTable.setUpdate(false);

			String[] tableFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn", "version",
					 "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" ,"remark"};

			
			String[] redisFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn", "version",
					 "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" ,"remark"};
			
			
			String[] tableFieldType = { "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "integer",
					  "string", "string", "string", "string", "string", "string" ,"string"};

			
			for(int i = 0 ; i < tableFields.length; i++){
				DbField field = new DbField();
				field.setIndex(i+1);
				field.setDbField(tableFields[i]);
				field.setRedisField(redisFields[i]);
				field.setDbType(tableFieldType[i]);
				if("date".equals(tableFieldType[i])){
					field.setFormatter("yyyyMMdd HH24miss");
				}
				dbTable.getFields().put(field.getDbField(), field);
			}				
			
			RedisQueue redisQueue = new RedisQueue();
			redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
			redisQueue.getRedisTables().put(dbTable.getTableName(), dbTable);
			
			RedisParseConfig conf = new RedisParseConfig();
			conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
			
			String xml = XmlUtil.simpleobject2xml(conf);
			System.out.println(xml);
			
			File file2 = new File("d:/dy_obu_version_hist.xml");
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
