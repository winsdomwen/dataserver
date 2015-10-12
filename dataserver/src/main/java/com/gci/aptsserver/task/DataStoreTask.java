package com.gci.aptsserver.task;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.IDatabase;
import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.parse.RedisQueue;

/**
 * 保存入库队列数据
 * 
 * @ClassName: StoreRunnable
 * @Description: TODO
 * @author Kuhn
 * @date Dec 28, 2012 9:19:03 AM
 * 
 */
@Component("dataStoreTask")
public class DataStoreTask extends BaseTask {

	private static final Logger logger = LoggerFactory.getLogger(DataStoreTask.class);
		
	private static  Map<String,RedisListBean> redisMap = new HashMap<String,RedisListBean>();
	
	static {		
		initQueue();
	}
	
	@Override
	public void run() {

		//System.out.println("store runnable is running ... " + redisQueueKey);
		
		// 读取数据		
		List<String> redisRecords = getRedisData(redisQueueKey);// 读取数据
		if(redisRecords == null)
			return;
		//System.out.println("DataStoreTask redisRecords = "+redisRecords.size());
		//long startTime = System.currentTimeMillis();   //获取开始时间		
		
		save(redisRecords);
				
		//long endTime = System.currentTimeMillis(); //获取结束时间  
		//System.out.println("DataStoreTask程序运行时间： "+(endTime-startTime)+"ms");		

	}
	
	//初始化rediskey List表,根据typeKey解析队列,分别存入不同的表中
	private static   void  initQueue(){
		
		List<String> tableList = new ArrayList<String>();
		tableList.add("dy_trip_log");
		
		RedisListBean bean = new RedisListBean("trip_id",tableList);//路单
		redisMap.put(bean.getTypeKey(), bean);
		
		tableList = new ArrayList<String>();
		tableList.add("dy_real_ip");
		
		bean = new RedisListBean("01_ip",tableList);
		redisMap.put(bean.getTypeKey(), bean);
		
		tableList = new ArrayList<String>();
		tableList.add("dy_door");
		
		bean = new RedisListBean("10",tableList);  //开关门数据
		redisMap.put(bean.getTypeKey(), bean);	
		
		
		tableList = new ArrayList<String>();
		tableList.add("dy_bus_online_hist");
		
		bean = new RedisListBean("01",tableList);
		redisMap.put(bean.getTypeKey(), bean);	
		
		
		tableList = new ArrayList<String>();
		tableList.add("dy_obu_version");
		tableList.add("dy_obu_version_hist");
		
		bean = new RedisListBean("0a",tableList);
		redisMap.put(bean.getTypeKey(), bean);			
					
	}	
	
	public boolean batchSaveRecords(List<String> redisRecords) {
		
		IDatabase db = RedisParseFactory.getDatabaseProxy();
		RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);		
		Map<String, DbTable> tables = redisQueue.getRedisTables();
		String sql = null;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

			ObjectMapper mapper = new ObjectMapper();		

			for (String record : redisRecords) {
				
				@SuppressWarnings("rawtypes")
				Map parsedRecord = mapper.readValue(record,Map.class);
				
				String typeKey = (String)parsedRecord.get("type");				
				
				RedisListBean bean = redisMap.get(typeKey);
				
				List<String> tableList = bean.getTableList();
				
				@SuppressWarnings("unchecked")
				Map<String, String> value = (Map<String, String>)parsedRecord.get("value");
				
				for (String tableName : tableList) {
					
					DbTable table = tables.get(tableName);	
					if(table == null ) continue;
					
					db.setTable(table);
					if (table.isUpdate()) {// merge更新数据
						if (table.hasPrimaryKey(value)) {// 如果数据主键存在

							String query = db.sqlQuery(value);
							int id = jdbcTemplate.queryForInt(query);

							if (id > 0) {// 存在记录，执行更新
								sql = db.sqlUpdate(value);
								if (sql != null)
									stmt.addBatch(sql);
							} else {
								sql = db.sqlInsert(value);
								stmt.addBatch(sql);
							}
						}
					} else {
						sql = db.sqlInsert(value);
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
					conn.rollback();
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			return false;
		}
		return true;				
	}	
	
	public void  saveRecords(List<String> redisRecords){
		
		IDatabase db = RedisParseFactory.getDatabaseProxy();
		RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);		
		Map<String, DbTable> tables = redisQueue.getRedisTables();
		String sql = null;
		       
		// 逐条处理数据
		for (String record : redisRecords) {
			try {	
				
				ObjectMapper mapper = new ObjectMapper();
				
				@SuppressWarnings("rawtypes")
				Map parsedRecord = mapper.readValue(record, Map.class);	
				//{"type":"0a","operation":"update","value":{"obuid":"430535","lastest_update_time":"20121227 104024"}}				
				
				String typeKey = (String)parsedRecord.get("type");				
				
				RedisListBean bean = redisMap.get(typeKey);
				
				List<String> tableList = bean.getTableList();	
				
				@SuppressWarnings("unchecked")
				Map<String, String> value = (Map<String, String>)parsedRecord.get("value");
				
				for (String tableName : tableList) {
					
					DbTable table = tables.get(tableName);
					if(table == null ) continue;	
					db.setTable(table);
					if (table.isUpdate()) {// merge更新数据
						if (table.hasPrimaryKey(value)) {// 如果数据主键存在

							String query = db.sqlQuery(value);
							int id = jdbcTemplate.queryForInt(query);

							if (id > 0) {// 存在记录，执行更新
								sql = db.sqlUpdate(value);
								if (sql != null)
									jdbcTemplate.execute(sql);
							} else {
								sql = db.sqlInsert(value);
								jdbcTemplate.execute(sql);
							}
						}
					} else {
						sql = db.sqlInsert(value);
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

}