package com.gci.aptsserver.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class TestControllerTest {
	@Resource
	@Qualifier("testController")
	private TestController testController;

	@Test
	public void testRedis() {
		List<String> list = testController.redis();
		for (String value : list) {
			System.out.println(value);
		}
	}

	@Test
	public void testJdbc() {
		System.out.println(testController.jdbc());
	}

}
