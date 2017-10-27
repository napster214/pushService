package com.jadlsoft.dbutils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.jadlsoft.utils.ArrayUtils;
import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.DomainBeanUtils;
import com.jadlsoft.utils.IConstants;
import com.jadlsoft.utils.TripleDES_CRC;
import com.jadlsoft.utils.icutil.Base64;

/**
 * <p>Title: DaoUtils</p>
 * <p>Description: 数据处理类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 安丹灵</p>
 * @author Li Banggui
 * @version 1.0
 * May 19, 2008
 */

public class DaoUtils {

	public static Logger log = Logger.getLogger(DaoUtils.class);

	private DataSource dataSource;

	/**
	 * 1:在传统的SQL语句中，参数都是用'?'占位符来表示的。 NamedParameterJdbcTemplate类内部封装了一个普通的JdbcTemplate，
	 *   并作为其代理来完成大部分工作。
	 * 2:NamedParameterJdbcTemplate意义非凡，为JDBC查询提供了带命名参数的占位符，而不止是JDBC自己的“？”，
	 *   这样使用JDBC的时候，也可以很容易的构造出来带占位符的动态条件查询，而不是参数值带入方式的拼接SQL字符串了。
	 * 3:NamedParameterJdbcTemplate类是线程安全的，该类的最佳使用方式不是每次操作的时候实例化一个新的NamedParameterJdbcTemplate，
	 *   而是针对每个DataSource只配置一个NamedParameterJdbcTemplate实例（比如在Spring IoC容器中使用Spring IoC来进行配置），
	 *   然后在那些使用该类的DAO中共享该实例。
	 */
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private NativeJdbcExtractor nje;

	private boolean saveLog = false;

	private String dicConfigFile = "/dicconfig.xml";
	private Map columnArrayMap = new LinkedHashMap();;

