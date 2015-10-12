package com.gci.aptsserver.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试使用，
* @ClassName: TestRunnable 
* @Description: TODO
* @author Kuhn
* @date Dec 26, 2012 3:06:16 PM 
*
 */
@Component
public class TestRunnable implements Runnable {
	
	static private Logger logger = LoggerFactory.getLogger(TestRunnable.class);
	
	@Override
	public void run() {
		System.out.println("test runnable");
		logger.info(" TestRunnable I am testing just test");
	}

}
