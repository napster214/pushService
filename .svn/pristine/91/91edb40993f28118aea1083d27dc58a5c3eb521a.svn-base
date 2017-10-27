/**
 * com.jadlsoft.struts.action UserUtils.java Dec 26, 2007 9:31:28 AM
 */
package com.jadlsoft.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jadlsoft.domain.UserSessionBean;
import com.jadlsoft.utils.XzhqhUtils;

/**
 * @author sky 功能：
 * 
 */
public class UserUtils {
	/**
	 * getUserSessionBean() 功能：获取session中用户信息
	 * 
	 * @param request
	 * @return UserSessionBean
	 */
	public static UserSessionBean getUserSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		} else {
			return (UserSessionBean) session.getAttribute("userSessionBean");
		}
	}

	/**
	 * checkGN() 功能：判断用户对某模块是否有修改权限
	 * 
	 * @param userSessionBean
	 *            登录用户基本信息
	 * @param gnCode
	 *            被操作的功能代码
	 * @return boolean true:无修改权限 false:有修改权限
	 */
	public static boolean checkGN(UserSessionBean userSessionBean, String gnCode) {
		/*
		 * 目前未添加操作的检查
		 * 
		 * String[] gnCodes = userSessionBean.getModuleList(); for (int i = 0; i
		 * < gnCodes.length; i++) { if (gnCodes[i].equals(gnCode)) { return
		 * false; } }
		 */
		return true;
	}

	/**
	 * checkXzqh() 功能：判断用户是否为该行政区划下的用户
	 * 
	 * @param userSessionBean
	 *            登录用户基本信息
	 * @param xzqh
	 *            被操作记录所属行政区划
	 * @param type
	 *            验证类型 1：本行政区划只能操作本行政区划数据，本行政区划下的数据只能由本行政区划下的用户操作
	 *            2：上级可以操作下级数据，下级只能查看上级数据（省、市、县三级）
	 * @return boolean true:无修改权限 false:有修改权限
	 */
	public static boolean checkXzqh(UserSessionBean userSessionBean,
			String xzqh, int type) {
		boolean boolReturn = false;
		String userXzqh = userSessionBean.getXzqh();
		if (type == 1) {
			boolReturn = !userXzqh.equals(xzqh);
		} else if (type == 2) {
			boolReturn = !xzqh.startsWith(getShortXZQH(userXzqh));
		}
		return boolReturn;
	}

	/**
	 * getShortXZQH() 功能：
	 * 
	 * @param xzqh
	 * @return String
	 */
	private static String getShortXZQH(String xzqh) {
		return XzhqhUtils.getXZHQH(xzqh);
	}

	/**
	 * 全角转半角
	 * 
	 * @date 2016-4-12下午06:18:21 TODO
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}
}
