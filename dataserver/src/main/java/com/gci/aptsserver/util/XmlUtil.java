package com.gci.aptsserver.util;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {
	// 将对象转为XML
	public static String simpleobject2xml(Object obj) {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias(obj.getClass().getSimpleName(), obj.getClass());
		String xml = xStream.toXML(obj);
		return xml;
	}

	// 将XML转为对象
	public static Object simplexml2object(String xml, Object obj) {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias(obj.getClass().getSimpleName(), obj.getClass());
		Object reobj = xStream.fromXML(xml);
		
		return reobj;
	}
	
	// 将XML转为对象
	public static Object simplexml2object(InputStream stream, Object obj) {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias(obj.getClass().getSimpleName(), obj.getClass());
		Object reobj = xStream.fromXML(stream);
		
		return reobj;
	}
}
