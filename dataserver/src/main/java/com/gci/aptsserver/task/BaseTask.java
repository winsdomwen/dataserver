package com.gci.aptsserver.task;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.IDatabase;
import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.parse.RedisQueue;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseTask implements Runnable{
	
	@Resource
	protected JdbcTemplate jdbcTemplate;
	
	@Resource
	protected JedisPool jedisPool;	
	
	protected  String redisQueueKey;
	
	private static final Logger logger = LoggerFactory.getLogger(BaseTask.class);		
	
	public  void save(List<String> redisRecords)
	{
		
		boolean result = batchSaveRecords(redisRecords);//批量处理数据，性能较好		
		
		if(result == false)
			saveRecords( redisRecords);
		
	}	
	
	/*
	 * 批量处理数据,性能较好
	 */
	//@Transactional
	public abstract boolean batchSaveRecords(List<String> redisRecords);
	
	/*
	{

		String sql = null;
		String record = null;
		Connection conn = null;
		Statement stmt = null;

		// 逐条处理数据
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

			ObjectMapper mapper = new ObjectMapper();
			
			RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);
			Map<String, DbTable> tables = redisQueue.getRedisTables();			

			for (int i = 0; i < redisRecords.size(); i++) {

				record = redisRecords.get(i);

				@SuppressWarnings("unchecked")
				Map<String, String> parsedRecord = mapper.readValue(record,Map.class);

				for (String key : tables.keySet()) {

					DbTable table = tables.get(key);

					if (table.isUpdate()) {// merge更新数据
						if (table.hasPrimaryKey(parsedRecord)) {// 如果数据主键存在

							String query = table.sqlQuery(parsedRecord);
							int id = jdbcTemplate.queryForInt(query);

							if (id > 0) {// 存在记录，执行更新
								sql = table.sqlUpdate(parsedRecord);
								if (sql != null)
									stmt.addBatch(sql);
							} else {
								sql = table.sqlInsert(parsedRecord);
								stmt.addBatch(sql);
							}
						}
					} else {
						sql = table.sqlInsert(parsedRecord);
						if (sql != null)
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

			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			return false;
		}
		return true;

	}*/
	
	/*
	 * 保存数据
	 */
	public void  saveRecords(List<String> redisRecords){
		
		String sql = null;	
		
		ObjectMapper mapper = new ObjectMapper();
		
		for (String record : redisRecords) {
			//System.out.println(record);
			
			try {				
			
				@SuppressWarnings("unchecked")
				Map<String, String> parsedRecord = mapper.readValue(record, Map.class);
				
				IDatabase db = RedisParseFactory.getDatabaseProxy();
				RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);															
						
				Map<String, DbTable> tables = redisQueue.getRedisTables();				
				
				for(String key : tables.keySet()){
					 
					DbTable table = tables.get(key); 								
					db.setTable(table);
					
					if (table.isUpdate()) {// merge更新数据
						if (table.hasPrimaryKey(parsedRecord)) {// 如果数据主键存在

							String query = db.sqlQuery(parsedRecord);
							int id = jdbcTemplate.queryForInt(query);

							if (id > 0) {// 存在记录，执行更新
								sql = db.sqlUpdate(parsedRecord);
								if (sql != null)
									jdbcTemplate.execute(sql);
							} else {
								sql = db.sqlInsert(parsedRecord);
								jdbcTemplate.execute(sql);
							}
						}
					} else {
						sql = db.sqlInsert(parsedRecord);
						if (sql != null)
							jdbcTemplate.execute(sql);
					}							
				}												
				
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("error");
				logger.error(e.getLocalizedMessage()+"\r\n"+"redis_data == " + record+"\r\n"+"sql == " + sql);		
			}			
		} 		
	}		
	
	/**
	 * 从redis中读取数据
	 * 
	 * @return
	 */
	public List<String> getRedisData(String redisQueueKey) {
		
		synchronized(redisQueueKey){		
			try{
				Jedis jedis = jedisPool.getResource();
				int count = 3000;
				List<String> reList = jedis.lrange(redisQueueKey, -count, -1);
				int reLength = reList.size();
				jedis.ltrim(redisQueueKey, 0, -(reLength + 1));
		
				jedisPool.returnResource(jedis);
				return reList;
			}catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
		}		
	}	
	
	public String getRedisQueueKey() {
		return redisQueueKey;
	}

	public void setRedisQueueKey(String redisQueueKey) {
		this.redisQueueKey = redisQueueKey;
	}

}
