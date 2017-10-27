package com.jadlsoft.filter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jadlsoft.domain.UserSessionBean;

/**
 * Example filter that sets the character encoding to be used in parsing the
 * incoming request
 */
public class PermissFilter implements Filter {
	// --------------properties-----------------------
	private String[] anonymousPath = null;

	private String loginurl = "/commons/promptlogin.htm";

	private String permissurl = "/commons/promptpermiss.htm";

	private String permissconfig = "/WEB-INF/permissconfig.xml";

	/*
	 * 张方俊 于 2008-08-13 增加数据权限控制
	 */
	private static Map gncode_data_permission = new HashMap();

	// --------------local variants-------------------
	private Document permissConf;

	private Map hmUrls = new HashMap();

	private static Logger log = Logger.getLogger(PermissFilter.class);

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.anonymousPath = null;
		this.loginurl = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String servletPath = ((HttpServletRequest) request).getServletPath();
		String contextpath = ((HttpServletRequest) request).getContextPath();

		if (servletPath == null || servletPath.length() <= 1) {
			chain.doFilter(request, response);
			return;
		} else {
			if (this.anonymousPath != null) {
				for (int i = 0; i < this.anonymousPath.length; i++)
					if (servletPath.startsWith(this.anonymousPath[i])) {
						chain.doFilter(request, response);
						return;
					}
			}
		}

		/*HttpSession session = ((HttpServletRequest) request).getSession(false);
		if (session == null || session.getAttribute("userSessionBean") == null) {
			((HttpServletResponse) response).sendRedirect(contextpath
					+ loginurl);
			return;
		}*/
		// 以下进行权限控制
		/*if (!isAccessable((HttpServletRequest) request,
				(UserSessionBean) session.getAttribute("userSessionBean"))) {
			((HttpServletResponse) response).sendRedirect(contextpath
					+ permissurl);
			return;
		} else {
			request.setAttribute("isforward", "true");
		}*/
		/**/
		// 传递控制到下一个过滤器
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String value = filterConfig.getInitParameter("anonymousPath");
		if (value != null && value.length() > 0) {
			this.anonymousPath = value.split(",");
		}
		value = filterConfig.getInitParameter("loginurl");
		if (value != null && value.length() > 0) {
			this.loginurl = value;
		}
		value = filterConfig.getInitParameter("permissurl");
		if (value != null && value.length() > 0) {
			this.permissurl = value;
		}
		value = filterConfig.getInitParameter("permissconfig");
		if (value != null && value.length() > 0) {
			this.permissconfig = value;
		}
		// this.permissconfig =
		// filterConfig.getServletContext().getRealPath(this.permissconfig);
		// 权限配置初始化
		// initPermissConfig(this.permissconfig);

