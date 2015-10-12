package com.gci.aptsserver.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.gci.aptsserver.task.DataStoreTask;

@Controller
@RequestMapping(value = "/test")
public class TestController {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private JedisPool jedisPool;

	@Resource
	private DataStoreTask storeRunnable;

	private final String TEST_KEY = "dpdb.queue.notify.db";

	Logger logger =LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(value = "/redis")
	@ResponseBody
	public List<String> redis() {
		Jedis jedis = jedisPool.getResource();

		int count = 10;
		List<String> reList = jedis.lrange(TEST_KEY, -count, -1);
		int reLength = reList.size();
		jedis.ltrim(TEST_KEY, 0, -(reLength + 1));

		jedisPool.returnResource(jedis);
		return reList;
	}

	@RequestMapping(value = "/jdbc")
	@ResponseBody
	public Integer jdbc() {
		int count = jdbcTemplate.queryForInt("select count(*) from bs_bus");
		return count;
	}

	@RequestMapping(value = "run")
	@ResponseBody
	public String run() {
		storeRunnable.run();
		return "success";
	}
	
	@RequestMapping(value = "log")
	@ResponseBody
	public String log() {
		logger.info("info   log");
		logger.debug("debug   log");
		logger.warn("warn  log");
		logger.error("error log");
		return "success";
	}

}
