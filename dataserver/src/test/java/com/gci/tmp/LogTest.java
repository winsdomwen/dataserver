package com.gci.tmp;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring/*.xml")
public class LogTest {

	private Logger logger = LoggerFactory.getLogger(LogTest.class);

	@Test
	public void testLog() {

		DOMConfigurator.configure(FileTest.class.getResource("/log4j/log4j.xml"));// 加载.xml文件
		// PropertyConfigurator.configure(FileTest.class.getResource("/log4j/log4j.properties"));

		logger.info("info msg");
		logger.warn("warn msg");
		logger.error("error msg");
		System.out.println("console msg");
	}
}
