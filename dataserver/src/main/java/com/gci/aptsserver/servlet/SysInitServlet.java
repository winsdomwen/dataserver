package com.gci.aptsserver.servlet;

import javax.servlet.http.HttpServlet;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.gci.aptsserver.task.TaskManager;
/**
 * /**
 * 系统初始化完成后启动服务
 * @author wenxiao
 *
 */

public class SysInitServlet extends HttpServlet{
	
	private static final long serialVersionUID = -2135175688237370903L;

	public SysInitServlet() {
		super();
	}

	public void init()  {
		
		String param = this.getInitParameter("start");		 		
		
		if(param.equals("true")){//如果要求tomcat启动运行任务
			WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			TaskManager.startAllTask(1L);
		}
		
	}
	
	public void destroy() {


	}

}
