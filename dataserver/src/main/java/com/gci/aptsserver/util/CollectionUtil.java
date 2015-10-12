package com.gci.aptsserver.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gci.aptsserver.parse.DbField;

/**
 * 集合工具类
 * 
 * @ClassName: SetUtil
 * @Description: TODO
 * @author Kuhn
 * @date Dec 28, 2012 3:08:10 PM
 * 
 */
public class CollectionUtil {

	public static <T> List<T> intersectionList(List<T> l1, List<T> l2) {
		Set<T> re = new HashSet<T>();
		re.addAll(l1);
		re.addAll(l2);
		List<T> reList = new ArrayList<T>();
		reList.addAll(re);
		return reList;
	}

	/**
	 * 交集
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static <T, E> Set<T> intersectionSet(Set<T> s1, Set<T> s2) {

		Set<T> re = new HashSet<T>();

		for (T t : s1) {
			if (s2.contains(t)) {
				re.add(t);
			}
		}
		return re;
	}
	
	public static Set<String> intersection(Map<String,String> record, Map<String, DbField> fields){
		Set<String> re = new HashSet<String>();
		
		Set<String> set = record.keySet();
		Set<String> keySet = fields.keySet();
		
		for(String key : keySet){
			DbField field = fields.get(key);
			String redisKey = field.getRedisField();

			if(set.contains(redisKey) ){
				
				Object value =  record.get(redisKey);
				if(value != null && value.toString().length() > 0)
					re.add(field.getDbField());			  	
			}
		}		
		return re;
		
	}
	
	

	/**
	 * 并集
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static <T> Set<T> unionSet(Set<T> s1, Set<T> s2) {
		Set<T> re = new HashSet<T>();
		re.addAll(s1);
		re.addAll(s2);
		return re;
	}

	public static <T> String join(Collection<T> s, String sep) {

		if (null == s || s.isEmpty()) {
			return "";
		}

		StringBuilder sBuilder = new StringBuilder();

		Iterator<T> it = s.iterator();
		sBuilder.append(it.next().toString());

		while (it.hasNext()) {
			sBuilder.append(sep + it.next().toString());
		}

		return sBuilder.toString();
	}

}
