package com.jadlsoft.dbutils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author libanggui
 * 
 */
public class DicCache {
	private Map cache = new HashMap(); // List缓存

	private Map mapcache = new HashMap();// Map缓存

	private JdbcOperations jdbcTemplate = null; // Jdbc模板

	private boolean retrieved = false; // 是否已经刷新

	private Calendar lastRetrieved = null; // 最后刷新时间

	private String[] tableList; // 需要刷新数据表名列表

	private String keyColumn; // 字典表中的主键列

	private static DicCache instance = new DicCache();

	public DicCache() {
		instance = this;
	}
	
	public static DicCache getInstance() {
		return instance;
	}

	public Map getCache() {
		return cache;
	}

	/**
	 * @see com.jadlsoft.dbutils.DaoUtils#init()
	 *
	 */
	public void setCache() {
		if (keyColumn == null || tableList == null || tableList.length == 0)
			return;

		// 比较上次更新时间是否在一天以内，若是，不进行更新
//		Calendar cr = Calendar.getInstance(); 
//		cr.add(Calendar.DATE, -1);
//		if(retrieved && lastRetrieved != null && lastRetrieved.after(cr)) {
//			return; 
//		}
		 
		// 设置更新时间
		this.retrieved = true;
		this.lastRetrieved = Calendar.getInstance();
		// 更新数据
		RowMapper rowmapper = new LowcaseColumnRowMapper();
		for (int i = 0; i < tableList.length; i++) {
			cacheOneTable(rowmapper, tableList[i]);
		}
		/*
		 * //以下缓存用于功能菜单 cache.put("xtgnmenu", jt.query("SELECT * FROM t_dm_xtgn
		 * WHERE code LIKE '%00' ORDER BY code", rowmapper)); //以下缓存用于角色功能配置
		 * cache.put("xtgn1", jt.query("SELECT code,gn FROM t_dm_xtgn " + "WHERE
		 * code IN(SELECT gncode FROM t_wh_rolepermiss WHERE roleid=11) ORDER BY
		 * code", rowmapper)); cache.put("xtgn2", jt.query("SELECT code,gn FROM
		 * t_dm_xtgn " + "WHERE code IN(SELECT gncode FROM t_wh_rolepermiss
		 * WHERE roleid=12) ORDER BY code", rowmapper)); cache.put("xtgn3",
		 * jt.query("SELECT code,gn FROM t_dm_xtgn " + "WHERE code IN(SELECT
		 * gncode FROM t_wh_rolepermiss WHERE roleid=13) ORDER BY code",
		 * rowmapper));
		 */
	}
	
	public Map getMapcache() {
		return mapcache;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * @see com.jadlsoft.dbutils.DaoUtils#setTableList(String[])
	 * @param tableList
	 */
	public void setTableList(String[] tableList) {
		this.tableList = tableList;
	}

	public String[] getTableList() {
		return this.tableList;
	}
	
	/**
	 * @param rowmapper
	 * @param tableStr
	 */
	public void cacheOneTable(RowMapper rowmapper, String tableStr) {
		String[] diccfgs = tableStr.split("//");
		String tableName = null;
		String sql = null;
		String keycol = null;
		if (diccfgs.length > 2) {
			tableName = diccfgs[0];
			sql = "(" + diccfgs[1] + ")";
			keycol = diccfgs[2];
		} else {
			tableName = diccfgs[0];
			sql = tableName;
			keycol = keyColumn;
		}
		List datalist = jdbcTemplate.query("SELECT * FROM " + sql, rowmapper);
		cache.put(tableName, datalist); // 以list方式缓存数据
		
		Map tabledata = new LinkedHashMap();
		for (int j = 0; j < datalist.size(); j++) {
			Map data = (Map) datalist.get(j);
			tabledata.put(data.get(keycol), data);
		}
		mapcache.put(tableName, tabledata); // 以map方式缓存数据
	}

	/**
	 * 张方俊 2008-09-01 增加，为business层提供重新缓存的功能。
	 * 重新缓存指定的表。表名同application.xml中设定的表名，不区分大小写。
	 * 
	 * @param tableName 重新缓存的表名
	 */
	public void reCacheOneTable(String tableName) {
		if (tableName == null) {
			return;
		}
		for (int i = 0; i < tableList.length; i++) {
			String tableCacheString = tableList[i];	
			String _table_name = tableCacheString.indexOf("//") != -1 ? tableCacheString.split("//")[0] : tableCacheString;
			if (tableName.equalsIgnoreCase(_table_name)) {
				RowMapper rowmapper = new LowcaseColumnRowMapper();
				cacheOneTable(rowmapper, tableList[i]);
				return;
			}
		}
	}
}
