package com.gci.redis.store;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class RedisStoreFactoryTest {
	
	
	/*
	@Test
	public void testTxtFormat() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		File file = new File("d:/t.txt");

		RedisType redisType = new RedisType();

		redisType.setTypeKey("0a");
		// obuversion
		DbTable obuVersion = new DbTable();

		obuVersion.setTableName("dy_obu_version_test");
		obuVersion.setUpdateId("obuid");
		obuVersion.setUpdate(true);

		String[] obuVersionFields = { "obuid", "kernel_version", "rootfs_version", "app_version", "motherboard_sn", "total_mem", "wlan_mac", "gprs_imei", "sim_imsi", "cf_sn", "video_sn", "version",
				"last_updated", "dr_status", "dr_mode", "gps_fault", "cpu_temperature_fault", "cpu_fan_speed_fault", "lcd_fault" };
		for (String f : obuVersionFields) {
			DbField field = new DbField();
			field.setDbField(f);
			field.setDbType("string");
			field.setRedisField(f);
			field.setFormatter("yyyyMMdd HHmmss");
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

		for (String f : obuVersionHistFields) {
			DbField field = new DbField();
			field.setDbField(f);
			field.setDbType("string");
			field.setRedisField(f);
			field.setFormatter("yyyyMMdd HHmmss");
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

		mapper.writeValue(file, conf);
	}

	@Test
	public void testReadProperties() throws IOException {

			
		String redisRecord = "{\"type\":\"0a\",\"operation\":\"insert\",\"value\":{\"obuid\":\"913703\",\"kernel_version\":\"2.6.28\",\"rootfs_version\":\"206\",\"app_version\":\"03_0.22dGZ\"}}";


		RedisQueue redisQueue = RedisParseFactory.getRedisQueue("dpdb.queue.notify.db");
		RedisType redisType = redisQueue.getRedisTypes().get("0a");

		for (DbTable table : redisType.getDbTables()) {
			ObjectMapper objectMapper = new ObjectMapper();
			Map record = objectMapper.readValue(redisRecord, Map.class);
			System.out.println(table.sqlInsert((Map) record.get("value"))+";");
			System.out.println(table.sqlQuery((Map) record.get("value"))+";");
			System.out.println(table.sqlUpdate((Map) record.get("value"))+";");
		}
	}*/
}
