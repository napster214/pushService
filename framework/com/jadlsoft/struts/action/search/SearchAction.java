/**
 * <p>Title: BaseListAction</p>
 * <p>Description: 列表Action的基础类 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-8
*/

package com.jadlsoft.struts.action.search;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jadlsoft.business.CommonListConfigInterface;
import com.jadlsoft.business.search.SearchManager;
import com.jadlsoft.dbutils.SqlMapConfig;
import com.jadlsoft.domain.UserSessionBean;
import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.IConstants;
import com.jadlsoft.utils.SQLBuilder;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.UserSessionUtils;

/**
 * @SearchListAction.java 查询页列表显示用的Action
 * @作者 田智武
 * @日期 Oct 31, 2006
 */
public class SearchAction extends Action {
	private static Logger log = Logger.getLogger(SearchAction.class);
	protected static Map searchMap = new HashMap();
	
	private static int linecount = 20;
	//格式为：条件~值~条件~值，保证配对，用~隔开
	protected Map conditions = new HashMap();
	
	static {
		loadConfig();
	}

	private SearchManager searchManager;
	
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}
	
	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return doGetList(mapping, form, request, response);
	}
	
	/**
	 * Description: 实际执行的获取列表操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * 2006-9-8
	 */
	public ActionForward doGetList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm sform = (DynaActionForm) form;
		//根据表单数据构造查询语句
		
