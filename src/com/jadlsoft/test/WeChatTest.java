package com.jadlsoft.test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.taglib.tiles.GetTag;

import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.httpRequestProxy.HttpRequestProxy;
import com.jadlsoft.utils.httpRequestProxy.HttpUtils;

public class WeChatTest {

	private String appid = "wxfbd19e4f6f8796d9";
	private String appno = "gh_998b22f2adad";
	private String appsecret = "0b89e63850462ca8f72ad0468aa66424";
	private String accessToken = "EQo37yEgU-hZg3SBMrX4m3oBqZHkBRbMBtYylRNdzcPV2aEYkyJSv2jxNNONnV9WQknVhD5yZDMduL6og68ZDt18gb1CHJqAOdi3SPs7W3XUT1IMoP-cayxDelN5tH6JXQCdAHAOHT";
	private String user_1 = "oqXx50ra0u_5FZPNMlEq6WaIUSNY";
	private String user_2 = "oqXx50nyuQ56TiKc6KlnVSqkOCtw";
	private String user_3 = "oqXx50jlXG5nzOiKfW4NpuE5Noso";
	
	private void testMenu() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=${ACCESS_TOKEN}";
		url = url.replace("${ACCESS_TOKEN}", accessToken);
		
		String string = HttpUtils.doRequest(url, "GET", "");
		System.out.println(string);
	}
	
	private void testSc() {
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=${ACCESS_TOKEN}";
		url = url.replace("${ACCESS_TOKEN}", accessToken);
		
		Map data = new HashMap();
		data.put("type", "image");
		data.put("offset", 0);
		data.put("count", 20);
		
		HttpUtils.doRequest(url, "POST", JsonUtil.map2json(data));
		
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			String string = proxy.doRequest(url, data, null, "UTF-8");
			System.out.println(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//测试二维码
	private void testErweima() {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=${ACCESS_TOKEN}";
		url = url.replace("${ACCESS_TOKEN}", accessToken);
		
		/*
		 * {"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
		 */
		
		Map data = new HashMap();
		data.put("expire_seconds", 5 * 60);
		data.put("action_name", "QR_STR_SCENE");
//		Map info = new HashMap();
//		info.put("requestMethod", "dealGzfz");
		data.put("action_info", "{\"scene\": {\"scene_str\": \"dealGzfz\"}}");
		String postData = "{\"expire_seconds\": 300, \"action_name\": \"QR_STR_SCENE\", " +
				"\"action_info\": {\"scene\": {\"scene_str\": \"checkUserVerCode&089324\"}},\"scene_id\":101}";
		data.put("scene_id", 110);
		
		String res = HttpUtils.doRequest(url, "POST", postData);
		System.out.println(res);
	}
	
	//获取二维码图片
	private void testGetErweima() {
		String ticket = "gQEa8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyRWJxUXBCRllkVmkxR2lXYjFwY2QAAgRmuYtZAwQsAQAA";
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${TICKET}";
		url = url.replace("${TICKET}", URLEncoder.encode(ticket));
		System.out.println(url);
//		String res = HttpUtils.doRequest(url, "GET", "");
//		System.out.println(res);
	}
	
	private void getTags() {
		String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=${ACCESS_TOKEN}";
		url = url.replace("${ACCESS_TOKEN}", accessToken);
		String res = HttpUtils.doRequest(url, "GET", "");
		System.out.println(res);
	}
	
	public static void main(String[] args) {
		WeChatTest test = new WeChatTest();
//		test.testSc();
//		test.testMenu();
//		test.testErweima();
//		test.testGetErweima();
		test.getTags();
	}
	
}
