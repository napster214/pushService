package com.jadlsoft.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class NullUtils {


	/**
	 * 获取指定值是否为空或者大小为0
	 * @功能: 支持常用的数据类型：
	 * 		String	null或者为""
	 * 		Integer	null
	 * 		List	null或者size为0
	 * 		Map		null或者size为0
	 * @param value 要校验的值
	 * @param valueClass 值的类型
	 * @return: boolean 为空返回true
	 */
	public static boolean isNullOrEmpty(Object value) {
		if (value == null) {
			return true;
		}else {
			if (value instanceof String) {
				//String类型
				if (value.equals("")) {
					return true;
				}else {
					return false;
				}
			}
			if (value instanceof Integer) {
				//Integer类型
				return false;
			}
			if (value instanceof List) {
				//List类型
				List objList = (List)value;
				if (objList.size()==0) {
					return true;
				}else {
					return false;
				}
			}
			if (value instanceof Map) {
				//map类型
				Map objMap = (Map)value;
				if (objMap.size()==0) {
					return true;
				}else {
					return false;
				}
			}
			if (value instanceof Set) {
				//Set类型
				Set objMap = (Set)value;
				if (objMap.size()==0) {
					return true;
				}else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * 字符串为空或者空字符串
	 * @param str	判断的str
	 * @return: boolean
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 是否为空，加入全部为空格的校验
	 * @param str  判断的str
	 * @return: boolean
	 */
	public static boolean isBlank(String str) {
		if (isEmpty(str)) {
			return true;
		}else {
			if (isEmpty(str.replaceAll(" ", ""))) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	/**
	 * 获取object的String值
	 * @功能: 如从map中获取的值，如果为空就是空字符串，否则就强转为String类型
	 * @param obj
	 * @return: String
	 */
	public static String getStrFromObject(Object obj) {
		if (obj == null) {
			return "";
		}
		if (obj instanceof String) {
			return (String)obj;
		}
		if (obj instanceof Integer || obj instanceof Float
				|| obj instanceof Double || obj instanceof Long) {
			return obj+"";
		}
		if (obj instanceof List) {
			return JsonUtil.list2json((List)obj);
		}
		if (obj instanceof Map) {
			return JsonUtil.map2json((Map)obj);
		}
		return "";
	}
}
