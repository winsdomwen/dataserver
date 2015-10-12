package com.gci.aptsserver.parse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gci.aptsserver.parse.RedisQueue;


/**
 * redis入库配置
 * 
 * @ClassName: RedisStoreConfig
 * @Description: TODO
 * @author Kuhn
 * @date Dec 28, 2012 3:13:37 PM
 * 
 */
public class RedisParseConfig {

	public Date lastUpdated;

	public Map<String, RedisQueue> redisQueues = new HashMap<String, RedisQueue>();

	/**
	 * 合并配置
	 * 
	 * @param p
	 */
	public void merge(RedisParseConfig properties) {
		for (String queueKey : properties.getRedisQueues().keySet()) {
			if (this.getRedisQueues().containsKey(queueKey)) {// 队列存在
				RedisQueue oldRedisQueue = this.redisQueues.get(queueKey);
				RedisQueue newRedisQueue = properties.getRedisQueues().get(queueKey);

				
				for (String tableKey : newRedisQueue.getRedisTables().keySet()) {
					/*
					if (oldRedisQueue.getRedisTypes().containsKey(typeKey)) {
						DbTable oldDbtable = oldRedisQueue.getRedisTypes().get(typeKey);
						DbTable newDbtable = newRedisQueue.getRedisTypes().get(typeKey);
						
						oldRedisQueue.getRedisQueueKey()
						
						//oldRedisType.setDbTables(CollectionUtil.intersectionList(oldRedisType.getDbTables(), newRedisType.getDbTables()));
						
					} else {
						oldRedisQueue.getRedisTypes().put(typeKey, newRedisQueue.getRedisTypes().get(typeKey));
					}*/
					oldRedisQueue.getRedisTables().put(tableKey, newRedisQueue.getRedisTables().get(tableKey));
				}
				
			} else{
				
				this.getRedisQueues().put(queueKey, properties.getRedisQueues().get(queueKey));
				
			}
		}
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Map<String, RedisQueue> getRedisQueues() {
		return redisQueues;
	}

	public void setRedisQueues(Map<String, RedisQueue> redisQueues) {
		this.redisQueues = redisQueues;
	}

}
