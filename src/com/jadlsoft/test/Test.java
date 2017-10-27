package com.jadlsoft.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.httpRequestProxy.HttpRequestProxy;

public class Test {
	
	private String appid = "wxfbd19e4f6f8796d9";
	private String appno = "gh_998b22f2adad";
	private String appsecret = "0b89e63850462ca8f72ad0468aa66424";
	
	private String tid_fwq = "qrS9s1gBj5sff-xWydWqudhVKQ5aNC78QkzyjFp11Pg";
	private String tid_yy = "2fCd-bbAVv1qHF6eIl9cxUoO_6-j-KqGBJAATNm4GMk";
	
	private String touser_0 = "oqXx50jlXG5nzOiKfW4NpuE5Noso";
	private String touser_1 = "oqXx50ra0u_5FZPNMlEq6WaIUSNY";
	
	private String url = "http://192.168.20.124:8080/dxfs/wechat/msgpush.do?method=mulitMsg";
	private String url_single = "http://192.168.20.124:8080/dxfs/wechat/msgpush.do?method=singleMsg";
	private String url_ewm = "http://192.168.20.124:8080/dxfs/wechat/wechat.do?method=getEwm";
	
	private void wechatPush() {
		/**
		 * 	appid、appno、appsecret
		 * 	List<Map> data	数据信息
		 * 		每一个Map代表一条待发送的消息：
		 * 			key 唯一标识，由调用方传递，如果传递有值，会随着返回结果返回，如果没有，就返回空字符串
		 * 			String template_id 模板id（通过微信开发平台获取）
		 * 			String touser 待发送粉丝的openID（需要调用方自己获取）
		 * 			Map data 模板数据
		 * 				一个key对应一个变量，value为对应值   如：{nickname:晓寒轻,fwqip:192.168.20.124}
		 */
		List<Map> data = new ArrayList<Map>();
		Map oneMap = new HashMap();
		Map twoMap = new HashMap();
		Map threeMap = new HashMap();
		
		oneMap.put("template_id", tid_fwq);
		oneMap.put("touser", touser_0);
		Map dataMap = new HashMap();
		dataMap.put("nickname", "李春晓");
		dataMap.put("yyname", "通用测试包");
		dataMap.put("fwqip", "192.168.20.124");
		dataMap.put("fwqdk", "8090");
		dataMap.put("ycxx", "就是出错了！");
		oneMap.put("data", JsonUtil.map2json(dataMap));
		
		twoMap.put("template_id", tid_fwq);
		twoMap.put("touser", touser_0);
		Map data2Map = new HashMap();
		data2Map.put("nickname", "晓寒轻");
		data2Map.put("yyname", "第二个测试包");
		data2Map.put("fwqip", "192.168.20.124");
		data2Map.put("fwqdk", "8090");
		data2Map.put("ycxx", "第二个也出错了！");
		twoMap.put("data", JsonUtil.map2json(data2Map));
		
		data.add(oneMap);
		data.add(twoMap);
		
		String dataStr = JsonUtil.list2json(data);
		Map paraMap = new HashMap();
		paraMap.put("data", dataStr);
		paraMap.put("appid", appid);
		paraMap.put("appno", appno);
		paraMap.put("appsecret", appsecret);
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			String resp = proxy.doRequest(url, paraMap, null, "UTF-8");
			Map toMap = JsonUtil.parserToMap(resp);
			System.out.println(toMap);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void json() {
		String msg = "";
		Map map = JsonUtil.parserToMap(msg);
		System.out.println(map);
	}
	
	private void singleMsg() {
		/**
		 * appid、appno、appsecret
		 * 	key 唯一标识，由调用方传递，如果传递有值，会随着返回结果返回，如果没有，就返回空字符串
		 * 	String template_id 模板id（通过微信开发平台获取）
		 * 	String touseres 待发送粉丝的openID，多个中间以逗号分隔（需要调用方自己获取）
		 * 	Map data 模板数据 
		 * 		一个key对应一个变量，value为对应值   如：{nickname:晓寒轻,fwqip:192.168.20.124}
		 */
		Map paraMap = new HashMap();
		paraMap.put("appid", appid);
		paraMap.put("appno", appno);
		paraMap.put("appsecret", appsecret);
		paraMap.put("key", "1234567890");
		paraMap.put("template_id", tid_fwq);
		paraMap.put("touseres", touser_0+","+touser_1);
		
		Map dataMap = new HashMap();
		dataMap.put("nickname", "李春晓");
		dataMap.put("yyname", "通用测试包");
		dataMap.put("fwqip", "192.168.20.124");
		dataMap.put("fwqdk", "8090");
		dataMap.put("ycxx", "就是出错了！");
		
		paraMap.put("data", JsonUtil.map2json(dataMap));
		
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			String resp = proxy.doRequest(url_single, paraMap, null, "UTF-8");
			Map toMap = JsonUtil.parserToMap(resp);
			System.out.println(toMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getEwm() {
		/**
		 * 基本信息：appid、appno、appsecret
		 * 		eventKey : 参数
		 * 		expireSeconds : 二维码有效期（单位/s，最长30天）
		 */
		String eventKey = "parameter";
		int expireSeconds = 10 * 60;
		Map dataMap = new HashMap();
//		dataMap.put("eventKey", eventKey);
		dataMap.put("expireSeconds", expireSeconds);
		
		dataMap.put("appid", appid);
		dataMap.put("appno", appno);
		dataMap.put("appsecret", appsecret);
		
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			String resp = proxy.doRequest(url_ewm, dataMap, null, "UTF-8");
			Map toMap = JsonUtil.parserToMap(resp);
			System.out.println(toMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Test t = new Test();
//		t.wechatPush();
//		t.json();
//		t.singleMsg();
		t.getEwm();
	}
}
