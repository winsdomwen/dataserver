package com.gci.aptsserver.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.parse.RedisQueue;
import com.gci.aptsserver.util.SpringUtil;

public class TaskManager {


	private static  Logger logger = LoggerFactory.getLogger(TaskManager.class);
	
	public static  Map<String, TaskBeanContainer> scheduleMap = new HashMap<String, TaskBeanContainer>();// 当前运行的线程	
	
	public static  Map<String, String> queueMap = new HashMap<String, String>();
	
	private static  ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);// 线程池
	
	private TaskManager(){
		
	}
	
	static{
		init();
	}
	
	/**
	 * 启动任务
	 * 
	 * @param taskType
	 * @param delay
	 * @param period
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 例子：http://localhost:8080/apts-server/task/start/runningBusTask
	 */
	public static void startTask(String taskType, long delay, long period) throws InstantiationException, IllegalAccessException {
		
		if(scheduleMap.containsKey(taskType)){
						
			String queueName = queueMap.get(taskType);
			Map<String, RedisQueue> config = RedisParseFactory.redisParseConfig.getRedisQueues();						
			
			RedisQueue queue = config.get(queueName);			
			String processClass = queue.getProcessClass();
			Integer interval = queue.getInterval();
			
			TaskBeanContainer container = scheduleMap.get(taskType);	
			if(container.getTaskList().size() >0)//如果任务正在运行中,则无需启动
				return;
			
			container.setInterval(Long.valueOf(queue.getInterval()));
			
			BaseTask task = (BaseTask) SpringUtil.getBean(processClass);
			task.setRedisQueueKey(queueName);	
						
			Integer threads = queue.getThreads();//获得线程的数量
			
			for(int i=0; i<threads; i++){
			
				ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(task, delay, interval, TimeUnit.SECONDS);			
				
				TaskBean bean = new TaskBean();
				//bean.setInterval(Long.valueOf(interval));
				bean.setPeriodTimeUnit(TimeUnit.SECONDS);
				bean.setTaskKey(taskType);
				bean.setQueueName(queueName);
				bean.setRunning(true);	
				bean.setScheduledFuture(scheduledFuture);
				
				container.addTask(bean);
			}		
			
			logger.info("start task  " + queueName);			
			System.out.println("start task  " + queueName);
			
		}		
	}
	
	/**
	 * 添加一个新的线程
	 */
	public static void addThread(String taskType, long delay, long period) throws InstantiationException, IllegalAccessException {
		
		if(scheduleMap.containsKey(taskType)){
			
			String queueName = queueMap.get(taskType);
			Map<String, RedisQueue> config = RedisParseFactory.redisParseConfig.getRedisQueues();						
			
			RedisQueue queue = config.get(queueName);			
			String processClass = queue.getProcessClass();
			Integer interval = queue.getInterval();
			
			TaskBeanContainer container = scheduleMap.get(taskType);	
			
			container.setInterval(Long.valueOf(queue.getInterval()));
			
			BaseTask task = (BaseTask) SpringUtil.getBean(processClass);
			task.setRedisQueueKey(queueName);							
			
			ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(task, delay, interval, TimeUnit.SECONDS);			
			
			TaskBean bean = new TaskBean();
			//bean.setInterval(Long.valueOf(interval));
			bean.setPeriodTimeUnit(TimeUnit.SECONDS);
			bean.setTaskKey(taskType);
			bean.setQueueName(queueName);
			bean.setRunning(true);	
			bean.setScheduledFuture(scheduledFuture);
			
			container.addTask(bean);	
			
			logger.info("add new task thread  " + queueName);			
			System.out.println("add new task thread   " + queueName);
			
		}			
	}

	/**
	 * 取消任务
	 * 
	 * @param taskType
	 */
	public static void stopTask(String taskType) {
		/*
		if (scheduleMap.containsKey(taskType)) {
			TaskBean task = scheduleMap.get(taskType);
			task.getScheduledFuture().cancel(true);
			task.setRunning(false);
		}*/
		
		if (scheduleMap.containsKey(taskType)) {
			
			TaskBeanContainer container = scheduleMap.get(taskType);
			
			for (TaskBean bean : container.getTaskList()) {
				bean.getScheduledFuture().cancel(false);
			}
			container.clear();
			
			/*
			for (TaskBean rb : scheduleMap.get(taskType)) {
				rb.getScheduledFuture().cancel(true);
			}
			scheduleMap.get(taskType).clear();
			*/
		}

	}
	
	/*
	 *启动所有服务 
	 */	
	public static void startAllTask(long delay) {
		
		for(String taskType:scheduleMap.keySet()){
			
			String queueName = queueMap.get(taskType);
			Map<String, RedisQueue> config = RedisParseFactory.redisParseConfig.getRedisQueues();						
			
			RedisQueue queue = config.get(queueName);			
			String processClass = queue.getProcessClass();
			Integer interval = queue.getInterval();
			
			TaskBeanContainer container = scheduleMap.get(taskType);
			
			if(container.getTaskList().size() >0)//如果任务正在运行中,则无需启动
				continue;
			
			container.setInterval(Long.valueOf(interval));
			
			BaseTask task = (BaseTask) SpringUtil.getBean(processClass);
			task.setRedisQueueKey(queueName);
			
			Integer threads = queue.getThreads();//获得线程的数量
			
			for(int i=0; i<threads; i++){
			
				ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(task, delay, interval, TimeUnit.SECONDS);			
				
				TaskBean bean = new TaskBean();
				//bean.setInterval(Long.valueOf(interval));
				bean.setPeriodTimeUnit(TimeUnit.SECONDS);
				bean.setTaskKey(taskType);
				bean.setQueueName(queueName);
				bean.setRunning(true);	
				bean.setScheduledFuture(scheduledFuture);
				
				container.addTask(bean);
			}
						
			logger.info("start task  " + queueName);			
			System.out.println("start task  " + queueName);
			
		}				
	}	
	
	/*
	 * 停止所有的任务
	 * 
	 */	
	public static void stopAllTask() {
		for (TaskBeanContainer container : scheduleMap.values()) {
			
			for (TaskBean bean : container.getTaskList()) {
				bean.getScheduledFuture().cancel(false);
			}
			container.clear();			
		}
	}
	
	/*
	 * 停止TaskManager服务
	 * 
	 */	
	public static void stopTaskManager() {
		for (TaskBeanContainer container : scheduleMap.values()) {
			
			for (TaskBean bean : container.getTaskList()) {
				bean.getScheduledFuture().cancel(false);
			}
			container.clear();			
		}
		scheduleMap.clear();
	}	

	/**
	 * 退出时执行
	 */
	public static void destroy() {
		/*
		for (TaskBean task : scheduleMap.values()) {
			task.getScheduledFuture().cancel(true);
			
		}
		scheduleMap.clear();
		executorService.shutdown();
		*/
		for (TaskBeanContainer container : scheduleMap.values()) {
			
			for (TaskBean bean : container.getTaskList()) {
				bean.getScheduledFuture().cancel(true);
			}
			container.clear();			
		}
		scheduleMap.clear();
		executorService.shutdown();
		
	}
	
	public static void init(){		
		Map<String, RedisQueue> config = RedisParseFactory.redisParseConfig.getRedisQueues();		
		for(String queueName:config.keySet()){
			
			String taskType  = queueName.replace(".","").replace("=", "");//去掉不合法的字符
			if (!scheduleMap.containsKey(taskType)) {	
				
				//String taskType  = queueName.replace(".","").replace("=", "");//去掉不合法的字符
				//TaskBean bean =  new TaskBean();				
				//bean.setQueueName(value);
				//bean.setRunning(false);
				
				RedisQueue queue = config.get(queueName);
				
				StringBuilder sb  = new StringBuilder();
				Map<String,DbTable> tables = queue.getRedisTables();
				
				//获取队列的介绍
				for(String tableName :tables.keySet()){
					DbTable table = tables.get(tableName);					
					sb.append(table.getIntroduction()+"<br>");					
				}				
				
				TaskBeanContainer container = new TaskBeanContainer();
				container.setQueueName(queueName);	
				container.setIntroduction(sb.toString());
				container.setInterval(Long.valueOf(queue.getInterval()));
				scheduleMap.put(taskType,container);
				queueMap.put(taskType, queueName);//设置类型和队列名的映射
				//scheduleMap.put(taskType, bean);	
			}
		}
		/*
		for(TaskType taskType:TaskType.values()){		
			String key = taskType.getKey();
			if (!scheduleMap.containsKey(key)) 				
				 scheduleMap.put(key, new ArrayList<TaskBean>());							
		}*/
	}
}
