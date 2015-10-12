package com.gci.rapid;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
* @ClassName: RedisUtil 
* @Description: TODO
* @author Kuhn
* @date Dec 29, 2012 4:40:53 PM 
*
 */
public class RedisUtil {

	static private final String host ="10.88.1.123"; //"192.168.223.249";//
	static private final int port = 6379;

	static private JedisPoolConfig config;
	static private JedisPool pool;

	static {
		config = new JedisPoolConfig();
		config.setMaxActive(80);
		config.setMaxIdle(10);
		config.setMaxWait(20);
		config.setTestOnBorrow(true);
		pool = new JedisPool(config, host, port);
	}

	static public Jedis getResource() {
		Jedis jedis = pool.getResource();
		return jedis;
	}

	static public void returnResource(Jedis jedis) {
		pool.returnResource(jedis);
	}
}
