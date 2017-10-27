package com.jadlsoft.struts.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.domain.WeChatJbxxBean;
import com.jadlsoft.domain.WeChatXtszBean;
import com.jadlsoft.handler.GzhHandler;
import com.jadlsoft.manager.IWeChatSysManager;
import com.jadlsoft.struts.form.WeChatSysForm;
import com.jadlsoft.utils.BeanUtils;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.ResultBean;
import com.jadlsoft.utils.StringUtils;

/**
 * 微信后台管理action
 * @类名: WeChatSysAction
 * @作者: lcx
 * @时间: 2017-8-4 上午10:12:47
 */
public class WeChatSysAction extends DispatchAction {
	
	private IWeChatSysManager weChatSysManager;
	public void setWeChatSysManager(IWeChatSysManager weChatSysManager) {
		this.weChatSysManager = weChatSysManager;
	}

	/**
	 * 前往接入公众号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward accessGzh(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		return mapping.findForward("editwechat");
	}
	
	/**
	 * 前往修改公众号信息界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward editGzh(ActionMapping mapping, ActionForm form,
			   HttpServletRequest request, HttpServletResponse response)
			   throws Exception {
		String id = request.getParameter("id");
		if (!StringUtils.isBlank(id)) {
			
			Map wechatMap = weChatSysManager.getWithXtszByWxid(id);
			if (wechatMap != null) {
				request.setAttribute("wechat", wechatMap);
				return mapping.findForward("editwechat");
			}
		}
		return null;
	}
	
	/**
	 * 保存公众号信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward saveGzh(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WeChatSysForm weChatSysForm = (WeChatSysForm) form;
		
		WeChatJbxxBean bean = weChatSysForm.getWeChatJbxxBean();
		//校验唯一性
//		ResultBean checkUnique = checkUnique(bean.getGzhmc(), bean.getAppid(), bean.getAppno(), bean.getAppsecret());
		ResultBean checkUnique = new ResultBean(SystemConstants.RESULT_SUCCESS, "");
		if (SystemConstants.RESULT_SUCCESS.equals(checkUnique.getStatusCode())) {
			int save = weChatSysManager.save(weChatSysForm.getWeChatJbxxBean());
			if (save >0) {
				//保存系统设置bean
				WeChatXtszBean xtszBean = weChatSysForm.getWeChatXtszBean();
				xtszBean.setWxid(bean.getId());
				weChatSysManager.saveXtsz(xtszBean);
				
			}
			return mapping.findForward("wechatlist");
		}
		//校验失败！
		Map map = BeanUtils.beanToMap(bean);
		request.setAttribute("wechat", map);
		request.setAttribute("msg", checkUnique.getMsg());
		return mapping.findForward("editwechat");
	}
	
	/**
	 * 更新公众号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward udpateGzh(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WeChatSysForm weChatSysForm = (WeChatSysForm) form;
		
		WeChatJbxxBean bean = weChatSysForm.getWeChatJbxxBean();
		//校验唯一性
		//校验是否可用
		if (GzhHandler.getInstance(bean.getAppno(),bean.getAppid(), bean.getAppsecret()).isGzhUsable()) {
			int update = weChatSysManager.update(weChatSysForm.getWeChatJbxxBean());
			if (update>0) {
				//更新系统设置的
				WeChatXtszBean xtszBean = weChatSysForm.getWeChatXtszBean();
				weChatSysManager.updateXtsz(xtszBean, "hddz");
			}
			return mapping.findForward("wechatlist");
		}
		//校验失败！
		Map map = BeanUtils.beanToMap(bean);
		map.put("hddz", weChatSysForm.getWeChatXtszBean().getHddz());
		
//		Map wechatMap = weChatSysManager.getWithXtszByWxid(bean.getId());
		request.setAttribute("wechat", map);
		request.setAttribute("msg", "公众号不可用！请检查appid和appsecret是否正确，或者稍后再试");
		return mapping.findForward("editwechat");
	}
	
	/**
	 * 删除公众号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward deleteGzh(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		WeChatSysForm weChatSysForm = (WeChatSysForm) form;
		WeChatJbxxBean weChatJbxxBean = weChatSysForm.getWeChatJbxxBean();
		weChatJbxxBean.setZt(SystemConstants.ZT_FALSE);
		int update = weChatSysManager.update(weChatJbxxBean, "zt");
		if (update > 0) {
			//将对应的设置信息删掉
			int i = weChatSysManager.deleteXtszByWxid(weChatJbxxBean.getId());
		}
		return mapping.findForward("wechatlist");
	}
	
	/**
	 * 校验唯一性
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return: ActionForward
	 */
	/*private ResultBean checkUnique(String gzhmc, String appid, String appno, String appsecret) {
		//设置默认返回
		ResultBean resultBean = null;
		
		//非空校验
		if (StringUtils.isBlank(gzhmc)) {
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "公众号名称不能为空！");
			return resultBean;
		}
		if (StringUtils.isBlank(appid)) {
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "appid不能为空！");
			return resultBean;
		}
		if (StringUtils.isBlank(appno)) {
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "公众号微信号不能为空！");
			return resultBean;
		}
		if (StringUtils.isBlank(appsecret)) {
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "公众号秘钥不能为空！");
			return resultBean;
		}
		
		WeChatJbxxBean bean = weChatSysManager.getByGzhmc(gzhmc);
		if (bean != null) {
			//公众号名称重复
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "公众号名称已存在！");
			return resultBean;
		}
		
//		bean = weChatSysManager.getByGzhmc(gzhmc);
		Map beanMap = weChatSysManager.getWithXtszByAppid(appid);
		if (beanMap != null) {
			//公众号appid重复
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "该公众号信息已存在！");
			return resultBean;
		}
		
		//校验appid和appsecret是否正确
		if (!GzhHandler.getInstance(appno, appid, appsecret).isGzhUsable()) {
			//公众号不可用
			resultBean = new ResultBean(SystemConstants.RESULT_FAIL, "公众号不可用！请检查appid和appsecret是否正确，或者稍后再试");
			return resultBean;
		}
		resultBean = new ResultBean(SystemConstants.RESULT_SUCCESS, "");
		return resultBean;
	}*/
	
	
	/**
	 * 处理ajax，校验唯一性、可用性
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward checkUnique(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		String gzhmc = request.getParameter("gzhmc");
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		
		//设置默认返回
		ResultBean resultBean = null;
		
		//非空校验
		if (StringUtils.isBlank(gzhmc)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "公众号名称不能为空！");
		}
		if (StringUtils.isBlank(appid)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "appid不能为空！");
		}
		if (StringUtils.isBlank(appno)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "微信号不能为空！");
		}
		if (StringUtils.isBlank(appsecret)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "秘钥不能为空！");
		}
		
		WeChatJbxxBean bean = weChatSysManager.getByGzhmc(gzhmc);
		if (bean != null) {
			//公众号名称重复
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "公众号名称已存在！");
		}
		
		Map beanMap = weChatSysManager.getWithXtszByAppid(appid);
		if (beanMap != null) {
			//公众号appid重复
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "该公众号信息已存在！");
		}
		
		//校验appid和appsecret是否正确
		if (!GzhHandler.getInstance(appno, appid, appsecret).isGzhUsable()) {
			//公众号不可用
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "公众号不可用！请检查appid和appsecret是否正确，或者稍后再试");
		}
		
		return doResponse(resultBean, response, SystemConstants.RESULT_SUCCESS, "");
	}
	
	/**
	 * 处理ajax，校验公众号可用性
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return: ActionForward
	 */
	public ActionForward checkUseable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		String appid = request.getParameter("appid");
		String appno = request.getParameter("appno");
		String appsecret = request.getParameter("appsecret");
		
		//设置默认返回
		ResultBean resultBean = null;
		
		//非空校验
		if (StringUtils.isBlank(appid)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "appid不能为空！");
		}
		if (StringUtils.isBlank(appno)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "微信号不能为空！");
		}
		if (StringUtils.isBlank(appsecret)) {
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "秘钥不能为空！");
		}
		
		//校验appid和appsecret是否正确
		if (!GzhHandler.getInstance(appno, appid, appsecret).isGzhUsable()) {
			//公众号不可用
			return doResponse(resultBean, response, SystemConstants.RESULT_FAIL, "公众号不可用！请检查appid和appsecret是否正确，或者稍后再试");
		}
		
		return doResponse(resultBean, response, SystemConstants.RESULT_SUCCESS, "");
	}
	
	
	
	
	
	//====================================公用工具方法============================================
	private ActionForward doResponse(ResultBean resultBean, HttpServletResponse response,
			String statusCode, String msg) throws Exception {
		resultBean = new ResultBean(statusCode, msg);
		ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		return null;
	}
	
}
