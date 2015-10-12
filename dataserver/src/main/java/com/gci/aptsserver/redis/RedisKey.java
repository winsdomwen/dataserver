package com.gci.aptsserver.redis;

public enum RedisKey {
	
	runningBus("dpdb.rt.bus_id="),//runninBus，车辆id获取
    notifyDb("dpdb.queue.notify.db"),//入库队列
    notifyMem("dpdb.queue.notify.mem"),//内存修改
    abnormal("dpdb.queue.income.0b"),  //异常数据
	obuApplyDispatch("dpdb.queue.terminal.03"), //终端触发队列
	gps("dpdb.queue.income.05"), //gps队列
	gpsFull("dpdb.queue.full.05"), //gpsfull队列
	tripLogbegin("dpdb.queue.40.01"),//TripLogbegin队列
	dispatchInfo("dpdb.queue.dispatch.db"),//调度信息队列
	specialBus("dpdb.queue.40.03"),//专车队列
	passengerSum("dpdb.queue.income.passenger"),//公交客流统计
	adreal("dpdb.queue.adreal.04"),
	checkAttendance("dpdb.queue.income.07");
    
    private String key;

    private RedisKey(String key) {
        this.key = key;
    }

   public String getKey(){	   
	   return this.key;
   }
    
   public  String getKey(String id) {
        String r = key;
        if (id != null) {
            r = this.key + id;
        } else {
            r = this.key;
        }
        return r;
    }
  

} 
