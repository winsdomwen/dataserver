package com.gci.aptsserver.parse;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件，redis队列配置
 * 
 * @ClassName: RedisStorePropertiesBean
 * @Description: TODO
 * @author Kuhn
 * @date Dec 28, 2012 2:59:41 PM
 * 
 */
public class RedisQueue {

	private Map<String, DbTable> redisTables = new HashMap<String, DbTable>();// 每种类型的解析方案
	private String redisQueueKey;// 队列名字
	private String processClass; //处理的类
	private Integer interval;  //处理的间隔
	private Integer threads;  //处理的线程数
	
	
	public String getRedisQueueKey() {
		return redisQueueKey;
	}

	public void setRedisQueueKey(String redisQueueKey) {
		this.redisQueueKey = redisQueueKey;
	}

	public Map<String, DbTable> getRedisTables() {
		return redisTables;
	}

	public void setRedisTables(Map<String, DbTable> redisTables) {
		this.redisTables = redisTables;
	}
	
	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}
	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getThreads() {
		return threads;
	}

	public void setThreads(Integer threads) {
		this.threads = threads;
	}
	
}
