package com.gci.aptsserver.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpUtil {

	/**
	 * post 请求
	 * 
	 * @param urlString
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static String sendPost(String urlString, Map<String, String> map)
			throws IOException {
		StringBuffer stringBuffer = new StringBuffer("1=1");

		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			stringBuffer.append("&" + key + "=" + map.get(key));
		}
		return sendPost(urlString, stringBuffer.toString());
	}

	/**
	 * post请求
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String sendPost(String urlString, String parameter)
			throws IOException {

		StringBuffer sendBack = new StringBuffer();

		URL url;

		url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setUseCaches(false);
		con.setDoOutput(true);// 开启输入输出
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.connect();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				con.getOutputStream(), "UTF-8"));
		out.write(parameter);
		out.flush();
		out.close();

		InputStream is = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while ((s = br.readLine()) != null) {
			sendBack.append(s);
		}

		return sendBack.toString();
	}

	/**
	 * get 请求
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static String sendGet(String urlString) throws IOException {
		StringBuffer sendBack = new StringBuffer();

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();

		InputStream is = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while ((s = br.readLine()) != null) {
			sendBack.append(s);
		}

		return sendBack.toString();
	}

}
