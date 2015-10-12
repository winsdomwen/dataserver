package com.gci.aptsserver.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.PrimaryKey;
import com.gci.aptsserver.parse.RedisParseConfig;
import com.gci.aptsserver.parse.RedisQueue;

import au.com.bytecode.opencsv.CSVReader;


/*
 * 解析src/main/resources/csv/目录下的csv文件，生成xml文件放入src/main/resources/model中
 */

public class ParseCsvUtil {
	
	//public static String fileName = "src/main/resources/csv/dy_gps.csv";
	//public static String filePath = "src/main/resources/model";
	
	public static void parseCsv(String path,String fileName) throws IOException {
		
		// TODO Auto-generated method stub
		FileReader fr = null;
		CSVReader reader = null;
		
		//String filePath = path +""

		try{
			fr = new FileReader(path +"/csv/"+fileName);
			reader = new CSVReader(fr);	
			
			parse(reader,path);
			
			if (reader != null) {
		        reader.close();
		    }
		      
			if (fr != null) {
		        fr.close();
		     }			
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void parse2(CSVReader reader,String path) throws IOException{
		
		String[] nextLine;
		
		//读取队列名
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("queue_name")){
			System.out.println("第一项为队列名");
			return ;
		}
		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey(nextLine[1].trim());
		
		
		//读取表名
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("table_name")){
			System.out.println("第二项为表名");
			return ;
		}
		
		String tableName = nextLine[1].trim();
		
		DbTable table = new DbTable();
		table.setTableName(tableName);
		
		//读取主键
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("primary_key")){
			System.out.println("第三项为主键名");
			return ;
		}
		
		String str = nextLine[1].trim();		
		String[] array =  str.split(",");		
		
		//设置主键
		List<String> keyList = new ArrayList<String>();
		
