package com.gci.aptsserver.parse.example;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.PrimaryKey;
import com.gci.aptsserver.parse.RedisParseConfig;
import com.gci.aptsserver.parse.RedisQueue;
import com.gci.aptsserver.util.XmlUtil;
import au.com.bytecode.opencsv.CSVReader;


public class ParseCsv {
	
	public static String fileName = "src/main/resources/csv/dy_gps.csv";
	public static String filePath = "src/main/resources/model";
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		FileReader fr = null;
		CSVReader reader = null;
		

		fr = new FileReader(fileName);
		reader = new CSVReader(fr);	
		
		parse(reader);
		
		if (reader != null) {
	        reader.close();
	    }
	      
		if (fr != null) {
	        fr.close();
	     }

	}
	
	public static void parse(CSVReader reader) throws IOException{
		
		String[] nextLine;
		
		//读取队列名
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("queue_name")){
			System.out.println("第一项为队列名");
			return ;
		}
		RedisQueue redisQueue = new RedisQueue();
		redisQueue.setRedisQueueKey(nextLine[1]);
		
		
		//读取表名
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("table_name")){
			System.out.println("第二项为表名");
			return ;
		}
		
		String tableName = nextLine[1];
		
		DbTable table = new DbTable();
		table.setTableName(tableName);
		
		//读取主键
		nextLine = reader.readNext();		
		if(!nextLine[0].equals("primary_key")){
			System.out.println("第三项为主键名");
			return ;
		}
		
		String str = nextLine[1];		
		String[] array =  str.split(",");		
		
		//设置主键
		List<String> keyList = new ArrayList<String>();
		
		for(int i = 0 ; i< array.length; i++){
			keyList.add(array[i]);	
		}		
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setGenerator(nextLine[2]);//设置主键方式,assigned表示普通主键,其它表示sequence
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
			redisQueue.setProcessClass(nextLine[1]);
		}				
		
		int index = 0;
		
		//字段设置
		while ( (nextLine = reader.readNext()) != null) {
			
			if(!nextLine[0].equals("column")){
				System.out.println("字段设置有误");
				return ;
			}
			
			index = index +1;
			
			DbField field = new DbField();
			field.setIndex(index);
			field.setDbField(nextLine[1]);
			field.setRedisField(nextLine[2]);
			field.setDbType(nextLine[3]);
			if("date".equals(nextLine[3])){
				if(nextLine[4].equals("")){
					System.out.println("日期格式有误");
					return;
				}
				field.setFormatter(nextLine[4]);
			}
			table.getFields().put(field.getDbField(), field);			
		
		}
				
		redisQueue.getRedisTables().put(table.getTableName(), table);
		
		RedisParseConfig conf = new RedisParseConfig();
		Map<String, RedisQueue> map = conf.getRedisQueues();
		map.put(redisQueue.getRedisQueueKey(), redisQueue);
		
		
		String xml = XmlUtil.simpleobject2xml(conf);
		System.out.println(xml);
		
		String name  = filePath +"/"+tableName+".xml" ;
		
		File file2 = new File(name);
		DataOutputStream output;
		try {
			output = new DataOutputStream(new FileOutputStream(file2));
	        output.write(xml.getBytes());
	        output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		
	}
		

}
