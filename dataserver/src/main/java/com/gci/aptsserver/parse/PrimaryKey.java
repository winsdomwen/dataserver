package com.gci.aptsserver.parse;

import java.util.List;


/*
 * 数据库的主键
 */
public class PrimaryKey {
	
	public String generator; //assigned表示默认字段，sequence表示sequence	
	public List<String>  key;
		

	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
	public List<String> getKey() {
		return key;
	}
	public void setKey(List<String> key) {
		this.key = key;
	}
	
	
}
