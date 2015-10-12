package com.gci.tmp;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class FileTest {

	 @Test
	public void testDir() throws URISyntaxException {

		File f = new File(Object.class.getResource("/model").toURI());

		System.out.println(Object.class.getResource("/model").toURI());

		if (f.exists() && f.isDirectory()) {
			String[] str = f.list();
			for (int i = 0; i < str.length; i++) {
				System.out.println(str[i]);
			}
		}
	}

}
