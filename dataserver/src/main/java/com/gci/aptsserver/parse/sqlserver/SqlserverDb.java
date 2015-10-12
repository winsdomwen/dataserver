package com.gci.aptsserver.parse.sqlserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;
import com.gci.aptsserver.parse.IDatabase;
import com.gci.aptsserver.util.CollectionUtil;

public class SqlserverDb implements IDatabase{

	private DbTable table;

	@Override
	public void setTable(DbTable table) {
		// TODO Auto-generated method stub	
		this.table = table;
	}

	@Override
	public String sqlQuery(Map<String, String> record) {
		// TODO Auto-generated method stub
		StringBuilder sBuilder = new StringBuilder();
		
		sBuilder.append(" select count(*) from " + table.getTableName());// + " where " + this.primaryKey + "='" + record.get(this.primaryKey) + "'";
		List<String> keyList = table.getPrimaryKey().getKey();
		
		for(int i =0; i< keyList.size(); i++){
			String primaryKey = keyList.get(i);
			DbField field = table.getFields().get(primaryKey);
			String redisKey = field.getRedisField();
			
			if(i == 0){
				sBuilder.append("  where " + primaryKey + " = '" + record.get(redisKey) + "'");
			}else
				sBuilder.append("  and " + primaryKey + " = '" + record.get(redisKey) + "'");			
		}
		
		return sBuilder.toString();
	}

	@Override
	public String sqlInsert(Map<String, String> record) {
		// TODO Auto-generated method stub
		if(record.keySet().size() <=0)
			return null;
		
		Map<String, DbField> fields = table.getFields();
		StringBuilder sBuilder = new StringBuilder();		

		// 获取需要更新的字段
		Set<String> needUpdateKeys = CollectionUtil.intersection(record, fields);		

		sBuilder.append(" insert into " + table.getTableName() + " (");
		sBuilder.append(CollectionUtil.join(needUpdateKeys, ","));
		sBuilder.append(") values(");


		// 拼接value
		List<String> valueSet = new ArrayList<String>();
		for (String key : needUpdateKeys) {
			DbField field = fields.get(key);
			String redisKey = field.getRedisField();
			valueSet.add(toSqlString(field,record.get(redisKey)));
		}
		sBuilder.append(CollectionUtil.join(valueSet, ","));

		sBuilder.append(")");

		return sBuilder.toString();
	}

	@Override
	public String getInsertScript() {
		// TODO Auto-generated method stub
		Map<String, DbField> fields = table.getFields();
		Map<Integer,DbField> map = new HashMap<Integer,DbField>();
		
		Set<String> key = fields.keySet();
        for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            //.out.println(fields.get(s));
            DbField  field = fields.get(s);
            map.put(field.getIndex(), field);           			            
        }		
        
        StringBuilder sb = new StringBuilder(); 
        StringBuilder sb2 = new StringBuilder();
        
        sb.append(" insert into " + table.getTableName() + " (");
        sb.append(map.get(1).getDbField());
        sb2.append("?");
        
        for(int i=1; i< map.size(); i++){
        	
        	sb.append(","+map.get(i+1).getDbField());
        	sb2.append(",?");
        }
                		
        sb.append(") values(").append(sb2).append(")");
        
		return sb.toString();
	}

	@Override
	public String sqlUpdate(Map<String, String> record) {
		// TODO Auto-generated method stub
		Map<String, DbField> fields = table.getFields();
		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append(" update " + table.getTableName() + " set ");

		// 获取需要更新的字段
		//Set<String> needUpdateKeys = CollectionUtil.intersectionSet(record.keySet(), this.fields.keySet());
		Set<String> needUpdateKeys = CollectionUtil.intersection(record, fields);
		
		// 拼接value
		Set<String> valueSet = new HashSet<String>();
		for (String key : needUpdateKeys) {
			if(!table.isPrimaryKey(key)){//不修改主键
				DbField field = fields.get(key);
				String redisKey = field.getRedisField();
				valueSet.add(key + "=" + toSqlString(field,record.get(redisKey)));
			}
		}
		//如果没有修改的字段
		if(valueSet.size() == 0){
			return null;
		}else{
		
			sBuilder.append(CollectionUtil.join(valueSet, ","));
			
			List<String> keyList = table.getPrimaryKey().getKey();
			
			for(int i =0; i< keyList.size(); i++){
				String primaryKey = keyList.get(i);
				DbField field = fields.get(primaryKey);
				String redisKey = field.getRedisField();
				
				if(i == 0){
					sBuilder.append("  where " + primaryKey + " = '" + record.get(redisKey) + "'");
				}else
					sBuilder.append("  and " + primaryKey + " = '" + record.get(redisKey) + "'");
				
			}
			
			/*
			DbField field = fields.get(this.primaryKey);
			String redisKey = field.getRedisField();
			sBuilder.append("  where " + this.primaryKey + " = '" + record.get(redisKey) + "'");
			*/
			return sBuilder.toString();
		}						
	}
	
	/**
	 * sql 转换
	 * 把字段转为SQL能识别的值
	 * @return
	 */
	public String toSqlString(DbField field,String value) {
		// number

		// date
		String dbType = field.getDbType();
		String formatter = field.getFormatter();

		// 字符串
		if (dbType.equals("string"))
			return "'" + value + "'";
		else if(dbType.equals("date"))
		{
			if(formatter.equals("yyyyMMdd HH24miss"))
				return "stuff(stuff('"+value+"',12, 0,':'),15,0,':')";
			else 
				return "'" + value + "'";
		}
		else return value;				
	}
}
