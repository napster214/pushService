package com.jadlsoft.business.login.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.business.login.ILoginManager;
import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.utils.StringUtils;

public class LoginManager extends BaseManager implements ILoginManager {

	private DaoUtils daoUtils;
	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}


	@Override
	public Map get(String userid, String password) {
		Map condition = new HashMap();
		condition.put("userid", userid);
		condition.put("password", password);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List find = daoUtils.find("#login.get", condition);
		if (StringUtils.isNullOrEmpty(find)) {
			return null;
		}
		return (Map) find.get(0);
	}
}
