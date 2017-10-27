package com.jadlsoft.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML解析、生成
 * @类名: XMLUtils
 * @时间: 2017-8-1 下午3:08:47
 */
public class XMLUtils {
	
	/**
	 * @description 将xml字符串转换成map
	 * @param xml
	 * @return Map
	 */
	public static Map transXmlStringToMap(String xml) {
		Map map = new HashMap();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
//			System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
			Iterator iter = rootElt.elementIterator(); // 获取根节点下的子节点
			// 遍历根节点
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				String elementName = recordEle.getName();
				String elementValue = recordEle.getTextTrim();
//				System.out.println(elementName + "=" + elementValue);
				map.put(elementName, elementValue);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将map转换为xml字符串
	 * @param map
	 * @param useCDATA 是否启用CDATA，如果使用，就会在判断是字符串的值外面加上<![CDATA["   "]]>
	 * @return: String
	 */
	public static String mapToXML(Map map, boolean useCDATA) {
		StringBuffer sb = new StringBuffer();
		if (map == null || map.size()==0) {
			return sb.toString();
		}
		Set<Entry> entrySet = map.entrySet();
		sb.append("<xml>\n");
		int i = 0;
		for (Entry entry : entrySet) {
			sb.append("<").append(entry.getKey()).append(">");
			Object value = entry.getValue();
			//如果是String类型并且启用了CDATA，就加上
			if (value instanceof String && useCDATA) {
				sb.append("<![CDATA["+ entry.getValue() + "]]");
			}else {
				sb.append(entry.getValue());
			}
			sb.append("</").append(entry.getKey()).append(">");
			if (i < map.size()-1) {
				sb.append("\n");
			}
			i++;
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
	public static String addZKH(String srcStr) {
		return "<![CDATA["+srcStr+"]]>";
	}
}
