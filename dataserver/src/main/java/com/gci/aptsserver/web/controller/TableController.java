package com.gci.aptsserver.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gci.aptsserver.parse.DbField;
import com.gci.aptsserver.parse.DbTable;

/**
 * 配置文件生成
 * 
 * @ClassName: TableController
 * @Description: TODO
 * @author Kuhn
 * @date Dec 28, 2012 4:26:55 PM
 * 
 */
@RequestMapping(value = "/table")
@Controller
public class TableController {

	@Resource
	private JdbcTemplate jdbcTemplate;

	private final String TABLE_STRUCTURE = "select COLUMN_NAME,DATA_TYPE,DATA_LENGTH from user_tab_columns  where table_name=";

	@RequestMapping(value = "/show/{tableName}")
	@ResponseBody
	public List<Map<String, Object>> show(@PathVariable String tableName) {
		String sql = TABLE_STRUCTURE + "'" + tableName + "'";
		return jdbcTemplate.queryForList(sql);
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public List<String> list() {
		String sql = "select distinct table_name from user_tab_columns order by table_name";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@RequestMapping(value = "/create/{tableName}/{id}/{op}")
	@ResponseBody
	public DbTable create(@PathVariable String tableName, @PathVariable String id, @PathVariable boolean op) {
		String sql = TABLE_STRUCTURE + "'" + tableName + "'";

		// obuversion
		DbTable table = new DbTable();
		table.setTableName(tableName);
		//table.setUpdateId(id);
		//table.setPrimaryKey(id);
		table.setUpdate(op);

		List<Map<String, Object>> tableStructures = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> record : tableStructures) {
			DbField field = new DbField();
			field.setDbField(record.get("COLUMN_NAME").toString().toLowerCase());
			field.setDbType(record.get("DATA_TYPE").toString().toLowerCase());
			field.setRedisField(field.getDbField());
			field.setFormatter("yyyyMMdd HHmmss");
			table.getFields().put(field.getRedisField(), field);
		}

		return table;
	}

}
