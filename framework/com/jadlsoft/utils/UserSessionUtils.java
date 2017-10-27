package com.jadlsoft.utils;

import javax.servlet.http.HttpSession;

import com.jadlsoft.domain.UserSessionBean;

/**
 * 
 * @author 张方俊
 *
 */
public class UserSessionUtils {
	private UserSessionUtils(){}
	public static UserSessionBean getCurrentSessionUser(HttpSession session){
		return (UserSessionBean) session.getAttribute("userSessionBean");
	}
}