	/**
	 * dataSource 数据源，设置时自动创建NamedParameterJdbcTemplate
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
	}

	/**
	 * Return the JDBC DataSource that this instance manages transactions for.
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * namedParameterJdbcTemplate 不需要赋值，字段从DataSource创建
	 */
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return this.namedParameterJdbcTemplate;
	}

	/**
	 * NativeJdbcExtractor 用于Oracle的LOB操作，若不涉及LOB操作，可不设置
	 */
	public void setNje(NativeJdbcExtractor nje) {
		this.nje = nje;
	}

	public NativeJdbcExtractor getNje() {
		return this.nje;
	}

	/**
	 * Description: 设置需要加载的字典
	 * @param tableList 字典数组
	 */
	public void setTableList(String[] tableList) {
		DicCache cache = DicCache.getInstance();
		cache.setTableList(tableList);
		cache.setJdbcTemplate(getJdbcTemplate());
	}

	//	public String[] getTableList() {
	//		return this.tableList;
	//	}

	/**
	 * NamedParameterJdbcTemplate类内部包装了一个标准的JdbcTemplate类。如果你需要访问其内部的JdbcTemplate实例
	 * （比如访问JdbcTemplate的一些方法）那么你需要使用getJdbcOperations()方法返回的JdbcOperations接口。
	 * （JdbcTemplate实现了JdbcOperations接口）。 
	 * Description: 根据namedParameterJdbcTemplate获取封装好的JdbcTemplate
	 * @return JdbcOperations(JdbcTemplate)
	 */
	public JdbcOperations getJdbcTemplate() {
		return getNamedParameterJdbcTemplate().getJdbcOperations();
	}

	/**
	 * Description: 设置字段缓存中的默认主键字段
	 * @param keyColumn 主键字段 May 19, 2008
	 */
	public void setKeyColumn(String keyColumn) {
		DicCache cache = DicCache.getInstance();
		cache.setKeyColumn(keyColumn);
	}

	/**
	 * saveLog 是否输出SQL语句，默认为否，目前仅实现更新操作（不保存Blob操作）的日志输出
	 */
	public void setSaveLog(boolean saveLog) {
		this.saveLog = saveLog;
	}

	/**
	 * Description: 设置SQL配置文件的位置（相对classpath）
	 * @param locates 文件名，多个文件使用逗号隔开
	 */
	public void setSqlLocate(String locates) {
		if (locates != null && locates.trim().length() > 0) {
			String[] locateArray = locates.split(",");
			for (int i = 0; i < locateArray.length; i++) {
				SqlMapConfig.loadConfig(locateArray[i]);
			}
		}
	}

	/**
	 * dicConfigFile 字典配置文件, 默认为classpath下的dicconfig.xml
	 */
	public void setDicConfigFile(String dicConfigFile) {
		if (dicConfigFile != null && dicConfigFile.trim().length() > 0) {
			this.dicConfigFile = dicConfigFile.startsWith("/") ? dicConfigFile
					: "/" + dicConfigFile;
		}
	}

	/**
	 * Description: 设置SQL配置文件的位置（相对classpath）
	 * @param locates 文件名，多个文件使用逗号隔开
	 */
	public void setCrcColumn(String columns) {
		if (columns != null && columns.trim().length() > 0) {
			String[] columnArray = columns.split(",");
			for (int i = 0; i < columnArray.length; i++) {
				this.columnArrayMap.put(columnArray[i], columnArray[i]);
			}
			DicMapLowcaseColumnRowMapper.setCrcColumnMap(columnArrayMap);
		}
	}

	/**
	 * spring中定义默认初始化方法，进行数据字典加载。（注意：此方法在此bean属性全部注入完成后调用）
	 * <bean id="daoUtils" class="com.jadlsoft.dbutils.DaoUtils" init-method="init">...</bean>
	 */
	public void init() {
		log.info("系统开始设置数据字典缓存...");
		DicCache.getInstance().setCache();
		URL url = DicMapLowcaseColumnRowMapper.class.getResource(dicConfigFile);
		String filePath = url.getFile();
		try {
			filePath = URLDecoder.decode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("获取字典表错误！");
			e.printStackTrace();
		}
		DicMapLowcaseColumnRowMapper.loadDicConfig(filePath, DicCache
				.getInstance().getMapcache());
		log.info("系统设置数据字典缓存完成");
	}

	//<!-------------------------------------------------------------------------------------------->

	/**
	 * BlobUtils 不需要用户创建、设置，系统会在需要时自动创建
	 */
	private BlobUtils blobUtils;

	public void setBlobUtils(BlobUtils blobUtils) {
		this.blobUtils = blobUtils;
	}

	public BlobUtils getBlobUtils() {
		return this.blobUtils;
	}

	/**
	 * Description: 刷新单个字典的缓存
	 * @param tableName 字典名称
	 */
	public void refreshTableCache(String tableName) {
		DicCache.getInstance().cacheOneTable(new LowcaseColumnRowMapper(),
				tableName);
	}

	// ------------------------------ 正式数据操作 ----------------------------------------
	/**
	 * Description: 进行数据更新操作
	 * @param sql SQL语句
	 * @param bean 参数
	 * @return 影响的行数
	 * May 19, 2008
	 */
	private int doExecuteStatement(String sql, Object bean) {
		int result = -1;
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		if (bean instanceof Map) {
			result = namedParameterJdbcTemplate.update(sql, (Map) bean);
		} else {
			bean = transBeanToCrc(bean,columnArrayMap);
			result = namedParameterJdbcTemplate.update(sql,
					new BeanPropertySqlParameterSource(bean));
		}
		if (saveLog) {
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	/**
	 * 记录SQL执行时间
	 * author 张方俊 2011-4-18 下午02:41:54
	 * @param startTime
	 */
	private void endRecordSqlExeTime(String sql,long startTime){
		if (saveLog) {
			long endTime = System.currentTimeMillis();
			long exeTime = (endTime - startTime);
			String starttime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
			.format(new Date(startTime)));
			String endtime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
			.format(new Date(endTime)));
			long minutes = exeTime / (60*1000);
			long minutes_ = exeTime % (60*1000);
			long seconds = minutes_ / 1000;
			long millisecond = exeTime - (minutes*60*1000) - seconds*1000;
			if(exeTime >= 10 * 1000){
				log.error("执行SQL2 = " + sql + "\n开始时间"+starttime+"\n结束时间"+endtime+"共执行：" + exeTime + "毫秒，合计"+minutes+"分钟"+seconds+"秒"+millisecond+"毫秒。执行时间较长！");
			}else{
				log.info("执行SQL2 = " + sql + "\n开始时间"+starttime+"\n结束时间"+endtime+"共执行：" + exeTime + "毫秒，合计"+minutes+"分钟"+seconds+"秒"+millisecond+"毫秒。");
			}
		}
	}

	// ----------------------------- 保存
	/**
	 * Description: 保存对象
	 * @param bean 对象
	 * @return 保存的行数
	 * May 19, 2008
	 */
	public int save(Object bean) {
		if (hasBlobField(bean)) {
			return saveBlob(bean);
		}
		String sql = SQLHelper.getInsertSQL(bean);
		return doExecuteStatement(sql, bean);
	}

	/**
	 * Description: 保存对象的部分字段
	 * @param bean 对象
	 * @param fields 要保存的字段，用逗号隔开
	 * @return 保存的行数
	 * May 19, 2008
	 */
	public int save(Object bean, String fields) {
		if (hasBlobField(bean, fields)) {
			return saveBlob(bean, fields);
		}
		String sql = SQLHelper.getInsertSQL(bean, fields.split(","));
		return doExecuteStatement(sql, bean);
	}

	/**
	 * Description: 保存Map数据
	 * @param data 数据，需要设置表名（key为DbConstants.DB_TABLENAME，value可以为String或者Class）
	 * @return 保存的行数
	 * May 19, 2008
	 */
	public int save(Map data) {
		String sql = SQLHelper.getInsertSQL(data);
		return doExecuteStatement(sql, data);
	}

	// ----------------------------- 更新
	/**
	 * Description: 更新对象
	 * @param bean 对象，需要设置表名和主键
	 * @return 更新的行数
	 * May 19, 2008
	 */
	public int update(Object bean) {
		if (hasBlobField(bean)) {
			/*
			 * ZFJ_ADVICE：修改BLOB
			 */
			//return updateBlob(bean);
			updateBlob(bean);
		}
		String sql = SQLHelper.getUpdateSQL(bean);
		return doExecuteStatement(sql, bean);
	}

	/**
	 * Description: 更新对象的部分字段
	 * @param bean 对象，需要设置表名和主键
	 * @param fields 需要更新的字段
	 * @return 影响的行数
	 * May 19, 2008
	 */
	public int update(Object bean, String fields) {
		if (hasBlobField(bean, fields)) {
			/*
			 * ZFJ_ADVICE：修改BLOB
			 */
			//return updateBlob(bean, fields);
			updateBlob(bean, fields);
		}
		String sql = SQLHelper.getUpdateSQL(bean, null, null, fields);
		return doExecuteStatement(sql, bean);
	}

	/**
	 * Description: 通过Map条件更新数据
	 * @param condition 条件---需要包括 1：表名（key = DbConstants.DB_TABLENAME, value = String 或 Class）
	 *                				  2：条件字段（key = DbConstants.DB_CONDITIONFIELDS, valu e= String(逗号隔开多个字段)）
	 * @return 影响的行数
	 * May 19, 2008
	 */
	public int update(Map condition) {
		String sql = SQLHelper.getUpdateSQL(condition);
		return doExecuteStatement(sql, condition);
	}

	//	 ----------------------------- 删除
	/**
	 * Description: 根据对象删除数据
	 * @param bean 对象，需要设置表名和主键
	 * @return 删除的行数 
	 * May 19, 2008
	 */
	public int delete(Object bean) {
		String sql = SQLHelper.getDeleteSQL(bean);
		return doExecuteStatement(sql, bean);
	}

	/**
	 * Description: 根据对象的某些字段条件删除数据
	 * @param bean 对象
	 * @param fields 条件字段
	 * @return 删除的行数 
	 * May 19, 2008
	 */
	public int delete(Object bean, String fields) {
		String sql = SQLHelper.getDeleteSQL(bean, fields);
		return doExecuteStatement(sql, bean);
	}

	/**
	 * Description: 根据Map条件删除数据
	 * @param condition 条件，需要设置表名
	 * @return 删除的行数 
	 * May 19, 2008
	 */
	public int delete(Map condition) {
		String sql = SQLHelper.getDeleteSQL(condition);
		return doExecuteStatement(sql, condition);
	}

	//	 ----------------------------- 查询 
	/**
	 * Description: 根据对象值查询数据
	 * @param bean 对象，将作为条件的属性设置正确的值
	 * @return 结果列表（List<Map<String,Object>>） 
	 * May 19, 2008
	 */
	public List find(Object bean) {
		String sql = SQLHelper.getSelectSQL(bean, SQLHelper
				.getExcuteTableName(bean));
		return getListWithDic(sql, new BeanPropertySqlParameterSource(bean));
	}

	/**
	 * Description: 根据Map条件查询数据
	 * @param condition Map条件，需要设置表名
	 * @return 结果列表（List<Map<String,Object>>） 
	 * May 19, 2008
	 */
	public List find(Map condition) {
		return find(condition, SQLHelper.getExcuteTableName(condition));
	}

	/**
	 * Description: 根据Class和Map条件查询数据
	 * @param clazz 决定表名的类
	 * @param condition 条件
	 * @return 结果列表 
	 * May 19, 2008
	 */
	public List find(Class clazz, Map condition) {
		String tableName = SQLHelper.getExcuteTableName(clazz);
		if (tableName == null || tableName.trim().length() == 0) {
			tableName = SQLHelper.getExcuteTableName(condition);
		}
		return find(condition, tableName);
	}

	/**
	 * Description: 根据表名和Map条件查询数据
	 * @param tableName 类名
	 * @param condition 条件
	 * @return 结果列表 
	 * May 19, 2008
	 */
	public List find(Map condition, String tableName) {
		String sql = SQLHelper.getSelectSQL(condition, tableName);
		return getListWithDic(sql, condition);
	}

	/**
	 * Description: 根据表名和List条件查询数据
	 * @param conditions List条件（List<Map<String,Object>>），Map使用CreateCondition方法生成
	 * @param tableName
	 * @return 
	 * May 19, 2008
	 */
	public List find(List conditions, String tableName) {
		String sql = SQLHelper.getSelectSQL(conditions, tableName);
		Map condition = getConditionsFromList(conditions);
		return getListWithDic(sql, condition);
	}

	/**
	 * Description: 获取分组数据
	 * @param conditions List条件
	 * @param tableName 表名
	 * @param groupColumns 分组字段
	 * @return 结果列表 
	 * May 19, 2008
	 */
	public List findByGroup(List conditions, String tableName,
			String groupColumns) {
		String sql = SQLHelper.getGroupSelectSQL(conditions, tableName,
				groupColumns);
		Map condition = getConditionsFromList(conditions);
		return getListWithDic(sql, condition);
	}

	/**
	 * Description: 分页数据查询
	 * @param condition Map条件
	 * @param skip 跳过行数（如每页10条，第3页，则为20）
	 * @param count 返回行数（每页行数）
	 * @return 结果列表 
	 * May 19, 2008
	 */
	public List find(Map condition, int skip, int count) {
		String tableName = SQLHelper.getExcuteTableName(condition);
		if (tableName != null && tableName.trim().length() > 0) {
			String sql = SQLHelper.getSelectSQL(condition, tableName);
			sql = SQLHelper.getListSqlFromSql(sql, skip, count);
			condition.put("page_maxcount", new Integer(skip + count));
			condition.put("page_skipcount", new Integer(skip));
			return getListWithDic(sql, condition);
		} else {
			return null;
		}
	}

	/**
	 * Description: 分页数据查询
	 * @param conditions List条件
	 * @param tableName 表名
	 * @param skip 跳过行数
	 * @param count 返回行数
	 * @return 结果列表 
	 * May 19, 2008
	 */
	public List find(List conditions, String tableName, int skip, int count) {
		String sql = SQLHelper.getSelectSQL(conditions, tableName);
		Map condition = getConditionsFromList(conditions);
		sql = SQLHelper.getListSqlFromSql(sql, skip, count);
		condition.put("page_maxcount", new Integer(skip + count));
		condition.put("page_skipcount", new Integer(skip));
		return getListWithDic(sql, condition);
	}

	/**
	 * Description: 通过对象条件查询结果数量
	 * @param bean 参数对象
	 * @return 数量 
	 * May 19, 2008
	 */
	public int getCount(Object bean) {
		String sql = SQLHelper.getConditionSQL(bean, SQLHelper.getExcuteTableName(bean));
		long startTime = 0;		
		if(saveLog){
			startTime = System.currentTimeMillis();
			log.info("执行SQL = " + "select count(*) " + sql);
		}
		int result = getNamedParameterJdbcTemplate().queryForInt("select count(*) " + sql,
				new BeanPropertySqlParameterSource(bean));
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	/**
	 * Description: 通过对象部分字段作为条件查询结果数量
	 * @param bean 参数对象
	 * @param fields 条件字段
	 * @return 数量 
	 * May 19, 2008
	 */
	public int getCount(Object bean, String fields) {
		fields = "," + fields + ",";
		Map condition = DomainBeanUtils.getPropertiesMap(bean);
		Iterator item = condition.keySet().iterator();
		while (item.hasNext()) {
			String key = (String) item.next();
			if (fields.indexOf("," + key + ",") < 0) {
				condition.remove(key);
			}
		}
		String sql = SQLHelper.getConditionSQL(condition, SQLHelper
				.getExcuteTableName(bean));
		return doExecuteGetCount("select count(*) " + sql, condition);
	}

	/**
	 * Description: 通过Map条件返回结果数量
	 * @param condition Map条件
	 * @return 结果数量 
	 * May 19, 2008
	 */
	public int getCount(Map condition) {
		String sql = SQLHelper.getConditionSQL(condition, SQLHelper
				.getExcuteTableName(condition));
		return doExecuteGetCount("select count(*) " + sql, makeCondition(condition));
	}

	/**
	 * Description: 通过类和Map条件返回结果数量
	 * @param clazz 决定表名的类
	 * @param condition 条件
	 * @return 结果数量 
	 * May 19, 2008
	 */
	public int getCount(Class clazz, Map condition) {
		String tableName = SQLHelper.getExcuteTableName(clazz);
		if (tableName == null || tableName.trim().length() == 0) {
			tableName = SQLHelper.getExcuteTableName(condition);
		}
		String sql = SQLHelper.getConditionSQL(condition, tableName);
		return doExecuteGetCount("select count(*) " + sql, makeCondition(condition));
	}

	/**
	 * Description: 通过List条件和表名查询结果数量
	 * @param conditions List条件
	 * @param tableName 表名
	 * @return 结果数量 
	 * May 19, 2008
	 */
	public int getCount(List conditions, String tableName) {
		String sql = SQLHelper.getConditionSQL(conditions, tableName);
		Map condition = getConditionsFromList(conditions);		
		return doExecuteGetCount("select count(*) " + sql, makeCondition(condition));
	}
	
	private int doExecuteGetCount(String sql , Map condition){
		long startTime = 0;		
		if(saveLog){
			startTime = System.currentTimeMillis();
			log.info("执行SQL = " + sql);
		}
		int result = getNamedParameterJdbcTemplate().queryForInt(sql, condition);
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}
	/**
	 * 张方俊
	 * Description: 通过类和Map条件查询对象
	 * @param clazz 返回结果类（决定查询表名）
	 * @param condition Map条件
	 * @return 对象
	 * @exception 当结果行数不为1时，取第一个值，如果未查询到结果，返回NULL
	 */
	public Object findObjectCompatibleNull(Class clazz, Map condition) {
		Object o = null;
		List list = find(clazz, condition);
		if (list == null || list.size() == 0) {
			return null;
		}
		Map map = (Map) list.get(0);
		try {
			o = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		DomainBeanUtils.populate(o, map);
		return o;
	}

	/**
	 * Description: 通过类和Map条件查询对象
	 * @param clazz 返回结果类（决定查询表名）
	 * @param condition Map条件
	 * @return 对象
	 * @exception 当结果行数不为1时，产生异常 
	 * May 19, 2008
	 */
	public Object findObject(Class clazz, Map condition) {
		String tableName = SQLHelper.getExcuteTableName(clazz);
		if (tableName == null || tableName.trim().length() == 0) {
			tableName = SQLHelper.getExcuteTableName(condition);
		}
		String sql = SQLHelper.getSelectSQL(condition, tableName);
		Object o = null;
		try {
			long startTime = 0;		
			if(saveLog){
				startTime = System.currentTimeMillis();
				log.info("执行SQL = " + sql);
			}
			
			Map map = namedParameterJdbcTemplate.queryForMap(sql,
					makeCondition(condition));
			
			if(saveLog){
				endRecordSqlExeTime(sql,startTime);
			}
			o = clazz.newInstance();
			DomainBeanUtils.populate(o, map);
		} catch (Exception e) {
			log.info("查询数据生成对象失败！对象" + clazz.getName(), e);
			throw new RuntimeException("查询数据生成对象失败！对象" + clazz.getName() + "\n" + e);
		}
		return transCrcToBean(o, columnArrayMap);
	}

	/**
	 * Description: 通过类和List条件查询对象
	 * @param conditions List条件
	 * @param clazz 返回结果类（决定查询表名）
	 * @return 对象
	 * @exception 当结果行数不为1时，产生异常 
	 * May 19, 2008
	 */
	public Object findObject(List conditions, Class clazz) {
		String tableName = SQLHelper.getExcuteTableName(clazz);
		String sql = SQLHelper.getSelectSQL(conditions, tableName);
		Map condition = getConditionsFromList(conditions);
		Object o = null;
		try {
			long startTime = 0;		
			if(saveLog){
				startTime = System.currentTimeMillis();
				log.info("执行SQL = " + sql);
			}
			
			Map map = namedParameterJdbcTemplate.queryForMap(sql,
					makeCondition(condition));
			
			if(saveLog){
				endRecordSqlExeTime(sql,startTime);
			}
			
			o = clazz.newInstance();
			DomainBeanUtils.populate(o, map);
		} catch (Exception e) {
			log.info("查询数据生成对象失败！表名：" + tableName, e);
			throw new RuntimeException("查询数据生成对象失败！表名：" + tableName + "\n" + e);
		}
		return o;
	}

	/**
	 * Description: 通过Map条件查询Map结果 
	 * @param condition Map条件
	 * @return Map对象，不对结果进行字典翻译，Key值为大写
	 * @exception 当结果行数不为1时，产生异常
	 * May 19, 2008
	 */
	public Map findMap(Map condition) {
		String tableName = SQLHelper.getExcuteTableName(condition);
		if (tableName == null || tableName.trim().length() == 0) {
			return null;
		}
		String sql = SQLHelper.getSelectSQL(condition, tableName);
		try {
			long startTime = 0;		
			if(saveLog){
				startTime = System.currentTimeMillis();
				log.info("执行SQL = " + sql);
			}
			
			Map result = namedParameterJdbcTemplate.queryForMap(sql,
					makeCondition(condition));
			
			if(saveLog){
				endRecordSqlExeTime(sql,startTime);
			}
			return result;
		} catch (Exception e) {
			log.info("查询数据生成Map失败！", e);
		}
		return null;
	}

	/**
	 * Description:通过List条件和表名查询Map结果
	 * @param conditions List条件
	 * @param tableName 表名
	 * @return Map对象，不对结果进行字典翻译，Key值为大写
	 * @exception 当结果行数不为1时，产生异常
	 * May 19, 2008
	 */
	public Map findMap(List conditions, String tableName) {
		if (tableName == null || tableName.trim().length() == 0) {
			return null;
		}
		String sql = SQLHelper.getSelectSQL(conditions, tableName);
		Map condition = getConditionsFromList(conditions);
		try {
			long startTime = 0;		
			if(saveLog){
				startTime = System.currentTimeMillis();
				log.info("执行SQL = " + sql);
			}
			
			Map result = namedParameterJdbcTemplate.queryForMap(sql,
					makeCondition(condition));
			
			if(saveLog){
				endRecordSqlExeTime(sql,startTime);
			}
			return result;
		} catch (Exception e) {
			log.info("查询数据生成Map失败！", e);
		}
		return null;
	}

	/**
	 * 通过Map条件和表名一次查询出结果条数和结果List，返回在Map中
	 * @param condition Map条件
	 * @param table_name 表名
	 * @return 数量和List，放置在Map中，key分别为total和list
	 * May 19, 2008
	 */
	public Map getCountAndList(Map condition, String table_name) {
		return doGetCountAndListFromSql(SQLHelper.getConditionSQL(condition,
				table_name), makeCondition(condition));
	}

	/**
	 * 通过List条件和表名一次查询出结果条数和结果List，返回在Map中
	 * @param conditions List条件
	 * @param table_name 表名
	 * @return 数量和List，放置在Map中，key分别为total和list
	 * May 19, 2008
	 */
	public Map getCountAndList(List conditions, String table_name) {
		String sql = SQLHelper.getConditionSQL(conditions, table_name);
		Map condition = getConditionsFromList(conditions);
		return doGetCountAndListFromSql(sql, makeCondition(condition));
	}

	/**
	 * 通过Map条件一次查询出结果条数和一页结果List，返回在Map中
	 * @param condition Map条件
	 * @param skip 跳过条数
	 * @param count 返回条数
	 * @return
	 * May 19, 2008
	 */
	public Map getCountAndList(Map condition, int skip, int count) {
		return doGetCountAndListFromSql(SQLHelper.getConditionSQL(condition,
				SQLHelper.getExcuteTableName(condition)),
				makeCondition(condition), skip, count);
	}

	/**
	 * 通过Map条件和表名一次查询出结果条数和一页结果List，返回在Map中
	 * @param condition Map条件
	 * @param table_name 表名
	 * @param skip 跳过条数
	 * @param count 返回条数
	 * @return
	 * May 19, 2008
	 */
	public Map getCountAndList(Map condition, String table_name, int skip,
			int count) {
		return doGetCountAndListFromSql(SQLHelper.getConditionSQL(condition,
				table_name), makeCondition(condition), skip, count);
	}

	/**
	 * 通过List条件和表名一次查询出结果条数和一页结果List，返回在Map中
	 * @param conditions List条件
	 * @param table_name 表名
	 * @param skip 跳过条数
	 * @param count 返回条数
	 * @return
	 * May 19, 2008
	 */
	public Map getCountAndList(List conditions, String table_name, int skip,
			int count) {
		String sql = SQLHelper.getConditionSQL(conditions, table_name);
		Map condition = getConditionsFromList(conditions);
		return doGetCountAndListFromSql(sql, makeCondition(condition), skip,
				count);
	}

	/**
	 * 张方俊 2008-09-23 根据指定条件查询记录列表
	 * @param conditions
	 * @param table_name
	 * @param skip
	 * @param count
	 * @param total
	 * @return
	 */
	public Map getList(List conditions, String table_name, int skip, int count,
			int total) {
		String sql = SQLHelper.getConditionSQL(conditions, table_name);
		Map condition = getConditionsFromList(conditions);
		transQueryCondition(condition);
		return doGetListFromSql(sql, makeCondition(condition), skip, count,
				total);
	}

	/**
	 * 张方俊 2008-09-23 仅查询一次记录总数。第一次查询时，total_ = -1，即记录总数未知
	 * @param sql
	 * @param condition
	 * @param skip
	 * @param count
	 * @param total_
	 * @return
	 */
	private Map doGetListFromSql(String sql, Map condition, int skip,
			int count, int total_) {
		Map map = new HashMap();
		int total = total_;
		if (total_ == -1) {
			total = count;
		}
		if (skip >= total || skip < 0) {
			skip = total - (total % count);
			if (skip == total && skip > 0) {
				skip = total - count;
			}
		}
		sql = SQLHelper.getListSqlFromSql("select * " + sql, skip, count);
		condition.put("page_maxcount", new Integer(skip + count));
		condition.put("page_skipcount", new Integer(skip));

		//张方俊 2010-12-13 增加查询时间统计功能
		long startTime = 0;
		if (saveLog) {
			startTime = System.currentTimeMillis();
			log.info("执行SQL = " + sql);
		}
		
		List list = getListWithDic(sql, condition);
		
		//张方俊 2010-12-13 增加查询时间统计功能
		if (saveLog) {
			endRecordSqlExeTime(sql,startTime);
		}

		map.put("total", new Integer(total_));
		map.put("skip", new Integer(skip));

		// 返回结果集 list[map{}] --- [{dwdm=2101034300004, ..., rq=2008-07-22}]
		map.put("list", list);
		return map;
	}

	/**
	 * 张方俊 2008-09-23
	 * @param sql
	 * @param condition
	 * @param skip
	 * @param count
	 * @param total_
	 * @return
	 */
	public Map getDataCount(List conditions, String table_name, int skip,
			int count, int total) {
		String sql = SQLHelper.getConditionSQL(conditions, table_name);
		Map condition = getConditionsFromList(conditions);
		return doGetCountFromSql(sql, makeCondition(condition), skip, count,
				total);
	}

	/*
	 * ZFJ
	 */
	private Map doGetCountFromSql(String sql, Map condition, int skip,
			int count, int total_) {
		
		//张方俊 2010-12-13 增加查询时间统计功能
		long startTime = 0;
		if (saveLog) {
			startTime = System.currentTimeMillis();
			log.info("执行SQL = " + sql);
		}
		
		//count时，sql语句中的order by *** 影响查询效率，在此剔除sql中所有的order by ***
		/**moved by zhaohuibin 2011-12-13 此处替换会造成包含RANK () OVER (ORDER BY ***)、
		 * ROW_NUMBER () OVER (ORDER BY ***)或ORDER BY count(*) 的语句报错
		if(sql.indexOf("OVER (ORDER BY") < 0){//除RANK () OVER (ORDER BY ***) 和 ROW_NUMBER () OVER (ORDER BY ***)
			sql = sql.replaceAll("(?i)ORDER\\s{1,}BY[^\\)]{0,}\\s{1,}WITH\\s{1,}", " WITH ").replaceAll("(?i)ORDER\\s{1,}BY[^\\)]{1,}\\)", ")").replaceAll("(?i)ORDER\\s{1,}BY.{1,}", " ");
		}*/
		int total = getNamedParameterJdbcTemplate().queryForInt(
				"select count(*) " + sql, makeCondition(condition));
		
		//张方俊 2010-12-13 增加查询时间统计功能
		if (saveLog) {
			endRecordSqlExeTime(sql,startTime);
		}
		
		if (skip >= total || skip < 0) {
			skip = total - (total % count);
			if (skip == total && skip > 0) {
				skip = total - count;
			}
		}
		Map map = new HashMap();
		map.put("total", new Integer(total));
		map.put("skip", new Integer(skip));
		return map;
	}

	private Map doGetCountAndListFromSql(String sql, Map condition) {
		Map map = new HashMap();
		int count = getNamedParameterJdbcTemplate().queryForInt(
				"select count(*) " + sql, makeCondition(condition));
		List list = getListWithDic("select * " + sql, condition);

		map.put("total", new Integer(count));
		map.put("list", list);
		return map;
	}

	private Map doGetCountAndListFromSql(String sql, Map condition, int skip,
			int count) {
		Map map = new HashMap();

		int total = getNamedParameterJdbcTemplate().queryForInt(
				"select count(*) " + sql, makeCondition(condition));
		if (skip >= total || skip < 0) {
			skip = total - (total % count);
			if (skip == total && skip > 0) {
				skip = total - count;
			}
		}
		sql = SQLHelper.getListSqlFromSql("select * " + sql, skip, count);
		condition.put("page_maxcount", new Integer(skip + count));
		condition.put("page_skipcount", new Integer(skip));

		List list = getListWithDic(sql, condition);

		map.put("total", new Integer(total));
		map.put("skip", new Integer(skip));

		// 返回结果集 list[map{}] --- [{dwdm=2101034300004, ..., rq=2008-07-22}]
		map.put("list", list);
		return map;
	}

	/**
	 * 查询结果，并自动翻译字典
	 * @param sql
	 * @param paramSource
	 * @return List
	 * May 19, 2008
	 */
	private List getListWithDic(String sql, SqlParameterSource paramSource) {
		List result;
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		result = namedParameterJdbcTemplate.query(sql, paramSource,
				new DicMapLowcaseColumnRowMapper());
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	/**
	 * 查询结果，并自动翻译字典
	 * @param sql SQL语句
	 * @param condition 条件对象
	 * @return List
	 * May 19, 2008
	 */
	private List getListWithDic(String sql, Map condition) {
		return getListWithDic(sql, new MapSqlParameterSource(
				makeCondition(condition)));
	}

	/**
	 * 从List条件获取Map条件，用于查询 
	 * @param conditions List条件
	 * @return Map条件
	 * May 19, 2008
	 */
	private Map getConditionsFromList(List conditions) {
		Map cond = new HashMap();
		try {
			Iterator item = conditions.iterator();
			while (item.hasNext()) {
				Map condition = (Map) item.next();
				String colname = (String) condition.get("column");
				String colnamerel = colname;
				if (cond.containsKey(colnamerel)) {
					for (int i = 0; i < 100; i++) {
						if (!cond.containsKey(colname + "_" + i + "_")) {
							colnamerel = colname + "_" + i + "_";
							break;
						}
					}
				}
				/**
				// String colvalue = (String) condition.get("value");
				// if (colvalue.indexOf(',') > 0) {
				// colvalue = colvalue.substring(0, colvalue.indexOf(','));
				// }
				// cond.put(colnamerel, colvalue);
				cond.put(colnamerel, condition.get("value"));*/
				/*modify by zhaohuibin 2011-05-10 解决页面查询条件为or时的处理，页面输入格式“字段名~操作符,操作符~值,值”，其中操作符和值一一对应
				* modify by zhaohuibin 2011-12-01 解决页面查询条件为or时的处理，页面输入格式“字段名1,字段名2...~操作符1,操作符2...~值1,值2...”，其中字段名、操作符和值一一对应
				* modify by zhaohuibin 2012-06-07 解决字段名中某两个一样的问题，页面输入格式“字段名1,字段名1,字段名2...~操作符1,操作符2,操作符3...~值1,值2,值3...”，其中字段名、操作符和值一一对应
				* */
				String colvalue = (String) condition.get("value");
				if (colvalue.indexOf(',') > 0) {
					if (colnamerel.indexOf(',') > 0) {
						String[] colvalues = colvalue.split(",");
						String[] colnamerels = colnamerel.split(",");
						for(int i = 0; i < colvalues.length; i++) {
							if (!cond.containsKey(colnamerels[i])) {
								cond.put(colnamerels[i], colvalues[i]);
							}else{
								cond.put(colnamerels[i] + "_", colvalues[i]);
							}
						}
					}else{
						String[] colvalues = colvalue.split(",");
						for(int i = 0; i < colvalues.length; i++) {
							if (!cond.containsKey(colname + "_" + i + "_")) {
								if(i!=0){
									colnamerel = colname + "_" + i + "_";
								}
								cond.put(colnamerel, colvalues[i]);
							}
						}						
					}
				}else{
					cond.put(colnamerel, colvalue);
				}
			}
		} catch (Exception e) {
		}
		return cond;
	}

	/**
	 * 根据字段、操作符、参数值创建条件Map 
	 * @param column 字段
	 * @param operation 操作符
	 * @param value 参数值
	 * @return 单个条件Map
	 * May 19, 2008
	 */
	public static Map createCondition(String column, String operation,
			Object value) {
		Map condition = new HashMap();
		condition.put("column", column);
		condition.put("operation", operation);
		condition.put("value", value);
		return condition;
	}

	/**
	 * 从序列取值 
	 * @param seqName 序列名称
	 * @return 序列的Nextval
	 * May 19, 2008
	 */
	public int getNextval(String seqName) {
		String sql = SQLHelper.getSequencesSQL(seqName);
		int val = getJdbcTemplate().queryForInt(sql);
		return val;
	}

	/**
	 * 通过Map条件和字段名（可以是表达式）查询整数值 
	 * @param condition Map条件
	 * @param field 字段/表达式
	 * @return 整数
	 * @exception 当结果数量不是1行时抛出异常
	 * May 21, 2008
	 */
	public int queryForInt(Map condition, String field) {
		if (field == null || field.trim().length() == 0
				|| field.trim().equals("*") || field.trim().indexOf(',') > 0) {
			return 0;
		}
		
		String sql = SQLHelper.getConditionSQL(condition, SQLHelper
				.getExcuteTableName(condition));
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + ("select " + field + " " + sql));
			startTime = System.currentTimeMillis();
		}
		int result = getNamedParameterJdbcTemplate().queryForInt(
				"select " + field + " " + sql, makeCondition(condition));
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		
		return result;
	}

	/**
	 * 通过Map条件和字段名查询返回指定的对象
	 * @param condition Map条件
	 * @param field 字段
	 * @param clazz 返回的类
	 * @return 对象
	 * @exception 当结果数量不是1行时抛出异常
	 * May 21, 2008
	 */
	public Object queryForObject(Map condition, String field, Class clazz) {
		if (field == null || field.trim().length() == 0
				|| field.trim().equals("*") || field.trim().indexOf(',') > 0) {
			return null;
		}
		
		String sql = SQLHelper.getConditionSQL(condition, SQLHelper
				.getExcuteTableName(condition));
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + ("select " + field + " " + sql));
			startTime = System.currentTimeMillis();
		}
		
		Object result = getNamedParameterJdbcTemplate().queryForObject(
				"select " + field + " " + sql, makeCondition(condition), clazz);
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		
		return result;
	}

	/**
	 * 通过Map条件查询指定字段（可以时多个）返回结果Map 
	 * @param condition Map条件
	 * @param field 字段（表达式），用逗号隔开
	 * @return 结果Map，不对字典进行翻译
	 * @exception 当结果数量不是1行时抛出异常
	 * May 21, 2008
	 */
	public Map queryForMap(Map condition, String field) {
		String sql = SQLHelper.getConditionSQL(condition, SQLHelper
				.getExcuteTableName(condition));
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + ("select " + field + " " + sql));
			startTime = System.currentTimeMillis();
		}
		
		Map result = (Map) getNamedParameterJdbcTemplate().queryForObject(
				"select " + field + " " + sql, makeCondition(condition),
				new LowcaseColumnRowMapper());
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		
		return result;
	}

	/**
	 * 通过Map条件查询所有字段返回结果Map 
	 * @param condition Map条件
	 * @return 结果Map，不对字典进行翻译
	 * @exception 当结果数量不是1行时抛出异常
	 * May 21, 2008
	 */
	public Map queryForMap(Map condition) {
		return queryForMap(condition, "*");
	}

	/**
	 * 通过Map条件查询指定字段（可以时多个）返回结果Map 
	 * @param condition Map条件
	 * @param field 字段（表达式），用逗号隔开
	 * @return 结果Map，对字典进行翻译
	 * @exception 当结果数量不是1行时抛出异常
	 * May 21, 2008
	 */
	public Map queryForMapWithDic(Map condition, String field) {
		String sql = SQLHelper.getConditionSQL(condition, SQLHelper
				.getExcuteTableName(condition));
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + ("select " + field + " " + sql));
			startTime = System.currentTimeMillis();
		}
		
		Map result = (Map) getNamedParameterJdbcTemplate().queryForObject(
				"select " + field + " " + sql, makeCondition(condition),
				new DicMapLowcaseColumnRowMapper());
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		
		return result;
	}

	/**
	 * 通过Map条件查询所有字段返回结果Map 
	 * @param condition Map条件
	 * @return 结果Map，对字典进行翻译
	 * @exception 当结果数量不是1行时抛出异常
	 * May 21, 2008
	 */
	public Map queryForMapWithDic(Map condition) {
		return queryForMapWithDic(condition, "*");
	}

	// --------------- SQLMap部分 ------------------

	/**
	 * 执行指定SQL语句 
	 * @param sql 语句，可以是SQLMap中设定的语句的编号（#namespace.id)
	 * @return 影响的行数
	 * May 21, 2008
	 */
	public int execSql(String sql) {
		sql = prepareUpdateSql(sql);
		if (sql == null) {
			return 0;
		}
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		int result = getJdbcTemplate().update(sql);
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	/**
	 * 执行指定SQL语句，并设置Map条件 
	 * @param sql 语句，可以是SQLMap中设定的语句的编号（#namespace.id)
	 * @param condition Map条件
	 * @return 影响的行数
	 * May 21, 2008
	 */
	public int execSql(String sql, Map condition) {
		sql = prepareUpdateSql(sql);
		if (sql == null)
			return 0;
		return doExecuteStatement(sql, condition);
	}

	/**
	 * 执行指定SQL语句，并设置Object条件 
	 * @param sql 语句，可以是SQLMap中设定的语句的编号（#namespace.id)
	 * @param bean Object条件
	 * @return 影响的行数
	 * May 21, 2008
	 */
	public int execSql(String sql, Object bean) {
		sql = prepareUpdateSql(sql);
		if (sql == null)
			return 0;
		return doExecuteStatement(sql, bean);
	}

	private String prepareUpdateSql(String sql) {
		if (sql != null && sql.startsWith("#")) {
			sql = SqlMapConfig.getSql("update", sql);
		}
		return sql;
	}

	public int queryForInt(String sql) {
		sql = prepareSelectSql(sql);
		if (sql == null) {
			return 0;
		}
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		int result = getJdbcTemplate().queryForInt(sql);
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public int queryForInt(String sql, Map condition) {
		sql = prepareSelectSql(sql);
		if (sql == null)
			return 0;

		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		int result = getNamedParameterJdbcTemplate().queryForInt(sql,
				makeCondition(condition));
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public int queryForInt(String sql, Object bean) {
		sql = prepareSelectSql(sql);
		if (sql == null)
			return 0;

		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		int result = getNamedParameterJdbcTemplate().queryForInt(sql,
				new BeanPropertySqlParameterSource(bean));
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	private String prepareSelectSql(String sql) {
		if (sql != null && sql.startsWith("#")) {
			sql = SqlMapConfig.getSql("select", sql);
		}
		return sql;
	}

	public Object queryForObject(String sql, Class clazz) {
		sql = prepareSelectSql(sql);
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		Object result = getJdbcTemplate().queryForObject(sql, clazz);
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public Object queryForObject(String sql, Map condition, Class clazz) {
		sql = prepareSelectSql(sql);
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		
		Object result = getNamedParameterJdbcTemplate().queryForObject(sql,
				makeCondition(condition), clazz);
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public Object queryForObject(String sql, Object bean, Class clazz) {
		sql = prepareSelectSql(sql);
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		
		Object result = getNamedParameterJdbcTemplate().queryForObject(sql,
				new BeanPropertySqlParameterSource(bean), clazz);
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public Map queryForMap(String sql, Map condition) {
		sql = prepareSelectSql(sql);
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		
		Map result = (Map) getNamedParameterJdbcTemplate().queryForObject(sql,
				makeCondition(condition), new LowcaseColumnRowMapper());
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public Map queryForMapWithDic(String sql, Map condition) {
		sql = prepareSelectSql(sql);
		
		long startTime = 0;
		if (saveLog) {
			log.info("执行SQL = " + sql);
			startTime = System.currentTimeMillis();
		}
		
		Map result = (Map) getNamedParameterJdbcTemplate().queryForObject(sql,
				makeCondition(condition), new DicMapLowcaseColumnRowMapper());
		
		if(saveLog){
			endRecordSqlExeTime(sql,startTime);
		}
		return result;
	}

	public List find(String sql, Map condition) {
		sql = prepareSelectSql(sql);
		return getListWithDic(sql, condition);
	}

	public List find2(String sql, Map condition) {
		sql = prepareSelectSql(sql);
		String sql2 = sql.replaceAll(":today", "to_date('"
				+ DateUtils.getCurrentData() + "', 'yyyy-MM-dd')");
		return getListWithDic(sql2, condition);
	}

	public List find(String sql, Object bean) {
		sql = prepareSelectSql(sql);
		return getListWithDic(sql, new BeanPropertySqlParameterSource(bean));
	}

	public List find(String sql, Map condition, int skip, int count) {
		sql = SQLHelper.getListSqlFromSql(prepareSelectSql(sql), skip, count);
		condition.put("page_maxcount", new Integer(skip + count));
		condition.put("page_skipcount", new Integer(skip));
		return getListWithDic(sql, condition);
	}

	public Object findObject(String sql, Map condition, Class clazz) {
		sql = prepareSelectSql(sql);
		Object o = null;
		try {
			long startTime = 0;
			if (saveLog) {
				log.info("执行SQL = " + sql);
				startTime = System.currentTimeMillis();
			}
			
			Map map = namedParameterJdbcTemplate.queryForMap(sql,
					makeCondition(condition));
			
			if(saveLog){
				endRecordSqlExeTime(sql,startTime);
			}
			o = clazz.newInstance();
			DomainBeanUtils.populate(o, map);
		} catch (Exception e) {
			log.error("查询数据生成对象失败！对象" + clazz.getName(), e);
			throw new RuntimeException("查询数据生成对象失败！对象" + clazz.getName() + "\n" + e);
		}
		return o;
	}

	// ====================Blob部分=======================
	/**
	 * Description: 检查BlobUtils对象，若不存在则创建 
	 * May 19, 2008
	 */
	private void checkBlobUtils() {
		if (blobUtils == null) {
			blobUtils = new BlobUtils();
			blobUtils.setJdbcTemplate(getJdbcTemplate());
			blobUtils
					.setNamedParameterJdbcTemplate(getNamedParameterJdbcTemplate());
			blobUtils.setNje(nje);
		}
	}

	/**
	 * Description: 保存包含BLOB类型的信息
	 * @param table 要保存到的表
	 * @param bean 包含有BLOB类型数据的bean 
	 * add:zong on 2006-10-18
	 */
	public int saveBlob(Object bean) {
		if (bean == null) {
			log.warn("保存BLOB数据，参数错误!对象:" + bean.getClass().getName());
			return 0;
		}
		if (!hasBlobField(bean)) { // 该对象中不包含BLOB类型字段,调用普通对象入库方法保存
			return save(bean);
		}
		checkBlobUtils();
		return blobUtils.doInsertBlobTable(SQLHelper.getExcuteTableName(bean),
				bean, null);
	}

	public int saveBlob(Object bean, String fields) {
		if (bean == null) {
			log.warn("保存BLOB数据，参数错误!对象:" + bean.getClass().getName());
			return 0;
		}
		if (!hasBlobField(bean, fields)) { // 该对象中不包含BLOB类型字段,调用普通对象入库方法保存
			return save(bean, fields);
		}
		checkBlobUtils();
		return blobUtils.doInsertBlobTable(SQLHelper.getExcuteTableName(bean),
				bean, fields);
	}

	/**
	 * Description: 更新包含BLOB类型的信息
	 * @param table 要更新到的表
	 * @param bean 包含有BLOB类型数据的bean
	 * @param pk 主键字段数字 
	 * add:zong on 2006-10-18
	 */
	public int updateBlob(final Object bean) {
		if (bean == null) {
			log.warn("修改BLOB数据，参数错误!对象:" + bean.getClass().getName());
			return 0;
		}

		if (!hasBlobField(bean)) { // 该对象中不包含BLOB类型字段,调用普通对象更新方法更新
			return update(bean);
		}
		checkBlobUtils();
		return blobUtils.doUpdateBlobTable(SQLHelper.getExcuteTableName(bean),
				bean, SQLHelper.getObjectPkFields(bean).split(","),
				DomainBeanUtils.getProperties(bean));
	}

	public int updateBlob(final Object bean, String fields) {
		if (bean == null) {
			log.warn("修改BLOB数据，参数错误!对象:" + bean.getClass().getName());
			return 0;
		}

		if (!hasBlobField(bean, fields)) { // 该对象中不包含BLOB类型字段,调用普通对象更新方法更新
			return update(bean, fields);
		}
		checkBlobUtils();
		return blobUtils.doUpdateBlobTable(SQLHelper.getExcuteTableName(bean),
				bean, SQLHelper.getObjectPkFields(bean).split(","), fields
						.split(","));
	}

	public int updateBlob(String sql, Map dataMap, String[] fields,
			int[] lengths) {
		sql = prepareUpdateSql(sql);
		checkBlobUtils();
		return blobUtils.doUpdateBlob(sql, dataMap, fields, lengths);
	}

	private boolean hasBlobField(final Object bean) {
		Object s = DomainBeanUtils.getPropertyValue(bean,
				DbConstants.DB_BLOBFIELD);
		return (s != null && !s.equals(""));
	}

	private boolean hasBlobField(final Object bean, final String fields) {
		Object s = DomainBeanUtils.getPropertyValue(bean,
				DbConstants.DB_BLOBFIELD);
		if (s != null && !s.equals("")) {
			if (fields.equals("*")) {
				return true;
			} else {
				String[] blobfields = ((String) s).split(",");
				String[] upfields = fields.split(",");
				for (int i = 0; i < upfields.length; i++) {
					if (ArrayUtils.containValue(blobfields, upfields[i])) {
						return true;
					}
				}
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Description: 获取包含BLOB类型的信息
	 * @param table 要获取Blob类型数据到的表
	 * @param bean 包含有BLOB类型数据的bean
	 * @param pk 主键字段
	 * @param pkValue 主键字段值 
	 * add:zong on 2006-10-18
	 */
	public Object getBlob(String table, Object bean, String[] pk,
			String[] pkValue) {
		if ((table == null) || ("".equals(table.trim())) || (bean == null)
				|| (pk == null) || (pk.length == 0) || (pkValue == null)
				|| (pkValue.length == 0) || (pk.length != pkValue.length)) {
			log.warn("获取BLOB数据，参数错误!对象:" + bean.getClass().getName());
			return null;
		}
		String[] fields = DomainBeanUtils.getProperties(bean); // 该bean的所有字段
		boolean flag = false;

		/*
		 * 根据所给定的代表数据库表的bean中字段组织sql语句
		 */
		StringBuffer sql = new StringBuffer("SELECT ");
		for (int i = 0; i < fields.length; i++) {
			if (!SQLHelper.isSystemIdentity(fields[i])) {
				sql.append(fields[i] + ","); // 将字段追加到sql语句中
				flag = true;
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" FROM ").append(table).append(" WHERE 1 = 1");
		for (int i = 0; i < pk.length; i++) {
			sql.append(" AND ").append(pk[i]).append(" = ?"); // 追加查询条件
		}

		if (!flag) { // 该对象中不包含BLOB类型字段,调用普通对象获取方法更新
			return DomainBeanUtils.populate(bean, getJdbcTemplate()
					.queryForMap(sql.toString(), pkValue));
		}
		checkBlobUtils();
		return blobUtils.doGetBlobTable(sql.toString(), bean, pkValue);
	}

	public Map getBlob(String sql, Map condition) {
		checkBlobUtils();
		sql = prepareSelectSql(sql);
		return blobUtils.doGetByteBlob(sql, makeCondition(condition));
	}

	private Map makeCondition(Map condition) {
		if (condition != null) {
			Iterator item = condition.keySet().iterator();
			while (item.hasNext()) {
				String searchField = (String) item.next();
				if (condition.get(searchField).getClass() == String.class) {
					String value = condition.get(searchField).toString();
					if (value.indexOf("to_date") >= 0) {
						condition.put(searchField, DateUtils.createDate(value
								.substring(7)));
					}
				}
			}
		}
		return condition;
	}
	/**
	 * @功能：对指定字段值加密
	 * @参数：
	 * @param bean 对象
	 * @param crcColumnMap 加密字段名
	 * @return
	 * @返回值：Object
	 * create by zhaohuibin 2014-12-1 下午09:11:22
	 */
	private Object transBeanToCrc(Object bean,Map crcColumnMap){
		Map map = DomainBeanUtils.getPropertiesClassMap(bean);
		Iterator _iterator_ = crcColumnMap.keySet().iterator();
		while(_iterator_.hasNext()){
			String _key_ = _iterator_.next().toString();
			String _value_ = (String)crcColumnMap.get(_key_);
			System.out.print("_key_=" + _key_);
			System.out.println("   _value_=" + _value_);

			if(map.containsKey(_key_)){
				
				if("sl".equals(_key_)){
					String tn = SQLHelper.getExcuteTableName(bean);
					if (null != tn && !"".equals(tn)) {
						if (tn.equals("t_lz_gmzwp") || tn.equals("t_lz_yszwp")) {
							BigDecimal value = (BigDecimal) DomainBeanUtils.getPropertyValue(bean,
									"sl");
							if (null != value && !"".equals(value)) {
								int val=Integer.parseInt(value.toString())^1024;
								DomainBeanUtils.setProperty(bean, _key_,Integer.toString(val));
							}
						}
					}
				}else{
					String value = (String)DomainBeanUtils.getPropertyValue(bean, _key_);
					//目前只针对运输证中字段加密
					if(SQLHelper.getExcuteTableName(bean).equals("t_lz_ysz") ||SQLHelper.getExcuteTableName(bean).equals("t_lz_yszwp")
							|| SQLHelper.getExcuteTableName(bean).equals("t_lz_gmzwp")){
						//判断加密字段的值是否为空，不为空则加密，为空不做处理
						if(null != value && !"".equals(value)){
							StringBuffer valueCrc = new StringBuffer();
							for (int i = 0; i < value.length(); i++) {
								String temp = "";
								if(i < value.length()){
									temp = value.substring(i,i + 1);
									valueCrc.append(Base64.encode(TripleDES_CRC.encode(temp))).append(IConstants.SPLIT_CHAR);
								}
							}
							DomainBeanUtils.setProperty(bean, _key_, IConstants.JADL_JMBZ + valueCrc.toString());
						}
					}
				}
				/**
				String value = (String)DomainBeanUtils.getPropertyValue(bean, _key_);
				//判断加密字段的值是否为空，不为空则加密，为空不做处理
				if(null != value && !"".equals(value)){
					StringBuffer valueCrc = new StringBuffer();
					for (int i = 0; i < value.length(); i++) {
						String temp = "";
						if(i < value.length()){
							temp = value.substring(i,i + 1);
							valueCrc.append(Base64.encode(TripleDES_CRC.encode(temp))).append(IConstants.SPLIT_CHAR);
						}
					}
					DomainBeanUtils.setProperty(bean, _key_, IConstants.JADL_JMBZ + valueCrc.toString());
				}**/
			}
		}
		return bean;
	}
	
	/**
	 * 对指定字段值解密
	 * @Description: TODO
	 * @param  bean
	 * @param  crcColumnMap
	 * @return Object  
	 * @throws
	 * @author zhangyk
	 * @date 2015-1-12 下午1:43:30
	 * @version 1.0
	 */
	private Object transCrcToBean(Object bean,Map crcColumnMap){
		Map map = DomainBeanUtils.getPropertiesClassMap(bean);
		Iterator _iterator_ = crcColumnMap.keySet().iterator();
		while(_iterator_.hasNext()){
			String _key_ = _iterator_.next().toString();
			String _value_ = (String)crcColumnMap.get(_key_);
			System.out.print("_key_=" + _key_);
			System.out.println("   _value_=" + _value_);

			if(map.containsKey(_key_)){
				if("sl".equals(_key_)){
					String tn = SQLHelper.getExcuteTableName(bean);
					if (null != tn && !"".equals(tn)) {
						if (tn.equals("t_lz_gmzwp") || tn.equals("t_lz_yszwp")) {
							BigDecimal value = (BigDecimal) DomainBeanUtils.getPropertyValue(bean,
									"sl");
							if (null != value && !"".equals(value)) {
								int val=Integer.parseInt(value.toString())^1024;
								DomainBeanUtils.setProperty(bean, _key_,Integer.toString(val));
							}
						}
					}
				}else{
					String value = (String)DomainBeanUtils.getPropertyValue(bean, _key_);
					if(null != value && !"".equals(value)){
						String[] arr=value.substring(4).split(IConstants.SPLIT_CHAR);
						StringBuffer valueCrc = new StringBuffer();
						for (int i = 0; i < arr.length; i++) {
							if(i < value.length()){
								valueCrc.append(TripleDES_CRC.decode(Base64.decode(arr[i])));
							}
						}
						DomainBeanUtils.setProperty(bean, _key_,valueCrc.toString());
					}
				}
			}
		}
		return bean;
	}
	
	/**
	 * @功能：转换查询条件中涉及加密的字段内容为加密内容
	 * @参数：
	 * @param condition 条件，格式“字段名=%XXX%或字段名=%XXX或字段名=XXX%”
	 * @返回值：void
	 * create by zhaohuibin 2014-12-1 下午09:12:38
	 */
	private void transQueryCondition(Map condition) {
		if (condition != null) {
			Iterator item = condition.keySet().iterator();
			while (item.hasNext()) {
				String searchField = (String) item.next();
				if (condition.get(searchField).getClass() == String.class) {
					String value = condition.get(searchField).toString();
					StringBuffer valueCondition = new StringBuffer();
					String pre = "";
					String end = "";
					if (columnArrayMap.containsKey(searchField)) {
						if (value.startsWith(IConstants.CONDITION_BFH) && value.endsWith(IConstants.CONDITION_BFH)) {
							value = value.substring(1, value.length() - 1);
							pre = IConstants.CONDITION_BFH;
							end = IConstants.CONDITION_BFH;
						}
						if (value.startsWith(IConstants.CONDITION_BFH) && !value.endsWith(IConstants.CONDITION_BFH)) {
							value = value.substring(1);
							pre = IConstants.CONDITION_BFH;
						}
						if (!value.startsWith(IConstants.CONDITION_BFH) && value.endsWith(IConstants.CONDITION_BFH)) {
							value = value.substring(0, value.length() - 1);
							end = IConstants.CONDITION_BFH;
						}
						for (int i = 0; i < value.length(); i++) {
							String temp = "";
							if (i < value.length()) {
								temp = value.substring(i, i + 1);
								valueCondition.append(Base64.encode(TripleDES_CRC.encode(temp))).append(
										IConstants.SPLIT_CHAR);
							}
						}
						condition.put(searchField, pre
								+ valueCondition.toString() + end);
					}
				}
			}
		}
	}

	/**
	 * 成批插入或更新
	 * Add by zongshuai 2009-07-10
	 * @param sql:待执行的sql语句
	 * @param date:插入或更新的值.是一个二维数组,第一维表示插入或更新的记录数,第二维表示插入或更新的字段值.
	 */
	public void executeBatchUpdate(String sql, final Object [][] data) {
		if (sql != null && sql.startsWith("#")) {
			sql = SqlMapConfig.getSql("update", sql);
		}
		BatchPreparedStatementSetter setter = null;
        setter = new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return data.length;
            }

            public void setValues(PreparedStatement ps, int index)
                    throws SQLException {
        		for(int i=0;i<data[index].length;i++){
        			if(data[index][i] instanceof String){
        				ps.setString(i + 1, (String)data[index][i]);
        			}else if(data[index][i] instanceof Integer){
        				ps.setInt(i + 1, ((Integer)data[index][i]).intValue());
        			}else if(data[index][i] instanceof Date){
        				ps.setTimestamp(i + 1, new Timestamp(((Date)data[index][i]).getTime()));
        			}else if(data[index][i] instanceof BigDecimal){
        				ps.setBigDecimal(i+1, (BigDecimal)data[index][i]);
        			}else if(data[index][i] instanceof Long){
        				ps.setLong(i+1, (Long)data[index][i]);
        			}else if(data[index][i] instanceof Double){
        				ps.setDouble(i+1, (Double)data[index][i]);
        			}else if(data[index][i] instanceof Float){
        				ps.setFloat(i+1, (Float)data[index][i]);
        			}else if(data[index][i] instanceof Double){
        				ps.setDouble(i+1, (Double)data[index][i]);
        			}else if(data[index][i] instanceof Object){
        				ps.setObject(i+1, (Object)data[index][i]);
        			}else if(data[index][i] instanceof Short){
        				ps.setShort(i+1, (Short)data[index][i]);
        			}else if(data[index][i] instanceof Clob){
        				ps.setClob(i+1, (Clob)data[index][i]);
        			}else if(data[index][i] instanceof Blob){
        				ps.setBlob(i+1, (Blob)data[index][i]);
        			}
        		}
            }
        };
        getJdbcTemplate().batchUpdate(sql, setter);		
	}
	
	public int[] executeBatchUpdate(String sql, final String [] field, final List data) {
		if (sql != null && sql.startsWith("#")) {
			sql = SqlMapConfig.getSql("update", sql);
		}
		if ((data == null) || (data.size() == 0)) {
			return null;
		}
		BatchPreparedStatementSetter setter = null;
        setter = new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return data.size();
            }
            public void setValues(PreparedStatement ps, int index)
                    throws SQLException {
            	Map _data = (Map)data.get(index);
				for (int i = 0; i < field.length; i++) {
					Object value = _data.get(field[i]);
        			if(value instanceof String){
        				ps.setString(i + 1, (String)value);
        			}else if(value instanceof Integer){
        				ps.setInt(i + 1, ((Integer)value).intValue());
        			}else if(value instanceof Date){
        				ps.setTimestamp(i + 1, new Timestamp(((Date)value).getTime()));
        			}else if(value instanceof BigDecimal){
        				ps.setBigDecimal(i+1, (BigDecimal)value);
        			}else if(value instanceof Long){
        				ps.setLong(i+1, (Long)value);
        			}else if(value instanceof Double){
        				ps.setDouble(i+1, (Double)value);
        			}else if(value instanceof Float){
        				ps.setFloat(i+1, (Float)value);
        			}else if(value instanceof Double){
        				ps.setDouble(i+1, (Double)value);
        			}else if(value instanceof Object){
        				ps.setObject(i+1, (Object)value);
        			}else if(value instanceof Short){
        				ps.setShort(i+1, (Short)value);
        			}else if(value instanceof Clob){
        				ps.setClob(i+1, (Clob)value);
        			}else if(value instanceof Blob){
        				ps.setBlob(i+1, (Blob)value);
        			}
        		}
            }
        };
       
    	 return  getJdbcTemplate().batchUpdate(sql, setter);
		
		
	}
}