package com.jadlsoft.handler;

import java.util.Map;

import com.jadlsoft.constants.WeChatConstants;
import com.jadlsoft.manager.IWeChatSysManager;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.SpringBeanFactory;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.httpRequestProxy.HttpUtils;

/**
 * 公众号处理类
 * 	完成向微信后台调用接口实现功能的方式
 * @类名: GzhHandler
 * @作者: lcx
 * @时间: 2017-8-2 上午10:58:37
 */
public class GzhHandler {

	private String appNO;	//微信号
	private String appID;
	private String appsecret;
	
	private static IWeChatSysManager weChatSysManager = (IWeChatSysManager) SpringBeanFactory.getBean("weChatSysManager");
	
	//=======================================处理操作=======================================
	/**
	 * 获取accessToken
	 * @return: String
	 */
	public String getAccessToken() {
		String url = getRealUrl(WeChatConstants.SYS_GETACCESSTOKEN, false);
		String result = HttpUtils.doRequest(url, "GET", null);
		Map parserToMap = JsonUtil.parserToMap(result);
		return (String) parserToMap.get("access_token");
	}
	
	/**
	 * 校验公众号是否可用，appid、appsecret是否正确
	 * @return: String
	 */
	public boolean isGzhUsable() {
		String url = getRealUrl(WeChatConstants.SYS_GETACCESSTOKEN, false);
		String result = HttpUtils.doRequest(url, "GET", null);
		Map parserToMap = JsonUtil.parserToMap(result);
		if (StringUtils.isNullOrEmpty(parserToMap)) {
			return false;
		}
		Object object = parserToMap.get("access_token");
		if (StringUtils.isNullOrEmpty(object)) {
			//获取失败
//			if ("-1".equals(parserToMap.get("errcode"))) {
//				//属于服务器端繁忙，再试一次
//			}
			return false;
		}
		return true;
	}
	
	
	/*
	 * 消息管理**********************************************************************************
	 */
	
	/**
	 * 发送模板消息
	 * @param msgData	封装好的模板所需信息
	 * @return: void
	 */
	public String sendTemplateMsg(Map msgData) {
		String url = getRealUrl(WeChatConstants.MSG_SENDTEMPLATEMSG, true);
		String response = HttpUtils.doRequest(url, "POST", JsonUtil.map2json(msgData));
		return response;
	}
	
	/**
	 * 通过标签群发消息（好像只能在认证之后使用、没法测试，并且这是是有限制的，好像是一天只能向每个用户发送一条）
	 * @param msgData  接口所需信息
	 * @return: void
	 */
	public String sendMsgByTag(Map msgData) {
		String url = getRealUrl(WeChatConstants.MSG_SENDBYTAG, true);
		return HttpUtils.doRequest(url, "POST", JsonUtil.map2json(msgData));
	}
	
	/*
	 * 用户管理***********************************************************************
	 */
	/**
	 * @功能: 创建一个标签
	 * @param data 组装好的接口所需参数信息
	 * @return: String
	 */
	public String createTag(Map data) {
		String url = getRealUrl(WeChatConstants.USER_CREATETAG, true);
		return HttpUtils.doRequest(url, "POST", JsonUtil.map2json(data));
	}
	
	/**
	 * @功能: 获取指定标签下的所有粉丝信息
	 * @param data 组装好的接口所需参数信息
	 * @return: String
	 */
	public String getFansByTag(Map data) {
		String url = getRealUrl(WeChatConstants.USER_GETFANSONTAG, true);
		return HttpUtils.doRequest(url, "GET", JsonUtil.map2json(data));
	}
	
	/**
	 * @功能: 批量给用户加标签
	 * @param data 组装好的接口所需参数信息
	 * @return: String
	 */
	public String batchTagging(Map data) {
		String url = getRealUrl(WeChatConstants.USER_BATCHTAGGING, true);
		return HttpUtils.doRequest(url, "POST", JsonUtil.map2json(data));
	}
	
	/**
	 * @功能: 批量给用户去除标签
	 * @param data 组装好的接口所需参数信息
	 * @return: String
	 */
	public String batchUnTagging(Map data) {
		String url = getRealUrl(WeChatConstants.USER_BATCHUNTAGGING, true);
		return HttpUtils.doRequest(url, "POST", JsonUtil.map2json(data));
	}
	
	/*
	 * 二维码***************************************************************
	 */
	/**
	 * 创建带参临时二维码
	 * @param data
	 * @return: String
	 */
	public String createTempErweimaWithPara(String postData) {
		String url = getRealUrl(WeChatConstants.EWM_CREATEWITHPARA, true);
		return HttpUtils.doRequest(url, "POST", postData);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 获取URL地址
	 * @功能: 获取真正的URL地址
	 * @param url	URL地址
	 * @param useAccessToken	是否用到accessToken
	 * @return: String
	 */
	private String getRealUrl(String url, boolean useAccessToken) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		url = url.replace("${APPID}", appID)
				.replace("${APPNO}", appNO)
				.replace("${APPSECRET}", appsecret);
		if (useAccessToken) {
//			String accessToken = GzhHandler.getAccessTokenFromCache(appID);
			String accessToken = GzhHandler.getCurrentAccessToken(appID);
			if (accessToken == null) {
				accessToken = "";
			}
			url = url.replace("${ACCESS_TOKEN}", accessToken);
		}
		return url;
	}

	//===================================工具方法==========================================
	/*
	 * 从缓存中获取AccessToken
	 */
//	public static String getAccessTokenFromCache(String appid) {
//		return WeChatCache.getInstance().getAccessToken(appid);
//	}
	
	/*
	 * 获取当前的accessToken
	 */
	public static String getCurrentAccessToken(String appid) {
		return weChatSysManager.getCurrentAccessToken(appid);
	}
	
	//=================================获取、get/set=======================================
	public static GzhHandler getInstance(String appno, String appid, String appsecret) {
		GzhHandler gzhHandler = new GzhHandler(appno,appid,appsecret);
		return gzhHandler;
	}
	
	private GzhHandler(String appNO, String appID, String appsecret) {
		this.appNO = appNO;
		this.appID = appID;
		this.appsecret = appsecret;
	}
	
	public String getAppNO() {
		return appNO;
	}
	public void setAppNO(String appNO) {
		this.appNO = appNO;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	
}
