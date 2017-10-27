/**
 * <p>Title: SetDatadicPlugin</p>
 * <p>Description: 设置数据字典插件</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-5
 */

package com.jadlsoft.plugin;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.jadlsoft.dbutils.DicCache;

public class SetDatadicPlugin implements PlugIn {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.PlugIn#destroy()
	 */
	public void destroy() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
	 */
	public void init(ActionServlet actionServlet, ModuleConfig mf) throws ServletException {
		Logger log = Logger.getLogger(SetDatadicPlugin.class);

//		log.info("系统开始设置数据字典缓存...");
//		WebApplicationContext wac = DelegatingActionUtils.findRequiredWebApplicationContext(actionServlet, mf);
//		if (wac == null) {
//			log.error("系统还没有加载Spring容器，无法进行数据字典缓存");
//			return;
//		}
//		actionServlet.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
//
//		DicCache dicCache = (DicCache) wac.getBean("dicCache", DicCache.class);

		if (actionServlet.getServletContext().getAttribute("dic") == null) {
			actionServlet.getServletContext().setAttribute("dic", DicCache.getInstance().getCache());
		}
		log.info("系统设置数据字典缓存完成");
	}
}
