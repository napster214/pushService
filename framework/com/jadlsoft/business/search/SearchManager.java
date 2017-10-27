package com.jadlsoft.business.search;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.jadlsoft.business.BaseManager;

public class SearchManager extends BaseManager {

	public int getSearchCount(String sql) {
		return daoUtils.queryForInt("select count(*) from (" + sql + ")");
	}
	
	public List getSearchList(String sql, int skip, int pagesize){
		return daoUtils.find(sql, new HashMap(), skip, pagesize);
	}
	
	public List getSearchList(String sql) {
		return daoUtils.find(sql, Collections.EMPTY_MAP);
	}
}
