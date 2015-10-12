package com.gci.aptsserver.task;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 运行的任务
* @ClassName: TaskBean 
* @Description: TODO
* @author Kuhn
* @date Dec 26, 2012 3:16:34 PM 
*
 */
public class TaskBean {
	
	private ScheduledFuture<?> scheduledFuture;
	//private Long interval;//时间间隔
	private String taskKey;
	private String queueName;
	private String taskPlace;//执行那个城市特定的任务
	private TimeUnit periodTimeUnit;	
	private boolean running;//是否运行
	

	public ScheduledFuture<?> getScheduledFuture() {
		return scheduledFuture;
	}

	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public TimeUnit getPeriodTimeUnit() {
		return periodTimeUnit;
	}

	public void setPeriodTimeUnit(TimeUnit periodTimeUnit) {
		this.periodTimeUnit = periodTimeUnit;
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getTaskPlace() {
		return taskPlace;
	}

	public void setTaskPlace(String taskPlace) {
		this.taskPlace = taskPlace;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
