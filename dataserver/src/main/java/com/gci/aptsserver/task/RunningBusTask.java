package com.gci.aptsserver.task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.IDatabase;
import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.parse.RedisQueue;


@Component("runningBusTask")
public class RunningBusTask extends BaseTask{ 
	
	private static final Logger logger = LoggerFactory.getLogger(RunningBusTask.class);	
	
	private static List<String> busIdList = null;//保存bus_id的列表
	
	private static Date syncDate = new Date();
	
	/*
	 * 初始化公交id
	 */
	private static void  initBusInfo(JdbcTemplate jdbcTemplate){
		
		busIdList = new ArrayList<String>();						
		try {			
			if(jdbcTemplate != null){	
				List<Map<String, Object>> list = jdbcTemplate.queryForList("select bus_id from bs_bus");
				
		        for(int i=0; i< list.size(); i++){        	
		        	Map<String, Object> bus = list.get(i);           	
		        	String busId = String.valueOf(bus.get("bus_id"));
		        	busIdList.add(busId);      	
		        }
			}
        
		 } catch (Exception e) {
	        	e.printStackTrace();
				System.out.println("error");
				logger.error(e.getLocalizedMessage()); 
		 }               
	}	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("RunningBusService");
		
		Date now = new Date();
		long minute =  (now.getTime()-syncDate.getTime())/(1000 * 60);//计算时时间差，分钟
		
		if(busIdList == null || minute >= 30){//如果busId列表为空或者超过半个小时没有更新
			initBusInfo(jdbcTemplate);
			syncDate = now;
		}
		
