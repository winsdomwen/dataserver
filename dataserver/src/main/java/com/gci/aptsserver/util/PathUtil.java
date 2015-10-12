package com.gci.aptsserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 工程路径和class路径
* @ClassName: PathUtil 
* @Description: TODO
* @author Kuhn
* @date Jan 6, 2013 4:35:38 PM 
*
 */
public class PathUtil {

	public static final String classPath;
	public static final String projectPath;

	static {
		URL url = PathUtil.class.getResource("/");
		String path = url.toString();
		
		if (path.startsWith("zip")) {// 当class文件在war中时，此时返回zip:D:/...这样的路径
			path = path.substring(4);
		} else if (path.startsWith("file")) {// 当class文件在class文件中时，此时返回file:/D:/...这样的路径
			path = path.substring(6);
		} else if (path.startsWith("jar")) {// 当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
			path = path.substring(10);
		}
		
		classPath = path;
		int index = path.indexOf("WEB-INF");

		if (index == -1) {
			index = path.indexOf("classes");
		}

		if (index == -1) {
			index = path.indexOf("bin");
		}

		path = path.substring(0, index);

		
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		projectPath = path;

	}
}