//		获取表单forward参数
		String type = sform.getString("forward");
		if(searchMap.containsKey(type)){
//			根据type去search.xml取对应的type节点所有子节点
			Map itemmap = (Map) searchMap.get(type);
			
//			获取子节点的值
			String sppagesize = (String) itemmap.get("pagesize");

			int pagesize = StringUtils.convertIntDef((String) sform.get("pageSize"), StringUtils.convertIntDef(sppagesize, linecount));
			if (pagesize<=0) pagesize = linecount;
			int pageno = StringUtils.convertIntDef((String)sform.get("pageNo"),1);
			if(pageno<=0) pageno=1;
			
			String choice = sform.getString("choice");
			if(choice==null || choice.length()==0)
				choice = "first";
			if(choice.equals("prev")) {
				pageno--;
			} else if(choice.equals("next")) {
				pageno++;
			} else if(choice.equals("first")) {
				pageno = 1;
			} else if(choice.equals("last")) {
				pageno = 0;  //后面计算页码
			}
			
			String conds = null;
			if(sform.getString("baseconditions").length()>0 && sform.getString("queryparamter").length()>0) {
				conds = sform.getString("baseconditions")+ "~" + sform.getString("queryparamter");
			} else {
				conds = sform.getString("baseconditions")+ sform.getString("queryparamter");
			}
			//SELECT dm,mc FROM T_DM_XZQH WHERE 1=1 and dm like '32%'
			//组织sql语句：createSQL（表名、条件、结果列）
			String table = (String)itemmap.get("table");
			/*
			 * 张方俊 2008-11-24 增加修改table功能
			 */
			String className = (String) itemmap.get("class");
			if(className != null && !"".equals(className)){
				Class clz = null;
				CommonListConfigInterface cfi = null;
				try{
					clz = Class.forName(className);
					cfi = (CommonListConfigInterface) clz.newInstance();
					List condsList = new ArrayList();					
					UserSessionBean user = UserSessionUtils.getCurrentSessionUser(request.getSession());
					condsList.add(user.getXzqh());
					condsList.add(conds);
					table = cfi.transTableName(table, condsList, null,user);
				} catch (ClassNotFoundException e){					
					e.printStackTrace();
				} catch (InstantiationException e){					
					e.printStackTrace();
				} catch (IllegalAccessException e){
					e.printStackTrace();
				}				 
			}
			/*
			 * 张方俊 2008-11-24 增加完成
			 */
			String sql = SQLBuilder.createSQL(table, conds, (String)itemmap.get("resultcol"));
			if(sql == null||sql.equals("")){
				log.info("查询参数错误导致不能构造sql");
				return mapping.findForward(IConstants.FAILURE_KEY);
			}
			if(itemmap.containsKey("filter")) {
				if(sql.indexOf("WHERE")>0) {
					sql = sql + " AND ";
				} else {
					sql = sql + " WHERE ";
				}
				sql = sql + itemmap.get("filter");
			}
			String orderby = (String) itemmap.get("orderby");
			if(orderby!=null && orderby.length()>0){
				sql += " ORDER BY " + orderby;
			}
			//added by zhaohuibin 2015-06-16 增加对应用服务器时间的支持。
			sql = sql.replaceAll(":today", "'" + DateUtils.getCurrentData() + "'");
			sql = sql.replaceAll(":nowtime", "'" + DateUtils.getCurrentDataTime() + "'");
			int count = 0;
			List result = null;
			/*add by 张俊吉 at 2008-10-06 如果不是第一次查询，不计算总数*/
			String sTotal = sform.getString("pageTotal");
			if(sTotal!=null && !"".equals(sTotal)){
				count = Integer.parseInt(sTotal);
			}
			try {
				if(count == 0){
					count = searchManager.getSearchCount(sql);
					}
				//需要检查页码，0表示最后一页
				if(pageno<=0 || pageno * pagesize >= count) {
					pageno = (int)((count+pagesize-1)/pagesize);
				}
				result= searchManager.getSearchList(sql, (pageno-1)*pagesize, pagesize);
			} catch(Exception e) {
				log.error("选择查询错误！", e);
			}
			
			request.setAttribute("total", new Integer(count));	//总条数
			request.setAttribute("list", result);				//结果列表
			request.setAttribute("pagesize", new Integer(pagesize));//每页条数
			String[] titles = ((String)itemmap.get("title")).split(",");
			request.setAttribute("titles", titles);				//标题
			request.setAttribute("titlelength", new Integer(titles.length));	//标题数量
			String[] resultcols = (itemmap.containsKey("resultshowcol") ? ((String) itemmap.get("resultshowcol")) : ((String) itemmap.get("resultcol"))).split(",");
			for(int i=0;i<resultcols.length;i++){
				if(resultcols[i].indexOf(" ")>0) {
					resultcols[i] = resultcols[i].substring(resultcols[i].indexOf(" ")+1);
				}
			}
			request.setAttribute("resultcol", resultcols);		//查询结果列
			request.setAttribute("searchcol", itemmap.get("searchcol"));	//搜索列
			request.setAttribute("searchcond", itemmap.get("searchcond"));	//搜索条件操作
			request.setAttribute("searchtitle", ((String)itemmap.get("searchtitle")).split(","));	//搜索标题
			if(itemmap.containsKey("colwidth")){
				request.setAttribute("colwidth", ((String)itemmap.get("colwidth")).split(","));	//搜索标题
			}
			request.setAttribute("queryparamter", sform.getString("queryparamter"));
			if(itemmap.containsKey("forward")){
				return mapping.findForward((String)itemmap.get("forward"));
			}
		}
		if("1".equals(sform.get("multiselect"))){
			return mapping.findForward("SuccessMulti");
		}else{
			return mapping.findForward(IConstants.SUCCESS_KEY);
		}
	}
	
	/**
	 * Description: 将配置文件调入内存
	 * 2006-11-10
	 */
	private static void loadConfig() {
		URL url = SqlMapConfig.class.getResource("/search.xml");
		String configFile = url.getFile();
		File file = null;
		try {
			file = new File(URLDecoder.decode(configFile, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if(file.exists() && file.isFile() && file.canRead()) {
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				Iterator item = root.elementIterator();
				while(item.hasNext()) {
					Element searchitem = (Element) item.next();
					String name=searchitem.getName();
					if(name.equals("linecount")) {
						linecount = StringUtils.convertIntDef(searchitem.attributeValue("value"),20);
					} else {
						Map itemmap = new HashMap();
						Iterator prop = searchitem.elementIterator();
						while(prop.hasNext()) {
							Element propitem = (Element) prop.next();
							itemmap.put(propitem.getName(), propitem.getTextTrim());
						}
						
						searchMap.put(name, itemmap);
					}
				}
				log.info("成功读取选择查询配置文件！");
			} catch (DocumentException e) {
				log.error("读取Search配置错误！", e);
			}
		}
	}
}