		initPermissConfig(filterConfig.getServletContext().getRealPath(
				this.permissconfig));
	}

	private void initPermissConfig(String configfile) {
		SAXReader reader = new SAXReader(); // 读xml文件包
		FileInputStream in = null; // 文件输入流
		try {
			// 获得文件输入流
			in = new FileInputStream(configfile);
			// 将流读入doc中
			permissConf = reader.read(in);
			// 获得根元素
			Element eRoot = permissConf.getRootElement();
			// 得到子元素列表
			List ls = eRoot.elements();
			// 临时元素存放
			Element eTemp = null;
			// 循环获取元素
			for (int n = 0; n < ls.size(); n++) {
				// 存放第n个元素
				eTemp = (Element) ls.get(n);

				// 将元素存放进hashmap中
				hmUrls.put(eTemp.attributeValue("url"), eTemp);

				/*
				 * 张方俊 2008-08-13 将GNCODE与数据权限绑定。便于控制数据操作权限
				 */
				String managetype = eTemp.attributeValue("managetype");
				// if (managetype != null && !managetype.equals("") &&
				// !managetype.equals("0")) {
				if (managetype != null && !managetype.equals("")) {
					String gncode = eTemp.attributeValue("gncode");
					gncode_data_permission.put(gncode, managetype);
				}
			}
			// 日志信息提示
			log.info("系统权限参数初始化完毕！");
		} catch (Exception ex1) {
			// 日志信息提示
			log.error("系统权限参数初始化错误！", ex1);
		} finally {
			if (in != null) {// 判断流是否已经关闭
				try {// 异常处理
					in.close();// 关闭
				} catch (IOException e) {
					// 错误信息提示
					log.info(e.toString());
				}
			}
		}
	}

	/**
	 * Description: 是否可访问判断
	 * 
	 * @param request
	 * @param user
	 *            登录用户
	 * @return 可访问返回true 2006-10-19
	 */
	protected boolean isAccessable_qz(HttpServletRequest request,
			UserSessionBean user) {
		/*
		 * // 判断是否forward String isForward = (String)
		 * request.getAttribute("isforward"); if (isForward != null &&
		 * isForward.length() > 0) { return true; } String contextpath =
		 * ((HttpServletRequest) request).getContextPath();
		 * 
		 * String requestUri = ((HttpServletRequest) request).getRequestURI();
		 * String servletPath = requestUri.substring(contextpath.length()); //
		 * 验证用户是否为本行政区划下本派出所的用户，如不是则不允许访问 String userGajno = user.getGajno(); if
		 * (request.getAttribute("gajno") != null &&
		 * request.getAttribute("pcsno") != null) { String gajno = (String)
		 * request.getAttribute("gajno"); if (!userGajno.equals(gajno)) { return
		 * false; } } // 没有配置不允许访问 if (!hmUrls.containsKey(servletPath)) {
		 * String method = request.getParameter("method"); String type =
		 * request.getParameter("type"); if (method != null && method.length() >
		 * 0) { if (!hmUrls.containsKey(servletPath + "?method=" + method)) { if
		 * (type != null && type.length() > 0 && !hmUrls.containsKey(servletPath
		 * + "?type=" + type)) { log.info("URL没有配置：" + servletPath); return
		 * false; } else { return false;// servletPath += "?type=" + type; } }
		 * else { servletPath += "?method=" + method; } } else { if (type !=
		 * null && type.length() > 0) { servletPath += "?type=" + type; if
		 * (!hmUrls.containsKey(servletPath)) { log.info("URL没有配置：" +
		 * servletPath); return false; } } else { log.info("URL没有配置：" +
		 * servletPath); return false; } } }
		 * 
		 * Element etemp = (Element) hmUrls.get(servletPath); // 判断是否有功能权限
		 * String gncode = etemp.attributeValue("gncode"); if (gncode != null &&
		 * gncode.length() > 0) { boolean flag = false; for (int i = 0; i <
		 * user.getModuleList().length; i++) { String strTemp =
		 * user.getModuleList()[i]; if (strTemp.equals(gncode)) { flag = true;
		 * break; } } if (!flag) { log.info("URL没有权限访问，功能编号：" + gncode +
		 * ", URL: " + servletPath); return false; }
		 * request.setAttribute("gncode", gncode); // 设置功能编号，用于日志记录 } // 进行数据管理
		 * String managetype = etemp.attributeValue("managetype"); String
		 * paramtername = etemp.attributeValue("manageparam"); switch
		 * (managetype.charAt(0)) { case '0': // 没有数据管理要求 return true; case '1':
		 * // 属地管理 if (checkAccess(request, user, paramtername, true)) { return
		 * true; } break; case '2': // 上下级管理 if (checkAccess(request, user,
		 * paramtername, false)) { return true; } default: return false; }
		 * log.info("URL属地管理权限不匹配：" + servletPath); return false;
		 */
		return true;
	}

	private boolean isAccessable(HttpServletRequest request,
			UserSessionBean user) {
		// 判断是否forward
		String isForward = (String) request.getAttribute("isforward");
		if (isForward != null && isForward.length() > 0) {
			return true;
		}
		String contextpath = ((HttpServletRequest) request).getContextPath();
		String requestUri = ((HttpServletRequest) request).getRequestURI();
		String servletPath = requestUri.substring(contextpath.length());
		/*
		 * JS文件可直接访问
		 */
		if (requestUri.endsWith(".js")) {
			return true;
		}
		/*
		 * 系统使用的OCX控件包
		 */
		if (requestUri.toUpperCase().indexOf(".CAB") != -1) {
			return true;
		}

		/*
		 * 民爆部分的权限判断由以下部分组成： 1、URL的判断，即所有的URL必须在permissconfig.xml中配置
		 * 2、功能权限的判断，包括临时爆破作业是否有权限操作 3、数据权限的判断，主要是属地还是本地管理
		 */
		// 没有配置不允许访问
		if (!hmUrls.containsKey(servletPath)) {
			String method = request.getParameter("method");
			String type = request.getParameter("type");
			if (method != null && method.length() > 0) {
				if (!hmUrls.containsKey(servletPath + "?method=" + method)) {
					if (type != null
							&& type.length() > 0
							&& !hmUrls.containsKey(servletPath + "?type="
									+ type)) {
						log.info("URL没有配置：" + servletPath);
						return false;
					} else {
						return false;// servletPath += "?type=" + type;
					}
				} else {
					servletPath += "?method=" + method;
				}
			} else {
				if (type != null && type.length() > 0) {
					servletPath += "?type=" + type;
					if (!hmUrls.containsKey(servletPath)) {
						log.info("URL没有配置：" + servletPath);
						return false;
					}
				} else {
					log.info("URL没有配置：" + servletPath);
					return false;
				}
			}
		}
		/* */
		Element etemp = (Element) hmUrls.get(servletPath);
		// 判断是否有功能权限
		String gncode = etemp.attributeValue("gncode");
		if (gncode != null && gncode.length() > 0) {
			boolean flag = false;
			List gndmList = user.getGndm();
			for (int i = 0; i < gndmList.size(); i++) {
				String strTemp = (String) gndmList.get(i);
				if (strTemp.equals(gncode)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				log.info("URL没有权限访问，功能编号：" + gncode + ", URL: " + servletPath);
				return false;
			}
			request.setAttribute("gncode", gncode); // 设置功能编号，用于日志记录
		}
		// 进行数据管理
		String managetype = etemp.attributeValue("managetype");
		String paramtername = etemp.attributeValue("manageparam");
		switch (managetype.charAt(0)) {
		case '0': // 没有数据管理要求
			return true;
		case '1': // 本地管理
			if (checkAccess(request, user, paramtername, true)) {
				return true;
			}
			break;
		case '2': // 上下级管理
			if (checkAccess(request, user, paramtername, false)) {
				return true;
			}
		case '3': // 市局、省厅所有权限
			if (checkAccess(request, user, paramtername, false)
					&& user.getXzqh().endsWith("00")) {
				return true;
			}
		case '4': // 省厅所有权限
			if (user.getXzqh().endsWith("0000")) {
				return true;
			}
		default:
			return false;
		}
		log.info("URL属地管理权限不匹配：" + servletPath);
		return false;
	}

	/**
	 * Description: 数据管理校验
	 * 
	 * @param request
	 * @param user
	 *            登录用户
	 * @param paramtername
	 *            传递枪管单位的参数名称
	 * @param isStrict
	 *            是否属地管理，否为上下级管理
	 * @return 2006-10-19
	 */
	private boolean checkAccess(HttpServletRequest request,
			UserSessionBean user, String paramtername, boolean isStrict) {
		String param = paramtername; // 实际参数
		String subparam = null; // 列表查询中的子参数
		if (param.indexOf('[') > 0) { // 是列表查询中的查询参数组合字符串
			subparam = param.substring(param.indexOf('[') + 1,
					param.length() - 1);
			if (subparam.length() < 1) {
				return false;
			}
			param = param.substring(0, param.indexOf('['));
			if (param.length() == 0) {
				param = "queryparamter";
			}
		}
		// 获取参数值，没有参数时默认可访问
		String value = request.getParameter(param);
		if (value == null) {
			return true;
		}
		// 列表查询中的查询参数组合字符串，需要进行解析
		if (subparam != null) {
			value = "~" + value;
			if (value.indexOf("~" + subparam + "~") <= 0) {
				return true;
			}
			value = value.substring(value.indexOf("~" + subparam + "~")
					+ subparam.length() + 2);
			value = value.substring(0, value.indexOf('~'));
		}

		// 判断与登录用户相同还是上下级关系
		if (isStrict) {
			return user.getXzqh().equals(value.substring(0, 6));
		} else {
			return value.startsWith(getShortXZQH(user.getXzqh()));
		}
	}

	/**
	 * Description:
	 * 
	 * @param xzqh
	 *            行政区划
	 * @return 2007-1-8
	 */
	private String getShortXZQH(String xzqh) {
		if (xzqh.endsWith("0000")) {
			xzqh = xzqh.substring(0, 2);
		} else {
			if (xzqh.endsWith("00")) {
				xzqh = xzqh.substring(0, 4);
			}
		}
		return xzqh;
	}

	public static Map getDataPermissionMap() {
		return gncode_data_permission;
	}
}
