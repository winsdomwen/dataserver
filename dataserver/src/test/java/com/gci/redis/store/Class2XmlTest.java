package com.gci.redis.store;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;


public class Class2XmlTest {

	/*
	public void test() {
		
		//File file = new File("d:/redis2.xml");

		redisType.setTypeKey("0a");
		// obuversion
		DbTable obuVersion = new DbTable();

		obuVersion.setTableName("dy_obu_version_test");
		obuVersion.setUpdateId("obuid");
		obuVersion.setUpdate(true);

		String[] obuVersionFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn", "version",
				"last_updated", "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" };

		String[] obuVersionFieldType = { "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "integer",
				 "date", "string", "string", "string", "string", "string", "string" };

		
		
		for(int i = 0 ; i < obuVersionFields.length; i++){
			DbField field = new DbField();
			field.setDbField(obuVersionFields[i]);
			field.setRedisField(obuVersionFields[i]);
			field.setDbType(obuVersionFieldType[i]);
			if("date".equals(obuVersionFieldType[i])){
				field.setFormatter("yyyyMMdd HHmmss");
			}
			obuVersion.getFields().put(field.getRedisField(), field);
		}		
		redisType.getDbTables().add(obuVersion);

		// obu version hist
		DbTable obuVersionHist = new DbTable();
		obuVersionHist.setTableName("dy_obu_version_hist_test");
		obuVersionHist.setUpdateId("obuid");
		obuVersionHist.setUpdate(false);
		String[] obuVersionHistFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn",
				"version", "last_updated", "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" };

		String[] obuVersionHistFieldType = { "string", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string",
				"integer", "date", "string", "string", "string", "string", "string", "string" };		
		
		
		
		for(int i = 0 ; i < obuVersionHistFields.length; i++){
			DbField field = new DbField();
			field.setDbField(obuVersionHistFields[i]);
			field.setRedisField(obuVersionHistFields[i]);
			field.setDbType(obuVersionHistFieldType[i]);
			if("date".equals(obuVersionHistFieldType[i])){
				field.setFormatter("yyyyMMdd HHmmss");
			}
			obuVersionHist.getFields().put(field.getRedisField(), field);
		}		
		redisType.getDbTables().add(obuVersionHist);
		

		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey("dpdb.queue.notify.db");
		redisQueue.setTypeKey("type");
		redisQueue.setValueKey("value");
		redisQueue.getRedisTypes().put(redisType.getTypeKey(), redisType);

		RedisParseConfig conf = new RedisParseConfig();
		conf.getRedisQueues().put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		File file2 = new File("d:/redis.xml");
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
		
	}*/
		
	
	
	
	
	public void testPath(){
		File path = new File("D:/eclipse-jee-indigo-SR2-win32/workspace/apts-server/src/main/resources/model");
		System.out.println(path.isDirectory());
		if (path.exists() && path.isDirectory()) {// 输入的必须是目录

			System.out.println("path is directory");
			
			String[] fileNames = path.list();
			
			System.out.println("file length == "+fileNames.length);
			for (String fileName : fileNames) {
				System.out.println(fileName);
				//RedisParseConfig properties = mapper.readValue(RedisStoreFactoryTest.class.getResourceAsStream(FILE_PATH + "/" + fileName), RedisParseConfig.class);
				//newConfig.merge(properties);
			}
			
		}
	}

}
