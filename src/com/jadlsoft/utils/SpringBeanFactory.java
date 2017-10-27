package com.jadlsoft.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.struts.DelegatingActionUtils;

public class SpringBeanFactory implements PlugIn {
	private static Logger logger = Logger.getLogger(SpringBeanFactory.class);
	private static ApplicationContext SpringBeanFactory_applicationContext;

	public static Object getBean(String beanName) {
		return SpringBeanFactory_applicationContext.getBean(beanName);
	}

	public void destroy() {
		SpringBeanFactory_applicationContext = null;
	}

	public void init(ActionServlet arg0, ModuleConfig arg1)
			throws ServletException {

		if (SpringBeanFactory_applicationContext == null) {
			SpringBeanFactory_applicationContext = DelegatingActionUtils
					.findRequiredWebApplicationContext(arg0, arg1);
		}
	}

	/**
	 * 2009年6月4日 张方俊 
	 * 初始化/WEB-INF/config文件夹下的spring配置文件。此方法为单元测试准备
	 */
	public void setApplicationContext() {
		if (SpringBeanFactory_applicationContext == null) {
			URL classUrl = SpringBeanFactory.class.getResource("");
			String path = classUrl.getPath();
			try {
				path = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("错误",e);
			}
			path = path.substring(0, path.indexOf("WEB-INF")) + "WEB-INF/";
			File configPath = new File(path + "config/");
			String[] applicationContexts = configPath
					.list(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							if (name.toLowerCase().startsWith(
									"applicationcontext")) {
								return true;
							}
							return false;
						}

					});
			for (int i = 0; i < applicationContexts.length; i++) {
				applicationContexts[i] = path + "config/"
						+ applicationContexts[i];
			}
			SpringBeanFactory_applicationContext = new FileSystemXmlApplicationContext(
					applicationContexts);
		}
	}

	public static void main(String[] args) {
		SpringBeanFactory s = new SpringBeanFactory();
		s.setApplicationContext();
	}

}
