package com.gci.aptsserver.task;


import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.IDatabase;
import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.parse.RedisQueue;


@Scope("prototype")
@Component("storeTask")
public class StoreTask extends  BaseTask{
		
	private static final Logger logger = LoggerFactory.getLogger(StoreTask.class);

	@Override
	public void run() {
		
		// TODO Auto-generated method stub				
		List<String> redisRecords = getRedisData(redisQueueKey);			
		if(redisRecords == null)
			return;			
		save(redisRecords);				
		
	}

	@Override
	public boolean batchSaveRecords(List<String> redisRecords) {
		// TODO Auto-generated method stub
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
			
			IDatabase db = RedisParseFactory.getDatabaseProxy();
			RedisQueue redisQueue = RedisParseFactory.getRedisQueue(redisQueueKey);
			Map<String, DbTable> tables = redisQueue.getRedisTables();			

			for (int i = 0; i < redisRecords.size(); i++) {

				record = redisRecords.get(i);

				@SuppressWarnings("unchecked")
				Map<String, String> parsedRecord = mapper.readValue(record,Map.class);

				for (String key : tables.keySet()) {

					DbTable table = tables.get(key);
					db.setTable(table);

					if (table.isUpdate()) {// merge更新数据
						if (table.hasPrimaryKey(parsedRecord)) {// 如果数据主键存在

							String query = db.sqlQuery(parsedRecord);
							int id = jdbcTemplate.queryForInt(query);

							if (id > 0) {// 存在记录，执行更新
								sql = db.sqlUpdate(parsedRecord);
								if (sql != null)
									stmt.addBatch(sql);
							} else {
								sql = db.sqlInsert(parsedRecord);
								stmt.addBatch(sql);
							}
						}
					} else {
						sql = db.sqlInsert(parsedRecord);
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
	

}
