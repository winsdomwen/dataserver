package com.gci.aptsserver.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;

import com.gci.aptsserver.parse.oracle.OracleDb;
import com.gci.aptsserver.parse.sqlserver.SqlserverDb;
import com.gci.aptsserver.util.XmlUtil;


public class RedisParseFactory {

	//private static final String FILE_PATH = "redisstore";// 配置文件位置
	//private static final String FILE_NAME_START = "redisparse";//配置文件过滤
	private static final String FILE_PATH = "/model";   //映射文件保存地址
	private static final String DB_PATH = "/db/db.properties";   //数据库配置文件保存地址

	public  static RedisParseConfig redisParseConfig; // 配置
	//public  static String dbType; //数据库类型,oracle为Oracle 数据库
	public  static IDatabase db = null;
	
	private RedisParseFactory(){}
	
	static {		
		init();
	}
	
	public static void init(){	
		initDatabase();
		syncWithProperties();
	}
		
	/**
	 * 从数据库配置文件判断是哪个数据库
	 */
	public static String  initDatabase(){
		String dbType ="";
		Properties prop = new Properties();
		try {
			prop.load(RedisParseFactory.class.getResourceAsStream(DB_PATH));
			String driver = prop.getProperty("datasource.driverClassName");
			if("oracle.jdbc.driver.OracleDriver".equals(driver)){
				dbType = "oracle";
				db = new OracleDb();
			}else if("net.sourceforge.jtds.jdbc.Driver".equals(driver)){
				dbType = "sqlserver";
				db = new SqlserverDb();
			}else{
				dbType = "unknown";
			}			
		} catch (IOException e) {
			e.printStackTrace();
			dbType = "unknown";
		}
		System.out.println("database is "+dbType);
		return dbType;
	}
	

	/**
	 * 从配置文件读取配置
	 * 
	 * @return
	 * @throws URISyntaxException 
	 */
	public static RedisParseConfig syncWithProperties() {
		RedisParseConfig newConfig = new RedisParseConfig();
		newConfig.setLastUpdated(new Date());
		//ObjectMapper mapper = new ObjectMapper();
				
		RedisParseConfig properties = null;
		/*
		try {
			properties = mapper.readValue(RedisParseFactory.class.getResourceAsStream(FILE_CONF), RedisParseConfig.class);
			redisParseConfig=properties;
			return redisParseConfig;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			
		try {			
			
			File filePath = new File(RedisParseFactory.class.getResource(FILE_PATH).toURI());
			
			if (filePath.exists() && filePath.isDirectory()) {// 输入的必须是目录
				
				String[] fileNames = filePath.list();
				
				System.out.println("model file length == "+fileNames.length);				
				
				for (String fileName : fileNames) {
					//System.out.println(fileName);															
					
					InputStream stream = RedisParseFactory.class.getResourceAsStream(FILE_PATH+"/"+fileName);
													
					properties = (RedisParseConfig) XmlUtil.simplexml2object(stream, new RedisParseConfig());
					newConfig.merge(properties);
					
				}											
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	
		redisParseConfig = newConfig;
		return redisParseConfig;

	}	
	
	/*
	 * 添加解析文件
	 */
	public static void addXmlConfigFile(String fileName) throws FileNotFoundException {
		
		InputStream stream = RedisParseFactory.class.getResourceAsStream(FILE_PATH +"/"+ fileName);		
		
		RedisParseConfig properties = (RedisParseConfig) XmlUtil.simplexml2object(stream, new RedisParseConfig());
		redisParseConfig.merge(properties);
		
	}
	

	/**
	 * 获取数据的解析方案
	 * 
	 * @param queue
	 * @param type
	 * @return
	 */
	/*
	public static RedisType getRedisType(String queue, String type) {
		if (redisParseConfig.getRedisQueues().containsKey(queue)) {
			if (redisParseConfig.getRedisQueues().get(queue).getRedisTypes().containsKey(type)) {
				return redisParseConfig.getRedisQueues().get(queue).getRedisTypes().get(type);
			}
		}
		return null;
	}*/

	/**
	 * 根据redis队列名获取队列的解析方案
	 * 
	 * @param redisQueueKey
	 * @return
	 */
	public static RedisQueue getRedisQueue(String redisQueueKey) {
		if (redisParseConfig.getRedisQueues().containsKey(redisQueueKey)) {
			return redisParseConfig.getRedisQueues().get(redisQueueKey);
		}
		return null;
	}
	public static IDatabase getDatabaseProxy(){
		return db;
	}
}
