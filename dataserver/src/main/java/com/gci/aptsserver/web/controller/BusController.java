package com.gci.aptsserver.web.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gci.aptsserver.task.TaskBean;
import com.gci.aptsserver.util.SpringUtil;


@Controller
@RequestMapping(value = "/bus")
public class BusController {
	
	private static TaskBean bean = new TaskBean();
	private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(30);// 线程池
	
	static{
		init();
	}
	
	public static void init(){
		
		bean.setRunning(false);
		bean.setTaskKey("checkBusRunStatusTask");
	}
	
	@RequestMapping(value = "/index")
	public ModelAndView index() {
		
		ModelAndView mv = new ModelAndView("/task/busTask");
		mv.addObject("task", bean);
		return mv;
	}
	
	/**
	 * 启动任务
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * 例子：http://localhost:8080/apts-server/task/start/runningBusTask
	 */
	@RequestMapping("/start/{taskType}/{time}")
	@ResponseBody
	public String start(@PathVariable String taskType,@PathVariable Long time) throws InstantiationException, IllegalAccessException {
		
		Runnable runnable = (Runnable) SpringUtil.getBean(taskType);
		bean.setTaskKey(taskType);
		bean.setScheduledFuture(executorService.scheduleWithFixedDelay(runnable, 1L, time, TimeUnit.SECONDS));	
		bean.setRunning(true);

		return "success";
	}
	
	/**
	 * 停止任务
	 * 
	 * @return
	 */
	@RequestMapping("/stop")
	@ResponseBody
	public String stop() {
		
		bean.getScheduledFuture().cancel(true);
		bean.setRunning(false);
		return "success";
	}

}
