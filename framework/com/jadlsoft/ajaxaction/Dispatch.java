/**
 * <p>Title: SetDatadicPlugin</p>
 * <p>Description: 响应AJAX请求，返回数据字典内容</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-5
 */

package com.jadlsoft.ajaxaction;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ContextLoaderPlugIn;

import com.jadlsoft.dbutils.BlobUtils;

public class Dispatch extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(Dispatch.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 处理： 
	 * 获取的参数格式：dicname=...&dm=colname&value=colname&cond=vv 
	 *     dicname: 字典名称 
	 *          dm: 代码列 
	 *       value: 值列 
	 *        cond: 条件表达式，使用~分隔多个条件，每个条件分3个域，用#隔开，分别为coldcol, oper, condvalue 
	 *     condcol: 条件列 
	 *        oper: 条件操作，equal=, great>, less<, like 
	 *   condvalue: 条件比较值，like需要加上* 
	 *      status: 0正常, 1系统错误, 2操作结果错误, 3过期/存在（用于检查是否存在）
	 * 返回的统一格式：<result><status></status><content></content></result>
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * if(request.getSession().getAttribute("user")==null) {
		 * //session过期，重新登陆 String result="<?xml version=\"1.0\"
		 * encoding=\"GBK\"?><result><status>3</status><content>会话过期，请重新登陆</content></result>";
		 * response.setContentType("text/xml"); OutputStream out =
		 * response.getOutputStream();
		 * 
		 * out.write(result.getBytes()); out.flush(); out.close(); return; }
		 */
		String action = (String) request.getParameter("action");
		if (action == null || action.length() == 0) {
			returnSimpleErrorResult("命令不正确！", response);
		} else {
			if (action.equalsIgnoreCase("getdic")) { // 获取数据字典
				getDicData(request, response);
			} else if (action.equalsIgnoreCase("getdata")) { // 从数据库中获取数据
				getDataContent(request, response);
			} else if (action.equalsIgnoreCase("checkdataexist")) { // 检查数据库数据存在
				checkDataExist(request, response);
			}
		}
	}

	/**
	 * Description: 获取数据字典数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getDicData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String dicname = (String) request.getParameter("dicname");
			String dm = (String) request.getParameter("dm");
			String value = (String) request.getParameter("value");
			// 更改行政区划标志
			String cond = ((String) request.getParameter("cond"));
			List dic = (List) ((Map) this.getServletContext().getAttribute("dic")).get(dicname);
			String result = DicManager.getDicContent(dic, dm, value, cond);
			returnResult(response, result);
		} catch (Exception e) {
			returnErrorResult(response, e);
		}
	}

	/**
	 * Description: 返回信息
	 * 
	 * @param response 信息内容
	 * @param result
	 * @throws IOException
	 */
	private void returnResult(HttpServletResponse response, String result) throws IOException {
		response.setContentType("text/xml");
		OutputStream out = response.getOutputStream();
		out.write(result.getBytes("GBK"));
		out.flush();
		out.close();
	}

	/**
	 * Description: 返回错误信息
	 * 
	 * @param response 信息内容
	 * @param e
	 * @throws IOException
	 */
	private void returnSimpleErrorResult(String errinfo, HttpServletResponse response) throws IOException {
		log.error("AJAX处理数据错误！" + errinfo);
		String result = null;
		if (errinfo == null || errinfo.length() == 0) {
			result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>1</status><content>获取服务器信息出错,请和管理员联系</content></result>";
		} else {
			result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>1</status><content>" + errinfo + "</content></result>";
		}
		returnResult(response, result);
	}

	/**
	 * Description: 返回错误信息
	 * 
	 * @param response 信息内容
	 * @param e
	 * @throws IOException
	 */
	private void returnErrorResult(HttpServletResponse response, Exception e) throws IOException {
		log.error("AJAX处理数据错误！", e);
		String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>1</status><content>获取服务器信息出错,请和管理员联系</content></result>";
		returnResult(response, result);
	}

	/**
	 * Description: 获取数据字典数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void checkDataExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>0</status><content></content></result>";
			String dataname = (String) request.getParameter("dataname");
			String cond = ((String) request.getParameter("cond"));
			result = DataloadManager.checkExistContent(getSpringDatasource(), dataname, cond);
			returnResult(response, result);
		} catch (Exception e) {
			returnErrorResult(response, e);
		}
	}

	/**
	 * Description: 获取数据字典数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getDataContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>0</status><content></content></result>";
			String dataname = (String) request.getParameter("dataname");
			String cols = (String) request.getParameter("cols");
			String cond = ((String) request.getParameter("cond"));
			String blobflag = (String) request.getParameter("blobflag");
			result = DataloadManager.getDataContent(getSpringDatasource(), (BlobUtils) getSpringBean("blobUtils"), dataname, cols, cond, (blobflag != null && blobflag.equals("1") ? true : false));
			returnResult(response, result);
		} catch (Exception e) {
			returnErrorResult(response, e);
		}
	}

	/**
	 * Description: 获取Spring容器中配置的数据源
	 * 
	 * @return 数据源
	 */
	private DataSource getSpringDatasource() {
		return (DataSource) getSpringBean("dataSource");
	}

	private Object getSpringBean(String beanid) {
		WebApplicationContext wac = (WebApplicationContext) this.getServletContext().getAttribute(ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX);
		if (wac == null) {
			log.info("AJAX Servlet无法获取Spring容器！");
			return null;
		}
		return wac.getBean(beanid);
	}
}
