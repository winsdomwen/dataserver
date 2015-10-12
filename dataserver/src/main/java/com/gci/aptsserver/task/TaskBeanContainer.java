package com.gci.aptsserver.task;

import java.util.ArrayList;
import java.util.List;

/*
 * 保存taskBean的容器
 */
public class TaskBeanContainer {
	private String queueName;//队列名
	private String introduction;//队列介绍
	private Long interval; //线程运行间隔
	List<TaskBean> taskList; //保存多线程的任务列表
	
	public TaskBeanContainer(){
		taskList = new ArrayList<TaskBean>();
	}

	public void addTask(TaskBean bean){
		taskList.add(bean);
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public List<TaskBean> getTaskList() {
		return taskList;
	}
	
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public void clear(){
		taskList.clear();
	}
	
	public int size(){
		return taskList.size();
	}
	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	

}
