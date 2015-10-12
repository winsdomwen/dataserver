package com.gci.aptsserver.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class StoreRunnableTest {

	@Resource
	//DataStoreTask storeRunnable;

	List<String> redisRecords = new ArrayList<String>();

	private int INSERT_COUNT = 10000;

	@Before
	public void before() {
		//storeRunnable.setRedisQueueKey("dpdb.queue.notify.db");
		//initRedisData();
	}

	// @Test
	public void testRun() {
		//storeRunnable.run();
	}

	/*
	public void initRedisData() {
		int count = 0;
		while (count < INSERT_COUNT) {
			List<String> records = storeRunnable.getRedisData();
			for (String record : records) {
				if (record.indexOf("0a") > -1) {
					count++;
					redisRecords.add(record);
				}
			}
		}
	}*/

	@Test
	public void testJdbc() throws SQLException {
		Date beginTime=new Date();
		System.out.println(redisRecords.size());
		
		//storeRunnable.save(redisRecords);
		System.out.println(new Date().getTime()-beginTime.getTime());
	}

}
