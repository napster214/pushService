package com.jadlsoft.struts.action;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.constants.WeChatConstants;
import com.jadlsoft.handler.GzhHandler;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.PushUtils;
import com.jadlsoft.utils.StringUtils;

public class WeChatAction extends DispatchAction {
	
	/**
	 * 获取临时带参二维码功能
	 * 	所需参数：
	 * 		基本信息：appid、appno、appsecret
	 * 		eventKey : 参数
	 * 		expireSeconds : 二维码有效期（单位/s，最长30天）
	 * 
	 *  返回信息：
	 *  	{
	 *			"statusCode": 结果码,
	 *			"errMsg": 失败信息,
	 *			"ewminfo": 二维码信息{
	 *							ticket ： 唯一票据
	 *							ewmurl ： 二维码的URL地址
	 *						}
	 *		}
	 */
	public void getEwm(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		
		/**
		 * 1、接收参数、设置默认值
		 */
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		String eventKey = request.getParameter("eventKey");
		String expireSeconds = request.getParameter("expireSeconds");
		expireSeconds = StringUtils.isBlank(expireSeconds) ? String.valueOf(WeChatConstants.EWM_EXPIRESECONDS) : expireSeconds;
		
		/**
		 * 2、组装为微信所需参数并发送请求
		 */
		String postData = "{\"expire_seconds\": "+expireSeconds+", \"action_name\": \"QR_STR_SCENE\", " +
				"\"action_info\": {\"scene\": {\"scene_str\": \""+eventKey+"\"}},\"scene_id\":11}";
		GzhHandler handler = GzhHandler.getInstance(appno, appid, appsecret);
		String resp = handler.createTempErweimaWithPara(postData);

		/**
		 * 3、解析微信返回数据并组装为返回格式数据
		 */
		Map respMap;
		try {
			respMap = JsonUtil.parserToMap(resp);
		} catch (Exception e) {
			//微信返回数据错误
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_WECHAT_PARSERESPERR, "解析微信数据出错，请联系微信服务管理员", null);
			return;
		}
		if (respMap != null && respMap.containsKey("ticket") && !StringUtils.isNullOrEmpty(respMap.get("ticket"))) {
			Map ewminfo = new HashMap();
			String ticket = URLEncoder.encode((String)respMap.get("ticket"));
			String ewmUrl = WeChatConstants.EWM_GETWITHPARA.replace("${TICKET}", ticket);
			ewminfo.put("ticket", ticket);
			ewminfo.put("ewmurl", ewmUrl);
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_OK, "", ewminfo);
		}else {
			//说明生成失败！
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_WECHAT_RESPERRCODE, "微信生成二维码失败！", null);
		}
	}
	
}
