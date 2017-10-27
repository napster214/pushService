package com.jadlsoft.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.domain.UserSessionBean;


public class CommonListManager extends BaseManager {

	public int getDataCount(Map condition) {
		return daoUtils.getCount(condition);
	}

	public Map getDataCountAndList(Map condition, int pageSize, int pageNo) {
		int skip = (pageNo-1) * pageSize;
		return daoUtils.getCountAndList(condition, skip, pageSize);
	}

	public List getDataList(Map condition, int skip, int count) {
		return daoUtils.find(condition, skip, count);
	}
	
	public Map getDataCountAndList(Map condition, String tableName, int pageSize, int pageNo) {
		int skip = (pageNo-1) * pageSize;
		return daoUtils.getCountAndList(condition, tableName, skip, pageSize);
	}

	public Map getDataCountAndList(List conditions, String tableName, int pageSize, int pageNo) {
		int skip = (pageNo-1) * pageSize;
		return daoUtils.getCountAndList(conditions, tableName, skip, pageSize);
	}
	
	public Map getDataCountAndList(Map condition, List conditions, UserSessionBean user, String tableName, String className, int pageSize, int pageNo) {
		
		tableName = transTableName(tableName , className , conditions , user);
		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(DaoUtils.createCondition(conds[0], conds[1], conds[2]));
			}
			return daoUtils.getCountAndList(realConditions, tableName, skip, pageSize);
		} else {
			return daoUtils.getCountAndList(condition, tableName, skip, pageSize);
		}
	}
	/**
	 * 张方俊 2008-09-23 仅查询一次记录总数。此方法查询记录的列表
	 * @param condition
	 * @param conditions
	 * @param tableName
	 * @param className
	 * @param pageSize
	 * @param pageNo
	 * @param count
	 * @return
	 */
	public Map getDataList(Map condition, List conditions, UserSessionBean user, String tableName, String className, int pageSize, int pageNo, int count) {
		tableName = transTableName(tableName , className , conditions ,user );		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(DaoUtils.createCondition(conds[0], conds[1], conds[2]));
			}
			return daoUtils.getList(realConditions, tableName, skip, pageSize, count);
		} 
		throw new RuntimeException("查询错误！");
	}
	
	/**
	 * @功能：同getDataList(Map condition, List conditions, String tableName, String className, int pageSize, int pageNo, int count) {
	 * 加入参数afterClass，重载原方法
	 * @参数：
	 * @param condition
	 * @param conditions
	 * @param tableName
	 * @param className
	 * @param afterClass
	 * @param pageSize
	 * @param pageNo
	 * @param count
	 * @return
	 * @返回值：Map
	 */
	public Map getDataList(Map condition, List conditions,UserSessionBean user, String tableName, String className, String afterClass, int pageSize, int pageNo, int count) {
		tableName = transTableName(tableName , className , conditions , user);		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(DaoUtils.createCondition(conds[0], conds[1], conds[2]));
			}
			Map resultMap = daoUtils.getList(realConditions, tableName, skip, pageSize, count);
			List resultList = (List)resultMap.get("list");
			aferQuery(resultList , afterClass);
			return resultMap;
		} 
		throw new RuntimeException("查询错误！");
	}
	/**
	 * 张方俊 2008-09-23 查询记录总数
	 * @param condition
	 * @param conditions
	 * @param tableName
	 * @param className
	 * @param pageSize
	 * @param pageNo
	 * @param count
	 * @return
	 */
	public Map getDataCount(Map condition, List conditions,UserSessionBean user, String tableName, String className, int pageSize, int pageNo, int count) {
		tableName = transTableName(tableName , className , conditions, user);		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(DaoUtils.createCondition(conds[0], conds[1], conds[2]));
			}			
			return daoUtils.getDataCount(realConditions, tableName, skip, pageSize, count);
		} 
		throw new RuntimeException("查询错误！");
	}
	
	private String transTableName(String tableName , String className , List conditions , UserSessionBean user){		
		if(className == null || className.equals("")){
			return tableName;
		}
		if(className.equalsIgnoreCase("default")){
			className = "com.jadlsoft.business.CommonListConfigDefault";
		}
		Class clazz = null;
		CommonListConfigInterface commonListConfigInterface = null;
		try{
			clazz = Class.forName(className);
			commonListConfigInterface = (CommonListConfigInterface) clazz.newInstance();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			return tableName;
		} catch (InstantiationException e){
			e.printStackTrace();
		} catch (IllegalAccessException e){			
			e.printStackTrace();
		}
		return commonListConfigInterface.transTableName(tableName , conditions , this.daoUtils ,user);
	}
	
	/**
	 * 增加对查询结果的处理，主要是翻译
	 * author 张方俊 2012-9-24 下午05:18:04
	 * @param resultList 查询结果列表
	 * @param afterName 处理的类名
	 * @return
	 */
	private List aferQuery(List resultList , String afterName){
		if(resultList == null || resultList.size() == 0){
			return resultList;
		}
		if(afterName == null || afterName.equals("")){
			return resultList;
		}
		Class clazz = null;
		CommonListConfigAfterInterface commonListConfigInterface = null;
		try{
			clazz = Class.forName(afterName);
			commonListConfigInterface = (CommonListConfigAfterInterface) clazz.newInstance();
			return commonListConfigInterface.transResultList(resultList , this.daoUtils);
		} catch (ClassNotFoundException e){
			throw new RuntimeException(e);
		} catch (InstantiationException e){
			throw new RuntimeException(e);
		} catch (IllegalAccessException e){			
			throw new RuntimeException(e);
		}
	}

}
