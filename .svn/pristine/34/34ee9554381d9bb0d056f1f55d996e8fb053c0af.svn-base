package com.jadlsoft.dbutils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.RowMapper;

import com.jadlsoft.utils.IConstants;
import com.jadlsoft.utils.TripleDES_CRC;
import com.jadlsoft.utils.icutil.Base64;

/**
 * <p>Title: LowcaseColumnRowMapper</p>
 * <p>Description: 实现数据字典映射，小写列名的RowMapper接口 </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2007-8-15
*/
public class DicMapLowcaseColumnRowMapper implements RowMapper {
	
	private static Map dicMap = new LinkedHashMap();	//数据字典定义
	private static Map crcColumnMap = new LinkedHashMap();	//

	private static Map dicCache = null;
	private static Logger log = Logger.getLogger(DicMapLowcaseColumnRowMapper.class);
	
	public static Map getDicMap(){
		return dicMap;
	}
	
	public static void setCrcColumnMap(Map crcColumnMap) {
		DicMapLowcaseColumnRowMapper.crcColumnMap = crcColumnMap;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map mapOfColValues = new LinkedHashMap(columnCount);
		List diclist = new LinkedList();
		List crcColumnList = new LinkedList();
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(rsmd.getColumnName(i));
			Object obj = getColumnValue(rs, i);
			mapOfColValues.put(key, obj);
			if(dicMap.containsKey(key)) {
				diclist.add(key);
			}
			if(crcColumnMap.containsKey(key)) {
				crcColumnList.add(key);
			}
		}
		if(!diclist.isEmpty()) {
			for(int i=0;i<diclist.size();i++) {
				String key = (String) diclist.get(i);
				Map coldicMap = (Map) dicMap.get(key);
				String keycols = (String) coldicMap.get("key");
				String keyValue = "";
				if(keycols.indexOf(",")>0) {
					String[] columns= keycols.split(",");
					boolean haserr = false;
					for(int j=0;j<columns.length;j++) {
						Object tempvalue = mapOfColValues.get(columns[j]);
						if(tempvalue==null) {
							haserr = true;
							continue;
						}
						keyValue += tempvalue + ",";
					}
					if(haserr) {
						continue;
					}
					keyValue = keyValue.substring(0,keyValue.length()-1);
				} else {
					/*
					 * 李洪磊 2008-07-09 修改tempvalue值
					 * 原程序为：Object tempvalue = mapOfColValues.get(keycols);
					 */
					Object tempvalue = mapOfColValues.get(key);
					if(tempvalue==null) {
						/*
						 * 张方俊 2008-06-24 修改：增加翻译不成功的处理
						 */
						mapOfColValues.put(key + "_dicvalue", mapOfColValues.get(key));
						continue;
					}
					keyValue = tempvalue.toString();
				}
				Map data = (Map) ((Map) dicCache.get(coldicMap.get("table"))).get(keyValue);
				if(data!=null) {
					String textcol = (String) coldicMap.get("text");
					String[] cols = textcol.split(",");
					for(int colsi = 0;colsi<cols.length;colsi++){
						if(colsi==0){
							mapOfColValues.put(key + "_dicvalue", data.get(cols[0]));
						}else{
							mapOfColValues.put(key + "_dicvalue_" + cols[colsi], data.get(cols[colsi]));
						}
					}
				}
				/*
				 * 张方俊 2008-06-24 修改：增加翻译不成功的处理
				 */
				else{
					mapOfColValues.put(key + "_dicvalue", mapOfColValues.get(key));
				}
			}
		}
		if(!crcColumnList.isEmpty()) {
			for(int i=0;i<crcColumnList.size();i++) {
				String key = (String) crcColumnList.get(i);
				if(!"sl".equals(key)){
					String tempvalue = (String)mapOfColValues.get(key);
					StringBuffer value = new StringBuffer(); 
					if(tempvalue!=null && tempvalue.startsWith(IConstants.JADL_JMBZ)) {
						String[] tempvalueArry = tempvalue.substring(4).split(IConstants.SPLIT_CHAR);
						for (int j = 0; j < tempvalueArry.length; j++) {
							value.append(TripleDES_CRC.decode(Base64.decode(tempvalueArry[j])));
						}
						mapOfColValues.put(key, value.toString());
					}
				}
			}
		}
		return mapOfColValues;
	}
	
	protected String getColumnKey(String columnName) {
		return columnName.toLowerCase();
	}
	
	private static Calendar calendar = Calendar.getInstance(); 
	
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		Object value = rs.getObject(index);
		
		if(value instanceof String) {
			return ((String)value).trim();
		} else {
			if(value instanceof java.util.Date || value instanceof java.sql.Date) {
				Timestamp result = rs.getTimestamp(index);
				calendar.setTime(result);
				if(calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0){
					return value;
				}else{
					return result;
				}
			} else {
				return value;
			}
		}
	}
	
	/**
	 * Description：从XML文件中读取字段的字典配置
	 * @param configFile XML配置文件
	 * @param dicCache   内存中的字典缓存，用于验证字典项的有效性
	 * 配置文件格式：
	 * <XML>
	 * 		<columns>
	 * 			<column name="ryzt" table="t_dm_zt" key="code" text="text" />
	 * 		</columns>
	 * <XML>
	 * 	其中，code和text可以是多个列，用逗号(,)隔开
	 */
	public static void loadDicConfig(String configFile, Map adicCache){
		if(adicCache==null || adicCache.size()==0) {
			return;
		}
		dicCache = adicCache;
		
		File file = new File(configFile.replaceAll("%20", " "));
		if(file.exists() && file.isFile() && file.canRead()) {
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				Iterator item = root.elementIterator();
				while(item.hasNext()) {
					Element column = (Element) item.next();
					String columnName = column.attributeValue("name");
					String columnTable = column.attributeValue("table");
					//String columnCols = column.attributeValue("usecols");
					String columnKey = column.attributeValue("key");
					if(columnKey==null || columnKey.length()==0) {
						columnKey = columnName;
					}
					String columnText = column.attributeValue("text");
					if(adicCache.containsKey(columnTable)) {
						Map datamap = (Map) adicCache.get(columnTable);
						//有数据才添加缓存定义
						if(!datamap.isEmpty()) {
							/*
							Map data = (Map) datamap.get(datamap.keySet().toArray()[0]);
							String[] keycolumns = columnKey.split(",");
							boolean canCache = true;
							for(int i=0;i<keycolumns.length;i++) {
								if(!data.containsKey(keycolumns[i])) {
									canCache = false;
									break;
								}
							}
							if(canCache) { */
								Map dicone = new HashMap();
								dicone.put("table", columnTable);
								//dicone.put("column", columnCols);
								dicone.put("key", columnKey);
								dicone.put("text", columnText);
								
								dicMap.put(columnName, dicone);
							//}
						}
					}
				}
			} catch (DocumentException e) {
				log.error("读取数据字典映射关系错误！", e);
			}
		}
	}

}
