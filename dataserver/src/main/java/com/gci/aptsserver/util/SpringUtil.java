package com.gci.aptsserver.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 读取spring 工厂中的bean 慎用
* @ClassName: SpringContextUtil 
* @Description: TODO
* @author Kuhn
* @date Dec 27, 2012 2:13:15 PM 
*
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

}
