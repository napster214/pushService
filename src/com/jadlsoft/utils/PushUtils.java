package com.jadlsoft.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 推送相关工具
 * @类名: MsgUtils
 * @作者: lcx
 * @时间: 2017-9-25 上午11:39:52
 */
public class PushUtils {

	private static Logger logger = Logger.getLogger(PushUtils.class);
	
	/**
	 * 从request中获取指定key的值，如果没有就返回空字符串
	 * @param request
	 * @param key
	 * @return: String
	 */
	public static String getStrFromReq(HttpServletRequest request, String key) {
		String parameter = request.getParameter(key);
		if (parameter == null) {
			parameter = "";
		}
		return parameter;
	}
	
	/**
	 * 返回结果统一工具
	 * @param response	response对象
	 * @param statusCode	返回状态码
	 * @param errmsg	说明信息
	 * @param paraInfo	其他参数【会将该map中的信息附加到返回的map中】
	 */
	public static void doResponse(HttpServletResponse response, String statusCode,
			String errmsg, Map paraInfo) {
		Map resultMap = new HashMap();
		resultMap.put("statusCode", statusCode);
		resultMap.put("errmsg", errmsg);
		if (paraInfo != null && paraInfo.size()>0) {
			Set entrySet = paraInfo.entrySet();
			for (Entry entry : (Set<Entry>)entrySet) {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		try {
			ResponseUtils.render(response, JsonUtil.map2json(resultMap));
		} catch (Exception e) {
			logger.error("返回结果出错！");
			e.printStackTrace();
		}
	}
	
}