		long startTime = System.currentTimeMillis();   //获取开始时间	
		this.save(new ArrayList<String>());	
		long endTime = System.currentTimeMillis(); //获取结束时间  
		System.out.println("RunningBus程序运行时间： "+(endTime-startTime)+"ms"); 		
	}
	
	public void save(List<String> redisRecords) {

		Date now = new Date();		
		//开启多线程
		List<Thread> threadList = new ArrayList<Thread>();
		int threadNum = 4;

        try {
        	
            for(int i = 0;i < threadNum;i++)
            {
                 Thread thread = new  RunningBusThread("RunningBusThread"+i,threadNum,i,now);
                 thread.start();
                 threadList.add(thread);
            }

            for(Iterator it = threadList.iterator();it.hasNext();)
            {
                ((Thread)it.next()).join();//等待所有线程结束
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
				
	}	
	
	/*
	 * 批量处理，性能较好,
	 * from表示是列表的某列开始
	 * end表示是列表的某列结束
	 */
	public boolean batchProcess(Date now,int from,int end){						
       	Jedis jedis = null;
        String sql = null;
        Connection conn = null;
        Statement stmt = null;
        List<String> redisFieldList = null;  
        
        IDatabase db = RedisParseFactory.getDatabaseProxy();
        RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);
        Map<String, DbTable> tables = redisQueue.getRedisTables(); //获得配置文件中队列对应的所有表      
                
        
        try {
            jedis = jedisPool.getResource();           
        	conn = jdbcTemplate.getDataSource().getConnection();
    		conn.setAutoCommit(false);
    		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);    		    		                                   
            
            for(String key : tables.keySet()){
            	DbTable table = tables.get(key);
            	db.setTable(table);
            	
            	Map<String, DbField> fieldMap =  table.getFields();
            	redisFieldList = new ArrayList<String>(); 
            	
            	//把配置文件中的redis字段放入list中
            	for(String filedKey :  fieldMap.keySet()){           		
            		DbField field = fieldMap.get(filedKey);            		
            		redisFieldList.add(field.getRedisField());           		          		
            	}            	
            	
            	for(int i = from; i < end; i++){
            		String busId = busIdList.get(i);           	
                	String redisKey = redisQueueKey + busId;
                	
                	Map<String, String> recordMap = new HashMap<String, String>();                	                	
                	
                	String[] array = redisFieldList.toArray(new String[redisFieldList.size()]);
                	List<String> redisList = jedis.hmget(redisKey,array);
                	
                	for(int j = 0; j< array.length;j++){                		
                		recordMap.put(array[j], redisList.get(j));
                	}                                                    	                	
                	if (table.hasPrimaryKey(recordMap)){//如果数据主键存在
					 	sql = db.sqlUpdate(recordMap);
						if(sql != null)
							stmt.addBatch(sql);
					}	              	            		
            	}           	           	            	            	
            }
            stmt.executeBatch();
            conn.commit();
            stmt.close();
    		conn.close();    		    		
                        
        } catch (Exception e) {
        	e.printStackTrace();
			System.out.println("error");
			logger.error(e.getLocalizedMessage()); 
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
            return false;
        } finally {
            if (jedis != null)
            	jedisPool.returnResource(jedis);
        }        
        return true;
	}
	
	public void process(Date now,int from,int end) {
		
		Jedis jedis = null;
		String sql = null;
		List<String> redisFieldList = null;
		//SimpleDateFormat sdf  = new  SimpleDateFormat("yyyyMMdd HHmmss"); 
		
		IDatabase db = RedisParseFactory.getDatabaseProxy();
		RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);
        Map<String, DbTable> tables = redisQueue.getRedisTables();                              
       
        try{
			jedis = jedisPool.getResource();
			
			 for(String key : tables.keySet()){
	            	DbTable table = tables.get(key);
	            	db.setTable(table);
	            	
	            	Map<String, DbField> fieldMap =  table.getFields();
	            	redisFieldList = new ArrayList<String>();
	            	
	            	//把配置文件中的redis字段放入list中
	            	for(String filedKey :  fieldMap.keySet()){           		
	            		DbField field = fieldMap.get(filedKey);            		
	            		redisFieldList.add(field.getRedisField());           		          		
	            	}            	
	            	
	            	for(int i=from; i<end; i++){
	            		String busId = busIdList.get(i);           	
	                	String redisKey = redisQueueKey + busId;
	                	
	                	Map<String, String> recordMap = new HashMap<String, String>();
	                	
	                	String[] array = redisFieldList.toArray(new String[redisFieldList.size()]);
	                	List<String> redisList = jedis.hmget(redisKey,array);
	                	
	                	for(int j = 0; j< array.length;j++){                		
	                		recordMap.put(array[j], redisList.get(j));
	                	}                  	     
	                	
	                	if (table.hasPrimaryKey(recordMap)){//如果数据主键存在
						 	sql = db.sqlUpdate(recordMap);
							if(sql != null)
								jdbcTemplate.update(sql);
						}		               	            		
	            	}           	           	            	            	
	            }
					
        }catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage()+"\r\n"+"sql == " + sql);
		}finally {
            if (jedis != null)
            	jedisPool.returnResource(jedis);
        }  			
	}
	
	/*
	public void process(int from,int end) {

		Jedis jedis = null;
		String sql = null;
		
		RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);
		Map<String, DbTable> tables = redisQueue.getRedisTables();

		try{
			jedis = jedisPool.getResource();
	
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select bus_id from bs_bus");
	
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> bus = list.get(i);
				String busId = String.valueOf(bus.get("bus_id"));
				String redisKey = redisQueueKey + busId;
				
				try {
					Map<String, String> map = jedis.hgetAll(redisKey);
	
					for (String key : tables.keySet()) {
	
						DbTable table = tables.get(key);
	
						if (table.hasPrimaryKey(map)) {// 如果数据主键存在
							sql = table.sqlUpdate(map);
							if (sql != null)
								jdbcTemplate.update(sql);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getLocalizedMessage());
					logger.error("sql=" + sql);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
            if (jedis != null)
            	jedisPool.returnResource(jedis);
        }    

	}*/
	

	@Override
	public boolean batchSaveRecords(List<String> redisRecords) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private class RunningBusThread extends Thread{
		
		private String name;	
		private int total; //线程的个数
		private int index; //第几个线程
		private Date now; //当前时间
		
		public RunningBusThread(String name,int total,int index,Date now) {
		      this.name = name;
		      this.total = total;
		      this.index = index;
		      this.now = now;
		}
		
		public void save(int from,int end){
			
			if(batchProcess(now,from,end) == false){
				System.out.println("RunningBus batch failed");
				process(now,from,end);	
			}
		}
		
		public void run(){
			
			int count = (int)busIdList.size()/total;
			
			int from = index*count;
			int end = (index+1)*count;
			
			if((index+1) == total)
				end = busIdList.size();		
			
			//System.out.println(this.name+","+from+","+end+","+busIdList.size());
			this.save(from,end);			
																			
		}		
	}
}
