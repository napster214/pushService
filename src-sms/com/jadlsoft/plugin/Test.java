package com.jadlsoft.plugin;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jadlsoft.utils.MD5Utils;
import com.jadlsoft.utils.httpRequestProxy.HttpRequestProxy;

public class Test {
	
	public static void main(String[] args) {
		//sysSendSms();
		getDxfsStatus("1710173515120547");
	}
	
	public static String  sysSendSms(){
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String url = "http://dx.ipyy.net/smsJson.aspx";
		Map postData = new HashMap();
		postData.put("userid", "jadl");
		postData.put("account", "AB00280");
		postData.put("password", MD5Utils.getMD5("AB0028020"));
		postData.put("mobile", "18600755757");
		postData.put("content", "【京安丹灵】感谢您使用个人安全中心，验证码为816859，请及时输入，并勿泄露给他人。");
		postData.put("sendTime", "");
		postData.put("action", "send");
		postData.put("extno", "");
		String str = null;
		try {
			str = httpRequestProxy.doRequest(url, postData, null, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = JSONObject.fromObject(str);
		String returnstatus = jsonObj.getString("returnstatus");
		String taskID = jsonObj.getString("taskID");
		String successCounts = jsonObj.getString("successCounts");
		System.out.println(str);
		return str ;
	}
	public static String getDxfsStatus(String taskID){
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String url = "http://dx.ipyy.net/statusJsonApi.aspx";
		Map postData = new HashMap();
		postData.put("userid", "jadl");
		postData.put("account", "AB00280");
		postData.put("password", MD5Utils.getMD5("AB0028020"));
		postData.put("statusNum", "");
		postData.put("action", "query");
		postData.put("taskid", taskID);
		String str = null;
		try {
			str = httpRequestProxy.doRequest(url, postData, null, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(str);
		return null;
	}
	
	public static String batchSms(){
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String url = "http://dx.ipyy.net/BatchSms.ashx";
		Map postData = new HashMap();
		postData.put("userid", "jadl");
		postData.put("account", "AB00280");
		postData.put("password", MD5Utils.getMD5("AB0028020"));
		postData.put("sendTime", "");
		postData.put("extno", "");
		
		String str = null;
		try {
			str = httpRequestProxy.doRequest(url, postData, null, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(str);
		return null;
	}
}
