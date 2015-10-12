package com.gci.aptsserver.task;


import java.util.List;

/*
 * 保存入库队列dpdb.queue.notify.db的redis数据的链表
 */
public class RedisListBean {
	private String typeKey;	//redis的type字段
	private List<String> tableList; //数据库对应的表

	
	
	public RedisListBean(String typeKey,List<String> tableList){
		this.typeKey = typeKey;
		this.tableList = tableList;
	}
	
	
	public List<String> getTableList() {
		return tableList;
	}
	
	public String getTypeKey() {   
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}


}
