package com.gci.aptsserver.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.parse.RedisQueue;


@Controller
@RequestMapping(value = "/redis")
public class RedisController {
	
	@Resource
	protected JedisPool jedisPool;
	
	private  Map<String,String> queueMap = new HashMap<String,String>();
		
	private  void init(){
		
		Map<String, RedisQueue> redisQueueMap = RedisParseFactory.redisParseConfig.getRedisQueues();
		for(String value :redisQueueMap.keySet()){
			if(value.charAt(value.length()-1) != '='){
				
				String key = value.replace(".","");							
				queueMap.put(key, value);
			}
		}		
	}
	
	/**
	 * 管理界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index() {
						
		init();
		
		ModelAndView mv = new ModelAndView("task/redis");
		mv.addObject("queueMap", queueMap);
		return mv;
	}
	
	
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String,Long> list() {
		
		init();		
		
		Map<String,Long> map = new HashMap<String,Long>();
		
		Jedis jedis = jedisPool.getResource();
		
		for(String key :queueMap.keySet()){
			
			String queue = queueMap.get(key);
			
			Long length = jedis.llen(queue);			
			map.put(key, length);
		}
		
		jedisPool.returnResource(jedis);		
		return map;
	}
	
	

}
