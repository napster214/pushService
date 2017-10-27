package com.jadlsoft.business.tjbb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TjbbBaseManager {
	
	private static Map config = new HashMap(); 
	private static Logger logger = Logger.getLogger(TjbbBaseManager.class);
			
	static{
		logger.info("开始读取EXCEL配置文件！");
		SAXReader reader = new SAXReader();// 读xml文件包
		InputStream in = null;
		try{
			in = TjbbBaseManager.class.getClassLoader().getResourceAsStream("excel/excelConfig.xml");
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			List items = root.elements();
			for(int i=0;i<items.size();i++){
				Element e = (Element) items.get(i);
				String code = e.attributeValue("code");
				String fileName = e.attributeValue("fileName");
				config.put(code, fileName);
			}
			logger.info("成功读取EXCEL配置文件！");
		} catch (DocumentException e){
			logger.error("读取EXCEL配置文件错误！");
			throw new RuntimeException("读取EXCEL配置文件错误！" + e);
		} finally {
			if(in != null){
				try	{					
					in.close();
				} catch (IOException e)	{
					logger.error("关闭EXCEL配置文件错误！");
					throw new RuntimeException("关闭EXCEL配置文件错误！" + e);
				}
			}
		}
	}
	
	public void templete(String code , OutputStream targetFile , Map dataMap) throws IOException{
		String fileName = (String) config.get(code);
		InputStream templeteInput = TjbbBaseManager.class.getClassLoader().getResourceAsStream("excel/" + fileName);
		this._templete(templeteInput, targetFile, dataMap);
		templeteInput.close();
	}
	
	/**
	 * 根据指定的
	 * @param templetePath
	 * @param targetFile
	 * @param dataMap
	 */
	private void _templete(InputStream templeteInput , OutputStream targetFile , Map dataMap){
		Workbook templeteXls = null;
		WritableWorkbook targetXls = null;
		try{
			templeteXls = Workbook.getWorkbook(templeteInput);						
			targetXls = Workbook.createWorkbook(targetFile, templeteXls);
			WritableSheet targetSheet = targetXls.getSheet(0);
			
			int rows = targetSheet.getRows();
			int cols = targetSheet.getColumns();			
			//	遍历所有的CELL,对于新插入的行需要统计
			for(int i=0;i<rows;i++){
				//	记录已经遍历的LIST
				Map hasList = new HashMap();
				for(int j=0;j<cols;j++){
					Cell c = targetSheet.getCell(j, i);
					if(!(c instanceof Label)){
						continue;
					}
										
					Label cell = (Label) targetSheet.getWritableCell(j, i);
					String content = cell.getContents();
										
					if(isInsertList(content)){	
						//	根据内容获取list的名称
						String listName = getListNameBy(content);
						//	EXCEL 模板指定插入LIST
						if(!hasList.containsKey(listName)){
							hasList.put(listName, "1");	
							
							//	获取哪些列需要插入值
							Map writeDataMap = this.getListWriteDataPositions(targetSheet, i, listName);
							
							List cols_ = (List) writeDataMap.get("columnPosition");
							List properties_ = (List) writeDataMap.get("properties");
							List listData = (List) dataMap.get(listName);	
							
							/*
							 * 处理listData长度为0的情况
							 */
							if(listData == null || listData.size() == 0){
								for(int n=0;n<cols_.size();n++){
									int column = Integer.parseInt((String) cols_.get(n));
									Label label = (Label) targetSheet.getWritableCell(column, i);
									label.setString("");
								}
								continue;
							}
							
							//	第一行的数据是替换，之后的数据是插入
							for(int m=0;m<cols_.size();m++){
								Map data = (Map) listData.get(0);
								for(int n=0;n<cols_.size();n++){
									int column = Integer.parseInt((String) cols_.get(n));
									Label label = (Label) targetSheet.getWritableCell(column, i);
									String value = "";
									if(data.get((String) properties_.get(n)) instanceof BigDecimal){
										value = (BigDecimal) data.get((String) properties_.get(n)) + "";
									}else{
										value = (String) data.get((String) properties_.get(n));
									}
									label.setString(value);
								}
							}
							for(int k=1;k<listData.size();k++){								
								this.insertNewRow(targetSheet, i + (k - 1), cols_, properties_, (Map) listData.get(k));
								//	重要：必须将行数增加
								rows = rows + 1;
							}
						}else{
							continue;
						}
					}else if(isInsertData(content)){
						String key = content.substring(content.indexOf(":") + 1);
						//	EXCEL 模板指定插入值
						String value = "";
						if(dataMap.get(key) instanceof BigDecimal){
							value = (BigDecimal) dataMap.get(key) + "";
						}else{						
							value = (String) dataMap.get(key);
						}
						value = value == null ? "" : value;
						cell.setString(value);
					}
				}
			}			
			targetXls.write();
		} catch (BiffException e){
			throw new RuntimeException("导出EXCEL文件！【BiffException】" + e);
		} catch (IOException e){
			throw new RuntimeException("导出EXCEL文件！【IOException】" + e);
		} catch (WriteException e){
			throw new RuntimeException("导出EXCEL文件！【WriteException】" + e);
		} finally {	
			try{
				if(targetXls != null){
					targetXls.close();
				}
				if(templeteXls != null){
					templeteXls.close();
				}	
			} catch(IOException e){
				throw new RuntimeException("关闭EXCEL模板文件错误。" + e);
			} catch (WriteException e){
				throw new RuntimeException("关闭EXCEL模板文件错误。" + e);
			}
		}
	}
	
	/**
	 * 是否进行LIST操作
	 * @param content 模板中定义的内容
	 * @return
	 */
	private static boolean isInsertList(String content){
		return content.startsWith(":") && content.indexOf(".") != -1;
	}
	
	/**
	 * 根据模板中定义的变量获取LIST的名称
	 * @param content 
	 * @return
	 */
	private static String getListNameBy(String content){
		int p1 = content.indexOf(":");
		int p2 = content.indexOf(".");
		return content.substring(p1 + 1 , p2);
	}
	
	/**
	 * 是否直接插入值
	 * @param content
	 * @return
	 */
	private static boolean isInsertData(String content){
		return content.startsWith(":") && content.indexOf(".") == -1;
	}
	
	/**
	 * EXCEL插入列表的格式如下：
	 * :dwList.dwdm :dwList.dwmc :dwList.zt .....
	 * 
	 * @param templeteSheet 模板的sheet
	 * @param rowPosition 插入的位置
	 * @param listName List的名称，如 list.a，dwList.dwmc
	 * @return Map对象，包括：columnPosition 需要填充的属性的位置，properties list.get(i)中Map对象所包含的属性
	 */
	private Map getListWriteDataPositions(WritableSheet templeteSheet , int rowPosition , String listName){
		Map listMap = new HashMap();
		List postions = new ArrayList();
		List properties = new ArrayList();
		int cols = templeteSheet.getColumns();
		for(int i=0;i<cols;i++){
			String cellContent = templeteSheet.getCell(i, rowPosition).getContents();
			int p = cellContent.indexOf(":" + listName + ".");
			if(p != -1){
				postions.add("" + i);
				String property = cellContent.substring(p + 2 + listName.length());
				properties.add(property);
			}
		}	
		listMap.put("columnPosition", postions);
		listMap.put("properties", properties);
		return listMap;
	}
	
	/**
	 * 在rowPosition下新插入一行，并且将指定的值插入到指定的位置
	 * @param templeteSheet 模板EXCEL文件的SHEET
	 * @param rowPosition 指定在这一行下面插入一新行
	 * @param cols 在哪些列中赋值
	 * @param properties 指定的属性名
	 * @param dataMap 数据MAP
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void insertNewRow(WritableSheet templeteSheet , int rowPosition , List cols , List properties , Map dataMap) throws RowsExceededException, WriteException{
		templeteSheet.insertRow(rowPosition + 1);
		for(int i=0;i<cols.size();i++){
			int colPosition = Integer.parseInt((String)cols.get(i));
			Label c = new Label(colPosition , rowPosition + 1 , (String) dataMap.get((String) properties.get(i))); 
			templeteSheet.addCell(c);
		}
	}
}
