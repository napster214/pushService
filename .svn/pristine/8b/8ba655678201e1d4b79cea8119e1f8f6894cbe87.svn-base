package com.jadlsoft.struts.action;

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

import com.jadlsoft.handler.GzhHandler;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.StringUtils;

public class UserAction extends DispatchAction {

	/**
	 * 创建标签
	 * 	 只是测试时候使用，正式的话这些操作在微信后台可以手动创建
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward createTag(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		
		String tagname = request.getParameter("tagname");
		
		Map data = new HashMap();
		Map tagMap = new HashMap();
		tagMap.put("name", tagname);
		
		data.put("tag", tagMap);
		
		String createTag = GzhHandler.getInstance(appno, appid, appsecret).createTag(data);
		ResponseUtils.render(response, createTag);
		return null;
	}
	
	
	/**
	 * 批量给用户加标签
	 * 	所需参数：
	 * 		基本信息：appid,appno,appsecret
	 * 		tagid :标签id
	 * 		openid_list :　用户openid集合
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward batchTagging(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		
		String tagid = request.getParameter("tagid");
		String openid_list = request.getParameter("openid_list");
		
		Map data = new HashMap();
		List oplist = JsonUtil.parserToList(openid_list);
		data.put("openid_list", oplist);
		
		data.put("tagid", tagid);
		
		String string = GzhHandler.getInstance(appno, appid, appsecret).batchTagging(data);
		ResponseUtils.render(response, string);
		return null;
	}
	
	/**
	 * 批量给用户移除标签
	 * 	所需参数：
	 * 		基本信息：appid,appno,appsecret
	 * 		tagid :标签id
	 * 		openid_list :　用户openid集合
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward batchUnTagging(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		
		String tagid = request.getParameter("tagid");
		String openid_list = request.getParameter("openid_list");
		
		Map data = new HashMap();
		List oplist = JsonUtil.parserToList(openid_list);
		data.put("openid_list", oplist);
		
		data.put("tagid", tagid);
		
		String string = GzhHandler.getInstance(appno, appid, appsecret).batchUnTagging(data);
		ResponseUtils.render(response, string);
		return null;
	}
}
