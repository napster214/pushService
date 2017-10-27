package com.jadlsoft.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: StringUtil.java
 * </p>
 * <p>
 * Description: 公用String函数
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 京安丹灵
 * </p>
 * 
 * @author 李帮贵
 * @version 1.0
 */

public class StringUtils {

	public static String filteNull(String str) {
		return (str == null || str.equalsIgnoreCase("null")) ? "" : str;
	}

	/**
	 * Description: 字符串转换成整数
	 * s
	 * @param strvalue
	 * @param defaultNum
	 * @return
	 */
	public static int convertIntDef(String strvalue, int defaultNum) {
		if (strvalue != null && !strvalue.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(strvalue);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static int getXzqhLevel(String xzqh) {
		// 空字符串
		if (xzqh == null || xzqh.length() == 0)
			return 0;
		// 非2的整数倍返回-1
		if (xzqh.length() % 2 > 0)
			return -1;

		String level = xzqh;
		while (level.endsWith("00")) {
			level = level.substring(0, level.length() - 2);
		}

		return level.length() / 2;
	}

	/**
	 * 补零
	 * 
	 * @param srcValue 原值
	 * @param length 补零后的长度
	 * @return
	 */
	public static String fillZero(int srcValue, int length) {
		String str = Integer.toString(srcValue);
		if (str.length() == length) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length - str.length(); i++) {
			sb.append("0");
		}
		return sb.toString() + str;
	}

	/**
	 * 李洪磊 2008-07-03 13:13 去掉换行符
	 * 
	 * @param _value 原值
	 * @return 去掉换行符后的值
	 */
	public static String base64BlankFilter(String _value) {
		if (_value == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < _value.length(); i++) {
			int _c = _value.charAt(i);
			if (_c != 10 && _c != 13) {
				sb.append(_value.substring(i, i + 1));
			}
		}
		return sb.toString();
	}

	/**
	 * 判断是否为指定长度
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static final boolean isLen(String str, int len) {
		if (isEmpty(str))
			return false;
		else if (str.length() == len)
			return true;
		else
			return false;
	}

	/**
	 * 李洪磊 add 2008-07-30 17:44 生成xml信息并传入response
	 * @param response
	 * @param text
	 * @param sign
	 */
	public static void createAjaxReturnXml(HttpServletResponse response, String[] text, String[] sign) {
		createAjaxReturnXml(response, text, sign, "");
	}

	/**
	 * 李洪磊 add 2008-07-30 17:44 生成xml信息并传入response
	 * @param response
	 * @param text
	 * @param sign
	 * @param oldCode
	 */
	public static void createAjaxReturnXml(HttpServletResponse response, String[] text, String[] sign, String oldCode) {
		StringBuffer retrunString = new StringBuffer("<jadl>");
		for (int i = 0; i < text.length; i++) {
			addXMLChild(retrunString, sign[i], text[i], true);
		}
		if (oldCode == null) {
			oldCode = "";
		}
		retrunString.append(oldCode);
		retrunString.append("</jadl>");
		PrintWriter out = null;
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(retrunString.toString().trim());
		retrunString = null;
		out.close();
	}

	/**
	 * 李洪磊 add 2008-07-30 17:44 将字符串传入response
	 * 
	 * @param response
	 * @param text
	 */
	public void createAjaxReturnMsg(HttpServletResponse response, String text) {
		PrintWriter out = null;
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(text);
		out.close();
	}

	/**
	 * 处理字符串中的特殊字符，目前只处理“'”
	 * 
	 * @param str
	 * @return
	 */
	public static String processSpecialChar(String str) {
		return str.replaceAll("'", "''");
	}

	/**
	 * 去除数组对象重复记录
	 * 
	 * @param objArray
	 * @return
	 */
	public static Object[] wipeOffRepeat(Object[] obj) {
		List list = new ArrayList();
		for (int i = 0; i < obj.length; i++) {
			list.add(obj[i]);
		}
		Set set = new HashSet(list);
		return set.toArray();
	}
	
	/**
	 * Description: 操作成功页面
	 * @param request
	 * @param msg：给用户的提示信息
	 * @param listMs：给用户操作的显示信息
	 * @param listUrl：给用户操作的显示信息的连接
	 * @param listTarget：给用户操作的显示信息连接位置(_self:在本页打开, _blank:在新页中打开)
	 */
	public static void success(HttpServletRequest request, String msg, List listMs, List listUrl, List listTarget) {
		List list = new ArrayList();
		for (int i=0; i<listMs.size(); i++) {
			Map map = new HashMap();
			map.put("ms", listMs.get(i));
			map.put("url", listUrl.get(i));
			map.put("target", listTarget.get(i));
			list.add(map);
		}
		request.getSession().setAttribute("successPageMsUrl", list);
		request.getSession().setAttribute("successPageMsg", msg);
	}
	
//	public static void main(String[] args) {
//		Object[] aa = (Object[]) StringUtils.wipeOffRepeat(new String[] {"a", "b", "c","a"});
//		for(int i=0; i<aa.length; i++) {
//			System.out.println(aa[i]);
//		}
//	}
	
	/**
	 * 判断是否是直辖市 11 北京 12 天津 31 上海 50 重庆
	 * @param xzqh
	 * @return
	 */
	public static boolean isZXS(String xzqh){
		if(xzqh.startsWith("11") || xzqh.startsWith("12") || xzqh.startsWith("31") || xzqh.startsWith("50")){
			return true;
		}else{
			return false;
		}
	} 
	/**
	 * 字符串排序(若字符串相联则显示String1至String2,如:01,02,03,04,05,显示为01至05) 
	 * @param list
	 * @return
	 */
	public static String compositorStr (String list){	
		String[] lists_tmp = list.split(",");
		String[] lists = new String[lists_tmp.length + 2];
		lists[0] = "-2";
		lists[lists.length - 1] = "-2";
		for (int i = 0; i < lists_tmp.length; i++) {
			lists[i + 1] = lists_tmp[i];
		}

		StringBuffer ls = new StringBuffer();
		String splitChar = "，";

		for (int p = 1; p < lists.length - 1; p++) {
			int pre = Integer.parseInt(lists[p - 1]);
			int after = Integer.parseInt(lists[p + 1]);
			int current = Integer.parseInt(lists[p]);
			if (current == (after - 1) && current == (pre + 1)) {
				continue;
			} else if (current == (after - 1)) {
				ls.append(intTo2String(current) + "~");
			} else if (current == (pre + 1)) {
				ls.append(intTo2String(current)).append(splitChar);
			} else {
				ls.append(intTo2String(current)).append(splitChar);
			}
		}

		return ls.toString();
	}
	
	private static String intTo2String(int t){
		String s = t + "";
		return s.length() <= 1 ? "0" + s : s;
	}
	
	
	/**
	 * 张方俊 2007-01-17 10:35
	 * 向xml中添加节点。如果有特殊字符，将采用<![CDATA[]]>方式添加
	 * @param sb 组合的字符串
	 * @param childName 节点的名称
	 * @param childValue 节点的值
	 * @param hasSpecialChar 是否需要添加<![CDATA[]]>处理
	 */
	public static void addXMLChild(StringBuffer sb, String childName , String childValue , boolean hasSpecialChar){
		sb.append("<");
		sb.append(childName);
		sb.append(">");
		if(hasSpecialChar){
			sb.append("<![CDATA[");
		}
		sb.append(childValue);
		if(hasSpecialChar){
			sb.append("]]>");
		}
		sb.append("</");
		sb.append(childName);
		sb.append(">");
	}
	
	
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
