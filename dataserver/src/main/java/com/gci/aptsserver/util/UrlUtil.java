package com.gci.aptsserver.util;

/**
 * url工具类
* @ClassName: UrlUtil 
* @Description: TODO
* @author Kuhn
* @date Sep 22, 2012 2:51:13 PM 
*
 */
public class UrlUtil {

	static final public String URL_SEPARATE = "$#$";// 替换 & 参数自定义分割
	static final public String URL_EQUAL = "##$";// 替换 =;

	/**
	 * 过滤参数的关键字
	 * @param para
	 * @return
	 */
	static public String decodeUrl(String para) {
		String p = para.replaceAll("&", URL_SEPARATE);
		String p2 = p.replaceAll("=", URL_EQUAL);
		return p2;
	}

	/**
	 * 过滤参数的关键字
	 * @param para
	 * @return
	 */
	static public String encodeUrl(String para) {
		String p = para.replaceAll(URL_SEPARATE, "&");
		String p2 = p.replaceAll(URL_EQUAL, "=");
		return p2;
	}

}
