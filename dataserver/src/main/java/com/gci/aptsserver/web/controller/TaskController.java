package com.gci.aptsserver.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.task.DataStoreTask;
import com.gci.aptsserver.task.TaskManager;
import com.gci.aptsserver.util.ParseCsvUtil;
import com.gci.aptsserver.util.SpringUtil;


@Controller
@RequestMapping(value = "/task")
public class TaskController {
	
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * 管理界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index() {
		
		//TaskManager.init();
		
		ModelAndView mv = new ModelAndView("task/index");
		mv.addObject("taskMap", TaskManager.scheduleMap);
		return mv;
	}
	
	/**
	 * 添加线程
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * 例子：http://localhost:8080/apts-server/task/addThread/runningBusTask
	 */	
	
	@RequestMapping("/addThread/{taskType}")  
	@ResponseBody
	public String addThread(@PathVariable String taskType) throws InstantiationException, IllegalAccessException {
		
		TaskManager.addThread(taskType, 1L, 3L);	
		return "success";
		
	}
	
	/*
	@RequestMapping("/addThread/{taskType}/{time}")  
	@ResponseBody
	public String addThread(@PathVariable String taskType,@PathVariable Long time) throws InstantiationException, IllegalAccessException {
		
		TaskManager.startTask(taskType, 1L, time);
		
		return "success";
		
	}*/
	
	

	/**
	 * 启动任务
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * 例子：http://localhost:8080/apts-server/task/start/runningBusTask
	 */
	@RequestMapping("/start/{taskType}")
	@ResponseBody
	public String start(@PathVariable String taskType) throws InstantiationException, IllegalAccessException {
		
		//RedisKey.notifyDb.name();
		//TaskManager.startTask("dpdb.queue.notify.db", 1L, 3L);
		//TaskManager.startTask(RedisKey.notifyDb.getKey(), 1L, 3L);
		TaskManager.startTask(taskType, 1L, 3L);

		return "success";
	}
	
	/**
	 * 启动所有任务
	 */
	@RequestMapping("/startAll")
	@ResponseBody
	public String startAll() throws InstantiationException, IllegalAccessException {
		
		TaskManager.startAllTask(1L);

		return "success";
	}
	

	/**
	 * 停止任务
	 * 
	 * @return
	 */
	@RequestMapping("/stop/{taskType}")
	@ResponseBody
	public String stop(@PathVariable String taskType) {
		TaskManager.stopTask(taskType);

		return "success";
	}
	
	/**
	 * 停止所有任务
	 */
	@RequestMapping("/stopAll")
	@ResponseBody
	public String stopAll() throws InstantiationException, IllegalAccessException {
		
		TaskManager.stopAllTask();

		return "success";
	}	

	/**
	 * 当前可以提供的服务类型
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/taskType")
	public Set<String> taskTypes() {
		return RedisParseFactory.redisParseConfig.getRedisQueues().keySet();
	}
	
	/**
	 * 测试数据库连接用
	 */
	@RequestMapping("/test")
	@ResponseBody
	public String testConnection() {
		
		int count  = jdbcTemplate.queryForInt("select count(*) from bs_bus");
		
		return String.valueOf(count);
	}
	
	/**
	 * 测试Task
	 */
	@RequestMapping("/testTask")
	@ResponseBody
	public String testTask() {
		
		DataStoreTask dataStore = (DataStoreTask) SpringUtil.getBean("dataStoreTask");
		
		
		List<String> list = new ArrayList<String>();
		
		String data ="{\"type\":\"0a\",\"operation\":\"update\",\"value\":{\"obuid\":\"310051\",\"dr_status\":49,\"dr_mode\":55}}";
		list.add(data);
		
		//dataStore.save(list);
		
		return "success";
	}
	
	
	
	/**
	 * 上传文件界面
	 * 
	 * @return
	 */
	@RequestMapping("/upload")
	public ModelAndView upload() {
						
		ModelAndView mv = new ModelAndView("task/uploadFile");
		return mv;
	}
	
	/**
	 * 批量上传界面
	 */
	@RequestMapping("/batchupload")
	public ModelAndView batchupload() {
						
		ModelAndView mv = new ModelAndView("task/batchUpload");
		return mv;
	}	
	
	/**
	 * 上传映射文件
	 * @throws URISyntaxException 
	 */
	 @RequestMapping(value="/uploadfile",method=RequestMethod.POST)
	 public ModelAndView uploadFile(@RequestParam("file") MultipartFile file,HttpServletRequest request){
		 
		 
		 //String dir = request.getSession().getServletContext().getRealPath(path);		  
		 
		 //上传csv文件
		 String path = TaskController.class.getResource("/").getPath();//获得存放文件根路径
		 //System.out.println(path);
		 String fileName = file.getOriginalFilename();	//获取文件名	 				 
		 File targetFile = new File(path+"/csv",fileName); 		 
		 if(!targetFile.exists()){
	            targetFile.mkdirs();
	        }
        //保存
        try {
            file.transferTo(targetFile);            
            TaskManager.stopTaskManager();//停止TaskManager服务            
            
            RedisParseFactory.redisParseConfig = null;
            
            //解析csv文件，生成xml配置文件
            ParseCsvUtil.parseCsv(path,fileName);             
            
            fileName = fileName.replace("csv", "xml");//把后缀名改为xml文件名
            
            RedisParseFactory.init();
            //加载配置文件
            RedisParseFactory.addXmlConfigFile(fileName);
            
            //重新初始化任务
            TaskManager.init();
                       
            
        } catch (Exception e) {
            e.printStackTrace();
        }                                               
		 
        return new ModelAndView("redirect:/task/index",TaskManager.scheduleMap);
		 
	 }
	 
	 
	/**
	 * 上传多个映射文件
	 * @throws URISyntaxException 
	 */
	 @RequestMapping(value="/fileupload",method=RequestMethod.POST)
	 public ModelAndView fileupload(HttpServletRequest request,@RequestParam("filedata") MultipartFile file){

		TaskManager.stopTaskManager();//停止TaskManager服务            	        
	    RedisParseFactory.redisParseConfig = null;
		try {
			// String uploadDir =
			// request.getSession().getServletContext().getRealPath("/WEB-INF/classes/csv");
			// 上传csv文件
			String path = TaskController.class.getResource("/").getPath();// 获得存放文件根路径
			String uploadDir = path + "csv";
			String fileName = file.getOriginalFilename();	//获取文件名
			
			File dirPath = new File(uploadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			file.transferTo(new File(uploadDir + "/" + fileName));

		    //解析csv文件，生成xml配置文件
	        ParseCsvUtil.parseCsv(path,fileName);             	        
	        fileName = fileName.replace("csv", "xml");//把后缀名改为xml文件名	     
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
		//return new ModelAndView("redirect:/task/index",TaskManager.scheduleMap);
	 }
	 
	/**
	 * 初始化任务
	 * 
	 * @return
	 */
	@RequestMapping("/inittask")
	public ModelAndView initTask() {
						
		RedisParseFactory.init();
        //重新初始化任务
        TaskManager.init();					
		
		ModelAndView mv = new ModelAndView("redirect:/task/index",TaskManager.scheduleMap);
		return mv;
	}
	 
}
