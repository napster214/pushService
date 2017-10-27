package com.jadlsoft.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.manager.IWeChatSysManager;
import com.jadlsoft.utils.PushUtils;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.SpringBeanFactory;
import com.jadlsoft.utils.StringUtils;

/**
 * 微信请求filter
 * @类名: WeChatServiceFilter
 * @描述: 拦截所有的微信服务的请求，判断该公众号信息是否已经绑定到该系统中
 * 		暂时处理：如果没有，就将该信息加入缓存中
 * @作者: lcx
 * @时间: 2017-8-2 下午1:32:23
 */
public class WeChatServiceFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chan) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		//处理中文乱码
		request.setCharacterEncoding("UTF-8");
		
		String servletPath = request.getServletPath();
		if (!servletPath.endsWith(".do")) {
			//排除非do结尾的
			chan.doFilter(request, arg1);
			return;
		}
		if ("/wechat/main.do".equals(servletPath)) {
			//排除主action地址请求
			chan.doFilter(request, arg1);
			return;
		}
		if (servletPath.startsWith("/wechat/wechatsys")) {
			//后台管理
			chan.doFilter(request, arg1);
			return;
		}
		
		String appno = request.getParameter("appno");
		String appid = request.getParameter("appid");
		String appsecret = request.getParameter("appsecret");
		if (StringUtils.isBlank(appno) || StringUtils.isBlank(appid) || StringUtils.isBlank(appsecret)) {
			try {
//				ResponseUtils.render(response, "appno、appid、appsecret不能为空!");
				PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_COM_ARGLOSS, "appno、appid、appsecret不能为空!", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		//判断库中是否有该信息，如果没有就提示先绑定！
		IWeChatSysManager weChatSysManager = (IWeChatSysManager) SpringBeanFactory.getBean("weChatSysManager");
		Map map = weChatSysManager.getWithXtszByAppid(appid);
		if (StringUtils.isNullOrEmpty(map)) {
			//不包含
			try {
//				ResponseUtils.render(response, "该公众号信息还未进行绑定！请先在<a href='../admin/index.jsp'>系统</a>中进行绑定！");
				PushUtils.doResponse(response, SystemConstants.STATUSCODE_ERR_WECHAT_NOTBIND, "该公众号信息还未进行绑定！请先前往项目根目录进行绑定！", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		//判断缓存中是否有该信息
		/*if (!WeChatCache.getInstance().getGzhCache().containsKey(appid)) {
			//不包含，就加进去
			Map gzhMap = new HashMap();
			gzhMap.put("appno", appno);
			gzhMap.put("appsecret", appsecret);
			gzhMap.put("appid", appid);
			gzhMap.put("lasttimemillis", System.currentTimeMillis());
			String accessToken = GzhHandler.getInstance(appno, appid, appsecret).getAccessToken();
			gzhMap.put("access_token", accessToken);
			WeChatCache.getInstance().addGzhCache(appid, gzhMap);
		}*/
		chan.doFilter(request, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
