package com.gci.rapid;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PushTest {

	private String key = "kuhn.test.key";
	

	@Test
	public void testPush() {
		Jedis jedis = RedisUtil.getResource();
		Pipeline pipeline = jedis.pipelined();

		pipeline.lpush(key, "");
		
		RedisUtil.returnResource(jedis);
	}

}
