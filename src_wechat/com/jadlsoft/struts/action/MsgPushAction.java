package com.jadlsoft.struts.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.constants.WeChatConstants;
import com.jadlsoft.handler.GzhHandler;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.NullUtils;
import com.jadlsoft.utils.PushUtils;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.StringUtils;

public class MsgPushAction extends DispatchAction {
	
	/**
	 * 发送简单消息（向多个粉丝发送同一个模板并且相同的内容）
	 * 		要求参数：
	 * 			appid、appno、appsecret
	 * 			String template_id 模板id（通过微信开发平台获取）
	 * 			String touseres 待发送粉丝的openID，多个中间以逗号分隔（需要调用方自己获取）
	 * 			Map data 模板数据 
	 * 				一个key对应一个变量，value为对应值   如：{nickname:晓寒轻,fwqip:192.168.20.124}
	 * 						
	 * 		返回参数信息：
	 * 			{
	 *				"statusCode": 结果码,
	 *				"errMsg": 失败信息,
	 *				"erruseres": 发送失败的openid信息，多个中间以逗号分隔
	 *			}
	 * @return: void
	 */
	public void singleMsg(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		
		/**
		 * 1、接收数据
		 */
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		String dataStr = request.getParameter("data");
		String touseres = request.getParameter("touseres");
		String templateId = request.getParameter("template_id");
		
		/**
		 * 2、解析、校验数据
		 */
		
		if (StringUtils.isBlank(touseres) || StringUtils.isBlank(templateId)) {
			//缺失参数
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_ARGLOSS, "缺失参数", null);
			return;
		}
		Map dataMap = null;
		try {
			dataMap = JsonUtil.parserToMap(dataStr);
		} catch (Exception e) {
			//解析失败，数据格式错误
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_ARGERR, "模板数据格式错误", null);
			return;
		}
		
		/**
		 * 3、组装为微信所需参数，并且发送
		 */
		StringBuffer erruseres = new StringBuffer();	//最终失败的用户信息
		String[] touserArr = touseres.split(",");
		Map msgData;
		for (String touser : touserArr) {
			msgData = new HashMap();
			msgData.put("touser", touser);
			msgData.put("template_id", templateId);
			msgData.put("topcolor", "#FF0000");
			//设置变量值
			Map everyVarData;
			Map data = new HashMap();
			if (dataMap != null && dataMap.size()>0) {
				Set<Entry> entrySet = dataMap.entrySet();
				for (Entry entry : entrySet) {
					everyVarData = new HashMap();
					everyVarData.put("value", entry.getValue());
					everyVarData.put("color", WeChatConstants.MSG_TOPCOLOR);
					data.put(entry.getKey(), everyVarData);
				}
			}
			if (data.size()>0) {
				msgData.put("data", data);
			}
			
			//3.2 调用处理类发送
			String resp = GzhHandler.getInstance(appno, appid, appsecret).sendTemplateMsg(msgData);
			
			/**
			 * 4、解析数据
			 */
			Map map = JsonUtil.parserToMap(resp);
			if (NullUtils.isNullOrEmpty(map)) {
				//解析微信返回信息错误，跳过，作为失败返回
				erruseres.append(touser).append(",");
				continue;
			}
			if (!WeChatConstants.MSG_SUCCESS_STATUS.equals(map.get("errcode"))) {
				//失败，发送失败，将失败信息作为参数返回
				erruseres.append(touser).append(",");
				continue;
			}
		}
		
		/**
		 * 5、最终返回数据
		 */
		if (erruseres.length() == 0) {
			//全部成功
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_OK, "", null);
		}else {
			//有失败的
			String errs = erruseres.substring(0, erruseres.length()-1);
			Map map = new HashMap();
			map.put("erruseres", errs);
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_HASPUSHERR, "有发送失败信息", map);
		}
	}
	
	/**
	 * 发送复杂消息（给每个粉丝发送的内容可以不一样）
	 * 		要求参数：
	 * 			appid、appno、appsecret
	 * 			List<Map> data	数据信息
	 * 				每一个Map代表一条待发送的消息：
	 * 					key 唯一标识，由调用方传递，如果传递有值，会随着返回结果返回，如果没有，就返回空字符串
	 * 					String template_id 模板id（通过微信开发平台获取）
	 * 					String touser 待发送粉丝的openID（需要调用方自己获取）
	 * 					Map data 模板数据
	 * 						一个key对应一个变量，value为对应值   如：{nickname:晓寒轻,fwqip:192.168.20.124}
	 * 						
	 * 		返回参数信息：
	 * 			{
	 *				"statusCode": 结果码,
	 *				"errMsg": 失败信息,
	 *				"errinfo": 发送失败的信息{
	 *									"touser"	粉丝信息
	 *									"errcode"	失败码
	 *									"errmsg"	失败详情
	 *									"key"	key
	 *								}
	 *			}
	 * @return: void
	 */
	public void mulitMsg(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		
		/**
		 * 1、接收数据
		 */
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		String dataStr = request.getParameter("data");
		
		/**
		 * 2、解析数据
		 */
		List dataList = null;
		try {
			dataList = JsonUtil.parserToList(dataStr);
		} catch (Exception e) {
			//解析失败，数据格式错误
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_ARGERR, "数据格式错误", null);
			return;
		}
		if (dataList == null || dataList.size() == 0) {
			//数据格式错误
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_ARGERR, "数据格式错误", null);
			return;
		}
		
		List errtouseres = new ArrayList();	//发送失败的消息集合
		Map temMap;	//单个错误数据存储
		for (Map dataMap : (List<Map>)dataList) {
			/**
			 * 2.1、校验单个map信息
			 */
			String templateId = StringUtils.getStrFromObject(dataMap.get("template_id"));
			String touser = StringUtils.getStrFromObject(dataMap.get("touser"));
			String key = StringUtils.getStrFromObject(dataMap.get("key"));
			Map templateMap = (Map) dataMap.get("data");
			if (templateMap == null || templateMap.size()==0 ||
					StringUtils.isBlank(templateId) || StringUtils.isBlank(touser)) {
				//数据格式错误，跳过，作为失败返回
				temMap = getOneError(touser, key, SystemConstants.STATUSCODE_ERR_COM_ARGLOSS, "缺失参数", "");
				errtouseres.add(temMap);
				continue;
			}
			
			/**
			 * 3、整理数据并发送
			 */
			
			//3.1 整理数据为微信发送所需数据格式
			Map msgData = new HashMap();
			msgData.put("touser", touser);
			msgData.put("template_id", templateId);
			msgData.put("topcolor", "#FF0000");
			//设置变量值
			Map everyVarData;
			Map data = new HashMap();
			Set<Entry> entrySet = templateMap.entrySet();
			for (Entry entry : entrySet) {
				everyVarData = new HashMap();
				everyVarData.put("value", entry.getValue());
				everyVarData.put("color", WeChatConstants.MSG_TOPCOLOR);
				data.put(entry.getKey(), everyVarData);
			}
			if (data.size()>0) {
				msgData.put("data", data);
			}
			
			//3.2 调用处理类发送
			String resp = GzhHandler.getInstance(appno, appid, appsecret).sendTemplateMsg(msgData);
			
			/**
			 * 4、解析数据
			 */
			Map map = JsonUtil.parserToMap(resp);
			if (NullUtils.isNullOrEmpty(map)) {
				//解析微信返回信息错误，跳过，作为失败返回
				temMap = getOneError(touser, key, SystemConstants.STATUSCODE_ERR_WECHAT_PARSERESPERR, "系统解析出错，请联系后台服务管理人员！", null);
				errtouseres.add(temMap);
				continue;
			}
			if (!WeChatConstants.MSG_SUCCESS_STATUS.equals(map.get("errcode"))) {
				//失败，发送失败，将失败信息作为参数返回
				Map wechatErrcode = new HashMap();
				wechatErrcode.put("wechaterrcode", map.get("errcode"));
				wechatErrcode.put("wechaterrmsg", map.get("errmsg"));
				temMap = getOneError(touser, key, SystemConstants.STATUSCODE_ERR_WECHAT_RESPERRCODE,
						"微信接口调用出错，请！", JsonUtil.map2json(wechatErrcode));
				errtouseres.add(temMap);
				continue;
			}
		}
		
		/**
		 * 5、最终返回数据
		 */
		if (errtouseres.size() == 0) {
			//全部成功
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_OK, "", null);
		}else {
			//有失败的
			Map map = new HashMap();
			map.put("errinfo", errtouseres);
			PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_HASPUSHERR, "有发送失败信息", map);
		}
	}
	
	/*
	 * 组装单个失败的信息
	 */
	private Map getOneError(String touser, String key, String errcode, String errmsg, String args) {
		//数据格式错误，跳过，作为失败返回
		Map temMap = new HashMap();
		temMap.put("touser", touser == null ? "" : touser);
		temMap.put("key", key == null ? "" : key);
		temMap.put("errcode", errcode == null ? "" : errcode);
		temMap.put("errmsg", errmsg == null ? "" : errmsg);
		temMap.put("args", args == null ? "" : args);
		return temMap;
	}
	
}
