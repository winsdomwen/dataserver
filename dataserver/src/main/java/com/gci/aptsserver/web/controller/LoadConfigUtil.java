package com.gci.aptsserver.web.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import com.gci.aptsserver.parse.RedisParseConfig;
import com.gci.aptsserver.parse.RedisParseFactory;
import com.gci.aptsserver.util.XmlUtil;

public class LoadConfigUtil {
	
	public static  RedisParseConfig  logConfigXml(){
		String FILE_PATH = "/model";
		
		RedisParseConfig newConfig = new RedisParseConfig();
		newConfig.setLastUpdated(new Date());
				
		RedisParseConfig properties = null;
		
		try {
			
			//System.out.println(Object.class.getResource("/model").toURI());
			
			File filePath = new File(RedisParseFactory.class.getResource(FILE_PATH).toURI());
						
			//File filePath = new File(path);
			//System.out.println(filePath.isDirectory());
			
			if (filePath.exists() && filePath.isDirectory()) {// 输入的必须是目录
	
				//System.out.println("path is directory");
				
				String[] fileNames = filePath.list();
				
				System.out.println("model file length == "+fileNames.length);
				
				
				for (String fileName : fileNames) {
					//System.out.println(fileName);															
					
					InputStream stream = RedisParseFactory.class.getResourceAsStream(FILE_PATH+"/"+fileName);
													
					properties = (RedisParseConfig) XmlUtil.simplexml2object(stream, new RedisParseConfig());
					newConfig.merge(properties);
					
					stream.close();
				}											
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return newConfig;
		
	}

}
