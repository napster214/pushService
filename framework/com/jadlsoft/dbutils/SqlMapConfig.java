package com.jadlsoft.dbutils;

import java.io.File;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <p>Title: SqlMapConfig.java</p>
 * <p>Description: 加载预先设置好的SQL语句，在系统中使用</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 京安丹灵</p>
 * @author Li Banggui
 * @version 1.0
 * May 21, 2008
 */
public class SqlMapConfig {

	private static Map sqlMap = null; //查询Map

	private static Logger log = Logger.getLogger(SqlMapConfig.class);

	private static String configfile = "/sqlmapconfig.xml";

	/**
	 * Description：从XML文件中读取字段的字典配置，加入到已有的配置中（可能覆盖）
	 * @param fileName XML配置文件
	 * <pre>配置文件格式：
	 * &lt;XML&gt;
	 * 		&lt;namespace&gt;
	 * 			&lt;select id="aaa" &gt;
	 * 				select * from xxxx
	 * 			&lt;/select&gt;
	 * 			&lt;update id="bbb" &gt;
	 * 				update xxx set aa=:aa,bb=:bb
	 * 			&lt;/update&gt;
	 * 		&lt;/namespace&gt;
	 * &lt;XML&gt;</pre>
	 * <XML>
	 * 		<namespace>
	 * 			<select id="aaa" >
	 * 				select * from xxxx
	 * 			</select>
	 * 			<update id="bbb" >
	 * 				update xxx set aa=:aa,bb=:bb
	 * 			</update>
	 * 		</namespace>
	 * <XML>
	 */
	public static void loadConfig(String fileName) {
		if (sqlMap == null) {
			sqlMap = new LinkedHashMap(); //HashMap ???
		}
		synchronized (sqlMap) {
			if (!fileName.startsWith("/")) {
				fileName = "/" + fileName;
			}
			URL url = SqlMapConfig.class.getResource(fileName);
			String configFile = url.getFile();

			try {
				configFile = URLDecoder.decode(configFile, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("获取SQL映射文件错误！");
				e.printStackTrace();
			}

			File file = new File(configFile.replaceAll("%20", " "));
			if (file.exists() && file.isFile() && file.canRead()) {
				SAXReader reader = new SAXReader();
				try {
					Document doc = reader.read(file);
					Element root = doc.getRootElement();
					String nm = root.getName();
					Iterator item = root.elementIterator();
					while (item.hasNext()) {
						Element sqlitem = (Element) item.next();
						String type = sqlitem.getName().trim().toLowerCase();
						String id = sqlitem.attributeValue("id").trim();
						String sql = sqlitem.getTextTrim();

						if ("select".equals(type) || "update".equals(type)) {
							sqlMap.put(type + "#" + nm + "." + id, sql);
						}
					}
				} catch (DocumentException e) {
					log.error("读取SQL映射配置文件 " + fileName + " 错误！", e);
				}
			}
		}

	}

	public static String getSql(String type, String id) {
		if (sqlMap == null) {	// 如果所有sqlmapconfig*.xml配置文件内容为空，此处会造成死循环！！！
			loadConfig(configfile);
		}
		return (String) sqlMap.get(type + id);
	}

//	public static void main(String[] args) {
//		SqlMapConfig.loadConfig("sqlmapconfig.xml");
//	}
}
