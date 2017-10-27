package com.jadlsoft.business.login;

import java.util.Map;

public interface ILoginManager {

	/**
	 * 根据用户id和密码查询用户对象
	 * @param userid
	 * @param password
	 * @return: Map
	 */
	Map get(String userid, String password);
}
