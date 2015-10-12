package com.gci.aptsserver.task;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.gci.aptsserver.model.Bus;
import com.gci.aptsserver.model.TripBegin;
import com.gci.aptsserver.redis.RedisKey;



/**
 * 读取数据
 * 
 * @ClassName: ObuApplyTask
 * @Description: TODO
 * @author wenxiao
 * @date Dec 28, 2012 9:19:03 AM
 * 
 */
@Component
public class ObuApplyTask  extends BaseTask{	

	private static final Logger logger = LoggerFactory.getLogger(ObuApplyTask.class);	
	
	/*
	 * 获得驾驶员信息
	 */
	protected Map<String, String> getRunningBus(String busId) {
		Jedis jedis = jedisPool.getResource();
		//Map<String, String> map  = jedis.hgetAll(RedisKey.runningBus.getKey(busId));
		
    	Map<String, String> map = new HashMap<String, String>();
    	   	
    	String[] array = {"employee_id","route_id_belongto","route_name_belongto","employee_name","qualification"};
    	List<String> redisList = jedis.hmget(RedisKey.runningBus.getKey(busId),array);
    	
    	for(int j = 0; j< array.length;j++){   
    		String value = redisList.get(j);
    		if(value!=null&&!value.equals("")){
    			map.put(array[j], redisList.get(j));
    		}
    	}    
		
		jedisPool.returnResource(jedis);
		return map;
	}
	
	
	protected class BusMapper implements RowMapper<Bus> {   
		
		@Override
		public Bus mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Bus bus = new Bus();			
			
			bus.setId(rs.getLong("bus_id"));
			bus.setBusCode(rs.getString("bus_code"));
			bus.setNumberPlate(rs.getString("number_plate"));			
			
			return bus;				
		}		  
	}    
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	
		List<String> redisRecords = this.getRedisData(redisQueueKey) ;
		if(redisRecords == null )
			return;

		//System.out.println("ObuApplyRecords = " + redisRecords.size());
		//long startTime = System.currentTimeMillis();   //获取开始时间			
		this.save(redisRecords);

		//long endTime = System.currentTimeMillis(); //获取结束时间  
		//System.out.println("ObuApplyTask程序运行时间： "+(endTime-startTime)+"ms");		
				
	}
	
	public void save(List<String> redisRecords){
		
		String sql = null;

		for (String record : redisRecords) {
			//System.out.println(record);
			try {

				ObjectMapper mapper = new ObjectMapper();

				@SuppressWarnings("rawtypes")
				Map parsedRecord = mapper.readValue(record, Map.class);

				String routeCode = (String) parsedRecord.get("route_code");
				String taskType = (String) parsedRecord.get("task_type");
				String obuId = (String) parsedRecord.get("obuid");
				String user = (String) parsedRecord.get("operator");
				String planTime = (String) parsedRecord.get("first_time");
				
				
				 //生成路单
                TripBegin trip = new TripBegin();
				
				sql = "select * from bs_bus where obu_id =?";
		     
				Bus bus = jdbcTemplate.queryForObject(sql, new Object[] {obuId}, new BusMapper());
		        
		        if(bus == null )
		        	continue;
		        
		        trip.setObuId(obuId);
		        trip.setBusId(bus.getId());
		        trip.setBusCode(bus.getBusCode());
		        trip.setNumberPlate(bus.getNumberPlate());		       		        
		        
		        Map<String,String> runningBusMap = getRunningBus(String.valueOf(bus.getId()));		        
	                   
		        if (runningBusMap.containsKey("employee_id") && !runningBusMap.get("employee_id").equals("")){
		        	
		        	Long employeeId = Long.valueOf(runningBusMap.get("employee_id"));
		        	trip.setEmployeeId(employeeId);
		        }
	                       	                    
		        if (runningBusMap.containsKey("route_id_belongto") && !runningBusMap.get("route_id_belongto").equals("")){
		        	Long routeIdBelongTo = Long.valueOf(runningBusMap.get("route_id_belongto"));
		        	trip.setRouteIdBelongTo(routeIdBelongTo);
		        }
		        trip.setRouteNameBelongTo(runningBusMap.get("route_name_belongto"));  
		        trip.setEmployeeName(runningBusMap.get("employee_name"));
		        trip.setQualification(runningBusMap.get("qualification"));
		        trip.setDispatcherUser(user);
		        trip.setPlanTime(planTime);
		        
		        
		        sql = "select t.routesub_id ,t.route_sub_name ,"+
		              " t.route_id as route_id,r.route_name as route_name,t.service_type as service_type,"+
		        	  " t.direction as direction"+
		        	  " from bs_routesub t left join bs_route r on t.route_id=r.route_id"+
		        	  " where r.route_code='"+routeCode+"' and t.service_number='"+taskType+"'";
		        
		        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		        
		        if(list.size() > 0){
		        			        
			        Map<String, Object> routeSub = list.get(0);		  
			        
			        Long routeSubId = ((BigDecimal)routeSub.get("routesub_id")).longValue(); 
			        String routeSubName = String.valueOf(routeSub.get("route_sub_name")); 
			        Long routeIdRun = ((BigDecimal)routeSub.get("route_id")).longValue();
			        String routeNameRun = String.valueOf(routeSub.get("route_name"));
			        String serviceType = String.valueOf(routeSub.get("service_type"));
			        String direction = String.valueOf(routeSub.get("direction"));
			        
			        trip.setRouteSubId(routeSubId);
			        trip.setRouteSubName(routeSubName);
			        trip.setRouteIdRun(routeIdRun);
			        trip.setRouteNameRun(routeNameRun);
			        trip.setServiceType(serviceType);
			        trip.setDirection(direction);
			        
			        sql = trip.getInertSql();
			        			        
			        jdbcTemplate.execute(sql);  
		        }		        

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("error");
				logger.error(e.getLocalizedMessage()+"\r\n"+"redis_data == " + record+"\r\n"+"sql == " + sql);	

			}
		}
		
	}

	@Override
	public boolean batchSaveRecords(List<String> redisRecords) {
		// TODO Auto-generated method stub
		return false;
	}

}
