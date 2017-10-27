package com.jadlsoft.utils;

/**
 * @SQLBuilder.java SQL构造器
 * @作者 田智武
 * @日期 Nov 1, 2006
 */
public class SQLBuilder {
	/**
	 * @createSQL 构造SQL语句
	 * @param tName 表或视图名称
	 * @param conditions 条件
	 * @param resultcol 结果列
	 * @return sql语句
	 */
	public static String createSQL(String tName,String conditions,String resultcol){
		if(tName == null||tName.equals("")){
			return null;
		}
		
//		查多表
		String[] tname = tName.split(",");
		
//		查多表的所有列
		if(tname.length >0&&resultcol.equalsIgnoreCase("*")){
			resultcol = tname[0]+".*";
			for(int i=1;i<tname.length;i++){
				resultcol += ","+tname[i]+".*";
			}
		}
		
		String sql = "";
		
//		所有列
		if(resultcol == null||resultcol.equals("")){
			resultcol = "*";
		}
		
//		条件为空,直接返回sql
		if(conditions==null||conditions.equals("")){
			return "SELECT "+resultcol+" FROM "+tName;
		}
		
//		条件不位空
		String[] condstr = conditions.split("~");
		for(int i=0; i<condstr.length; i++){
			sql += " and "+createWhere(condstr[i]);
		}
		return "SELECT "+resultcol+" FROM "+tName+" WHERE 1=1"+sql;
	}
	
	/**
	 * @createWhere 构造where条件
	 * @param where where条件
	 * @return where语句
	 */
	public static String createWhere(String where){
		String[] wh = where.split("@");
		if(wh.length != 3){
			return "1=1";
		}
		String colname = wh[0];
		String oper = wh[1];
		String colvalue = wh[2];
		if(oper.equalsIgnoreCase("equals")||oper.equalsIgnoreCase("equal")){  //等于
			return colname+" = '"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("equalsInt")||oper.equalsIgnoreCase("equalInt")){   //等于一个数字
			return colname+" = "+colvalue;
		}else if(oper.equalsIgnoreCase("notequals")||oper.equalsIgnoreCase("notequal")){    //不等于
			return colname+" != '"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("notequalsInt")||oper.equalsIgnoreCase("notequalInt")){  //不等于一个数字
			return colname+" != "+colvalue;
		}else if(oper.equalsIgnoreCase("more")){   //大于
			return colname+" > '"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("moreInt")){  //大于一个数字
			return colname+" > "+colvalue;
		}else if(oper.equalsIgnoreCase("less")){   //小于
			return colname+" < '"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("lessInt")){  //小于一个数字
			return colname+" < "+colvalue;
		}else if(oper.equalsIgnoreCase("moreequal")||oper.equalsIgnoreCase("moreequals")){   //大于等于
			return colname+" >= '"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("moreequalInt")||oper.equalsIgnoreCase("moreequalsInt")){  //大于等于一个数字
			return colname+" >= "+colvalue;
		}else if(oper.equalsIgnoreCase("lessequal")||oper.equalsIgnoreCase("lessequals")){   //小于等于
			return colname+" <= '"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("lessequalInt")||oper.equalsIgnoreCase("lessequalsInt")){  //小于等于一个数字
			return colname+" <= "+colvalue;
		}else if(oper.equalsIgnoreCase("like")){
			return colname+" like '%"+colvalue+"%'";
		}else if(oper.equalsIgnoreCase("likeLeft")){
			return colname+" like '"+colvalue+"%'";
		}else if(oper.equalsIgnoreCase("likeRight")){
			return colname+" like '%"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("notlike")){
			return colname+" not like '%"+colvalue+"'";
		}else if(oper.equalsIgnoreCase("levellikeLeft")){
			return createLevelCondition(colname, colvalue + "%", true);
		}else if(oper.equalsIgnoreCase("levelequals")||oper.equalsIgnoreCase("levelequal")){  //等于
			return createLevelCondition(colname, colvalue, true);
		}
		
		return "1=1";
	}
	
	public static String createLevelCondition(String colname, String colvalue, boolean isLongNo) {
		if(isLongNo) {
			if(colvalue.indexOf("%")>=0) {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE " + colname+" LIKE xzqh || '%' AND levelno LIKE '"+colvalue+"')";
			} else {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE " + colname+" LIKE xzqh || '%' AND levelno = '"+colvalue+"')";
			}
		} else {
			if(colvalue.indexOf("%")>=0) {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE xzqh=" + colname+" AND levelno LIKE '"+colvalue+"')";
			} else {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE xzqh=" + colname+" AND levelno = '"+colvalue+"')";
			}
		}
		
	}	
}
