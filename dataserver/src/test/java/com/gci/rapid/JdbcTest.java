package com.gci.rapid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class JdbcTest {

	//@Resource
	private JdbcTemplate jdbcTemplate;		

	private int INSERT_COUNT = 10000;
	private String sqlClause = " insert into dy_obu_version_hist_test (cf_sn,total_mem,sim_imsi,video_sn,gprs_imei,wlan_mac,obuid,motherboard_sn) values('TSS26071070125002837','1','460006103214439','','352012006157951','001B111B4660','932094','00900B0D9CDA')";

	
	
	//@Test
	public void testDbUtils() throws SQLException, ParseException, ClassNotFoundException{
		//Connection conn = jdbcTemplate.getDataSource().getConnection();
		//conn.setAutoCommit(false);
		
		Connection conn = null; 
		  Class.forName("oracle.jdbc.driver.OracleDriver");//加载数据库驱动
		  conn = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.222.53)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.222.54)(PORT = 1521))(LOAD_BALANCE = yes))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = NBUSDB)))","apts", "apts");// 与数据库建立连接
		  conn.setAutoCommit(false);
		
		
		QueryRunner qRunner = new QueryRunner();;
		
		String sql = "insert into dy_obu_version_hist_test (id,cf_sn,total_mem,sim_imsi,video_sn,gprs_imei,wlan_mac,obuid,motherboard_sn,obu_time) values(?,?,?,?,?,?,?,?,?,?)";		
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss"); 
		Date date = sdf.parse("20130218 150830");
		
		
		Object[] values = new Object[10];
		
		
		for (int i = 0; i < INSERT_COUNT; i++) {
			sdf = new SimpleDateFormat("yyyyMMdd HHmmss"); 
		    date = sdf.parse("20130218 150830");
		    
		    values[0]= i+1;
		    values[1]= "TSS26071070125002837";
		    values[2]= "1";
		    values[3]= "5";
		    values[4]= "352012006157951";
		    values[5]= "001B111B4660";
		    values[6]= "932094";
		    values[7]= "932094";
		    values[8]= "00900B0D9CDA";
		    values[9]=  new java.sql.Timestamp(date.getTime());
		    
		    qRunner.update(conn, sql, values);
			
		}
		conn.commit();
		conn.close();
		
	}
	
	
	@Test
	public void testPreparedSta() throws SQLException, ParseException, ClassNotFoundException {
		//Connection conn = jdbcTemplate.getDataSource().getConnection();
		
		Connection conn = null; 
		  Class.forName("oracle.jdbc.driver.OracleDriver");//加载数据库驱动
		  conn = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.222.53)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.222.54)(PORT = 1521))(LOAD_BALANCE = yes))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = NBUSDB)))","apts", "apts");// 与数据库建立连接
		  conn.setAutoCommit(false);
		
		conn.setAutoCommit(false);
		String sql = "insert into dy_obu_version_hist_test (id,cf_sn,total_mem,sim_imsi,video_sn,gprs_imei,wlan_mac,obuid,motherboard_sn,obu_time) values(?,?,?,?,?,?,?,?,?,?)";		
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss"); 
		Date date = sdf.parse("20130218 150830");
		 
		

		PreparedStatement prest = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < INSERT_COUNT; i++) {
			
			sdf = new SimpleDateFormat("yyyyMMdd HHmmss"); 
		    date = sdf.parse("20130218 150830");
			
			prest.setObject(1, i+1);
			prest.setObject(2, "TSS26071070125002837");
			prest.setObject(3, "1");
			prest.setObject(4, "5");
			prest.setObject(5, "352012006157951");
			prest.setObject(6, "001B111B4660");
			prest.setObject(7, "932094");
			prest.setObject(8, "932094");
			prest.setObject(9, "00900B0D9CDA");
			prest.setObject(10, new java.sql.Timestamp(date.getTime()));
			//prest.setObject(10, new java.sql.Timestamp(date.getTime()));
			//prest.(parameterIndex, x)
			prest.addBatch();
		}
		prest.executeBatch();
		conn.commit();
		prest.clearBatch();
		conn.close();
	}
	
	//@Test
	public void testUpdatePreparedStatement() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		conn.setAutoCommit(false);
		//String sql = "insert into dy_obu_version_hist_test (id,cf_sn,total_mem,sim_imsi,video_sn,gprs_imei,wlan_mac,obuid,motherboard_sn) values(?,?,?,?,?,?,?,?,?)";

		String sql = "update dy_obu_version_hist_test set cf_sn = ?,total_mem=? where id =?";
		
		PreparedStatement prest = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < INSERT_COUNT; i++) {
			if(i%2 == 0){
				prest.setObject(1, "wenxiao");
				prest.setObject(2, null);
				prest.setObject(3, i+1);
				prest.addBatch();
			}
			
		}
		prest.executeBatch();
		conn.commit();
		prest.clearBatch();
		conn.close();
		
		
	}
	

	//@Test
	public void testStatementBatch() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

		for (int x = 0; x < INSERT_COUNT; x++) {
			stmt.addBatch(sqlClause);
		}
		stmt.executeBatch();
		conn.commit();
		stmt.close();
		conn.close();
	}

	//@Test
	public void testInsertSta() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		for (int x = 0; x < INSERT_COUNT; x++) {
			stmt.execute(sqlClause);
		}
		conn.commit();
		conn.close();
	}

	//@Test
	public void testInsertJdbcTemplate() {
		for (int i = 0; i < INSERT_COUNT; i++)
			jdbcTemplate.update(sqlClause);
	}

	//@Test
	public void testInsertJdbcTemplateBatch() {
		String[] sql = new String[INSERT_COUNT];
		for (int i = 0; i < INSERT_COUNT; i++) {
			sql[i] = sqlClause;
		}
		jdbcTemplate.batchUpdate(sql);
	}

}
