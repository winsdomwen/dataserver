package com.gci.aptsserver.redis;

import javax.annotation.Resource;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolFactory {
	
	@Resource
	protected JedisPoolConfig jedisPoolConfig;
	
	
	

}
