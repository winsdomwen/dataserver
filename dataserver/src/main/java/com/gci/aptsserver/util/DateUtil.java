package com.gci.aptsserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @ClassName: DateUtil
 * @Description: TODO
 * @author Kuhn
 * @date Dec 5, 2012 4:35:55 PM
 * 
 */
public class DateUtil {

	public static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 判断字符串是否是有效的日期， 格式"yyyy-MM-dd,yyyy-MM-d,yyyy-M-dd,yyyy-M-d
	 * "yyyy/MM/dd,yyyy/MM/d,yyyy/M/dd,yyyy/M/d" "yyyyMMdd"
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isValidDate(String date) {
		if ((date == null) || (date.length() < 8)) {
			return false;
		}
		try {
			boolean result = false;
			SimpleDateFormat formatter;
			char dateSpace = date.charAt(4);
			String format[];
			if ((dateSpace == '-') || (dateSpace == '/')) {
				format = new String[4];
				String strDateSpace = Character.toString(dateSpace);
				format[0] = "yyyy" + strDateSpace + "MM" + strDateSpace + "dd";
				format[1] = "yyyy" + strDateSpace + "MM" + strDateSpace + "d";
				format[2] = "yyyy" + strDateSpace + "M" + strDateSpace + "dd";
				format[3] = "yyyy" + strDateSpace + "M" + strDateSpace + "d";
			} else {
				format = new String[1];
				format[0] = "yyyyMMdd";
			}

			for (int i = 0; i < format.length; i++) {
				String aFormat = format[i];
				formatter = new SimpleDateFormat(aFormat);
				formatter.setLenient(false);
				String tmp = formatter.format(formatter.parse(date));
				if (date.equals(tmp)) {
					result = true;
					break;
				}
			}
			return result;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是有效的日期，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isValidTime(String date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);
			formatter.setLenient(false);
			formatter.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 转换字符串为日期
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		return formatter.parse(date);
	}

	/**
	 * 转换字符串为日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) throws ParseException {
		return parseDate(date, DEFAULT_FORMAT);
	}

	/**
	 * 转换日期为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 转换日期为字符串
	 * 
	 * @param date
	 * @return
	 */

	public static String formatDate(Date date) {
		if (date == null)
			return null;
		return formatDate(date, DEFAULT_FORMAT);
	}

	public static Date dateComputer(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(field, amount);
		return calendar.getTime();
	}
}
