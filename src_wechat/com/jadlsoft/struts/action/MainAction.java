package com.jadlsoft.struts.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.jadlsoft.cache.WeChatCache;
import com.jadlsoft.constants.WeChatConstants;
import com.jadlsoft.manager.IWeChatSysManager;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.XMLUtils;
import com.jadlsoft.utils.httpRequestProxy.HttpRequestProxy;

public class MainAction extends DispatchAction {
	
	private IWeChatSysManager weChatSysManager;
	public void setWeChatSysManager(IWeChatSysManager weChatSysManager) {
		this.weChatSysManager = weChatSysManager;
	}

	/**
	 * 作为申请公众号以及接受用户交互时候的验证地址
	 * 	会走这个方法的情况：
	 * 		1、申请公众号时候接口配置
	 * 		2、用户主动跟公众号交互：
	 * 			2.1、用户主动发送消息给公众号
	 * 			2.2、触发事件：关注、取消关注、扫描二维码
	 * 
	 * 
	 * 	关于申请公众号的验证方式：
	 * 		验证时候，微信后台会发送如下参数：
	 * 			signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 * 			timestamp	时间戳
	 *			nonce	随机数
	 *			echostr	随机字符串
	 *		校验是否为微信后台发送的依据（防止其他人调用该接口）
	 *	 		1）将token、timestamp、nonce三个参数进行字典序排序
				2）将三个参数字符串拼接成一个字符串进行sha1加密
				3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public synchronized void authentication(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		/**
		 * 1、接收参数
		 */
		String echostr = request.getParameter("echostr");	//如果校验成功，将该值返回给微信后台
		String timestamp = request.getParameter("timestamp");  
		String nonce = request.getParameter("nonce");  
		String signature = request.getParameter("signature");  
		
		/**
		 * 2、判断是否为申请公众号的验证信息
		 */
		if (StringUtils.isBlank(echostr)) {
			//不是验证信息
			dealInteract(mapping, form, request, response);
			return;
		}
		
		/**
		 * 3、验证
		 */
		if (StringUtils.isBlank(echostr) || StringUtils.isBlank(timestamp) 
				|| StringUtils.isBlank(nonce) || StringUtils.isBlank(signature)) {
			//校验失败
			return;
		}
		
		//1、将token、timestamp、nonce三个参数进行字典序排序
		List<String> list = new ArrayList<String>();
		list.add(WeChatConstants.TOKEN_JADLSOFT);
		list.add(timestamp);
		list.add(nonce);
		Collections.sort(list);
		//2、sha1加密
		@SuppressWarnings("deprecation")
		String sign = DigestUtils.shaHex(list.get(0)+list.get(1)+list.get(2));
		//3、比较
		if (signature.equals(sign)) {
			//校验成功
			ResponseUtils.render(response, echostr);
		}
		return;
	}
	
	/**
	 * 处理用户交互的请求信息，将请求传递给调用方设置的回调地址
	 * 
	 * 	传递给调用方的数据信息格式如下：
	 * 		{
	 * 			FromUserName ： 用户的openID
	 * 			ToUserName ： 公众号的appno值 
	 * 			MsgType	： 消息类型（用户发送消息为text，交互事件为event）
	 * 			Content ： 用户发送消息内容（用户发送消息时候才有）
	 * 			Event	： 事件类型（交互事件才有）
	 * 			...具体详细请查看	https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140454
	 * 		}
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public void dealInteract(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		/**
		 * 1、处理短时间内重复请求（微信在处理一些请求的时候可能会在很短的时间内请求了多次，如扫描二维码，这里限制短时间内只有一次有效）
		 */
		long currentTimeMillis = System.currentTimeMillis();
		long lastTimeMillis = WeChatCache.lastDealMillis;
		if (currentTimeMillis - lastTimeMillis <= 1500) {
			ResponseUtils.render(response, "success");
			return;
		}else {
			WeChatCache.lastDealMillis = System.currentTimeMillis();
		}
		
		/**
		 * 2、接收、解析用户交互信息
		 */
		String xml = getStrFromReqIO(request);
		if (StringUtils.isBlank(xml)) {
			return;
		}
		Map map = XMLUtils.transXmlStringToMap(xml);
    	String openid = (String) map.get("FromUserName");
    	String appno = (String) map.get("ToUserName");
    	String msg = (String) map.get("Content");
		
    	/**
    	 * 3、调用配置的回调地址将请求返回给调用方
    	 */
    	Map gzhMap = weChatSysManager.getWithXtszByAppno(appno);
    	if (StringUtils.isNullOrEmpty(gzhMap)) {
    		log.error("未在库中查询到该公众号信息!");
		}
    	String respUrl = StringUtils.isNullOrEmpty(gzhMap.get("hddz")) ? "" : (String) gzhMap.get("hddz");
    	if (StringUtils.isBlank(respUrl)) {
			//没有配置回调地址
    		return;
		}
    	HttpRequestProxy proxy = new HttpRequestProxy();
    	String result;
    	try {
    		result = proxy.doRequest(respUrl, map, null, "UTF-8");
		} catch (Exception e) {
			result = "";
		}
    	
    	/**
    	 * 4、将结果信息返回给用户
    	 */
    	Map resultMap = new HashMap();
    	resultMap.put("ToUserName", XMLUtils.addZKH(openid));
    	resultMap.put("FromUserName", XMLUtils.addZKH(appno));
    	resultMap.put("CreateTime", map.get("CreateTime"));
    	resultMap.put("MsgType", XMLUtils.addZKH("text"));
    	resultMap.put("Content", XMLUtils.addZKH(result));
    	String respXml = XMLUtils.mapToXML(resultMap, false);
        try {  
        	ResponseUtils.render(response, respXml);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    	
	}
	
	/*
	 * 从request中接收IO传输信息
	 */
	private String getStrFromReqIO(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			is = request.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			String s = "";  
	        while ((s = br.readLine()) != null) {  
	            sb.append(s);  
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