		for(int i = 0 ; i< array.length; i++){
			keyList.add(array[i].trim());	
		}		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator(nextLine[2].trim());//设置主键方式,assigned表示普通主键,其它表示sequence
		primaryKey.setKey(keyList);	
		table.setPrimaryKey(primaryKey);
		
		
		//设置数据保存方式
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("save_method")){
			System.out.println("第四项为数据保存方式");
			return ;
		}		
		if((!nextLine[1].equals("update"))&&(!nextLine[1].equals("insert"))){
			System.out.println("第四项有误");
			return ;
		}		
		
		if(nextLine[1].equals("update")){
			table.setUpdate(true);
		}else{
			table.setUpdate(false);
		}	
		
		
		//设置处理的类
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("process_class")){
			System.out.println("第五项为数据保存方式");
			return ;
		}	
		
		if("".endsWith(nextLine[1])){//如果为空,使用默认处理类
			redisQueue.setProcessClass("storeTask");			
		}else{
			redisQueue.setProcessClass(nextLine[1].trim());
		}		
		
		//设置介绍
		nextLine = reader.readNext();	
		if(!nextLine[0].equals("introduction")){
			System.out.println("第六项为队列介绍");
			return ;
		}
		table.setIntroduction(nextLine[1].trim());
		
		int index = 0;
		
		//字段设置
		while ((nextLine = reader.readNext()) != null) {
			
			if(!nextLine[0].equals("column")){
				System.out.println("字段设置有误");
				return ;
			}
			
			index = index +1;
			
			DbField field = new DbField();
			field.setIndex(index);
			field.setDbField(nextLine[1].trim());
			field.setRedisField(nextLine[2].trim());
			field.setDbType(nextLine[3].trim());
			if("date".equals(nextLine[3].trim())){
				if(nextLine[4].equals("")){
					System.out.println("日期格式有误");
					return;
				}
				field.setFormatter(nextLine[4].trim());
			}
			table.getFields().put(field.getDbField(), field);	
								
		}
				
		redisQueue.getRedisTables().put(table.getTableName(), table);
		
		RedisParseConfig conf = new RedisParseConfig();
		Map<String, RedisQueue> map = conf.getRedisQueues();
		map.put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		//System.out.println(xml);
		
		String name  = path +"/model/"+tableName+".xml" ;

		//File file2 = new File(name);

		DataOutputStream output;
		try {
			output = new DataOutputStream(new FileOutputStream(new File(name)));
	        output.write(xml.getBytes());
	        output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}		
	}	
	
	/**
	 * 解析上传的csv文件，并转化为xml配置文件
	 * @param reader
	 * @param path
	 * @throws IOException
	 */
	public static void parse(CSVReader reader,String path) throws IOException{
		
		String[] nextLine = null;
		DbField field = null;
		Map<String, String> typeMap = new HashMap<String, String>();
		List<DbField> fieldList = new ArrayList<DbField>();
		
		while ((nextLine = reader.readNext()) != null) {
			
			if(nextLine[0].equals("queue_name")){
				typeMap.put("queue_name", nextLine[1].trim());
			}
			else if(nextLine[0].equals("table_name")){
				typeMap.put("table_name", nextLine[1].trim());
			}
			else if(nextLine[0].equals("primary_key")){
				typeMap.put("primary_key", nextLine[1].trim()+"-"+nextLine[2].trim());
			}
			else if(nextLine[0].equals("save_method")){
				typeMap.put("save_method", nextLine[1].trim());
			}
			else if(nextLine[0].equals("process_class")){
				typeMap.put("process_class", nextLine[1].trim());
			}
			else if(nextLine[0].equals("introduction")){
				typeMap.put("introduction", nextLine[1].trim());
			}
			else if(nextLine[0].equals("interval")){
				typeMap.put("interval", nextLine[1].trim());
			}
			else if(nextLine[0].equals("threads")){
				typeMap.put("threads", nextLine[1].trim());
			}
			else if(nextLine[0].equals("column")){
				field = new DbField();
				field.setDbField(nextLine[1].trim());
				field.setRedisField(nextLine[2].trim());
				field.setDbType(nextLine[3].trim());
				if("date".equals(nextLine[3].trim())){
					if(!nextLine[4].equals("")){
						field.setFormatter(nextLine[4].trim());
					}else{
						return;
					}
				}	
				fieldList.add(field);
			}														
		}		
		
		//设置队列名
		String queueName = typeMap.get("queue_name");
		RedisQueue redisQueue = new RedisQueue();
		
		if(queueName == null || "".equals(queueName)){
			return;
		}else{
			redisQueue.setRedisQueueKey(queueName);
		}
		
		//设置表名
		String tableName = typeMap.get("table_name");
		DbTable table = new DbTable();
		if(tableName == null || "".equals(tableName)){
			return;
		}else{
			
			table.setTableName(tableName);
		}
		
		//设置主键名
		String primaryKey = typeMap.get("primary_key");	
		PrimaryKey key = new PrimaryKey();
		if(primaryKey != null && !"".equals(primaryKey)){
			
			String[] cloumn =  primaryKey.split("-");			
			String str = cloumn[0].trim();		
			String[] array =  str.split(",");		
			
			//设置主键
			List<String> keyList = new ArrayList<String>();			
			for(int i = 0 ; i< array.length; i++){
				keyList.add(array[i].trim());	
			}		
			
			key.setGenerator(cloumn[1].trim());//设置主键方式,assigned表示普通主键,其它表示sequence
			key.setKey(keyList);	
			table.setPrimaryKey(key);
		}
				
		//设置保存方式
		String saveMethod = typeMap.get("save_method");	
		if(saveMethod == null || "".equals(saveMethod)){
			return;
		}else{			
			if((!saveMethod.equals("update"))&&(!saveMethod.equals("insert"))){				
				return ;
			}					
			if(saveMethod.equals("update")){
				table.setUpdate(true);
			}else{
				table.setUpdate(false);
			}				
		}
		
		//设置处理的类
		String processClass = typeMap.get("process_class");		
		if(processClass == null || "".equals(processClass)){			
			redisQueue.setProcessClass("storeTask");
		}else{		
			redisQueue.setProcessClass(processClass);
		}
		
		//设置队列介绍
		String introduction = typeMap.get("introduction");	
		if(introduction == null || "".equals(introduction)){			
			table.setIntroduction("");
		}else{		
			 table.setIntroduction(introduction);
		}	
		
		//设置处理间隔时间
		String interval = typeMap.get("interval");			
		if(interval == null || "".equals(interval)){			
			redisQueue.setInterval(10); //如果间隔时间为空，默认为10秒
		}else{		
			redisQueue.setInterval(Integer.parseInt(interval));
		}	
		
		//设置线程数量
		String threads = typeMap.get("threads");
		if(threads == null || "".equals(threads)){			
			redisQueue.setThreads(1); //如果线程数为空，默认为1
		}else{		
			redisQueue.setThreads(Integer.parseInt(threads));
		}			
		
		//设置字段
		for(int i=0;i<fieldList.size();i++){
					
			field = fieldList.get(i);
			field.setIndex(i+1);
			table.getFields().put(field.getDbField(), field);	
		}		
		
		redisQueue.getRedisTables().put(table.getTableName(), table);
		
		RedisParseConfig conf = new RedisParseConfig();
		Map<String, RedisQueue> map = conf.getRedisQueues();
		map.put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		//System.out.println(xml);
		
		String name  = path +"/model/"+tableName+".xml" ;

		//File file2 = new File(name);

		DataOutputStream output;
		try {
			output = new DataOutputStream(new FileOutputStream(new File(name)));
	        output.write(xml.getBytes());
	        output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}				
	
	}

}
