/**
 * com.jadlsoft.ajaxaction CheckData.java Nov 20, 2007 11:56:33 AM
 */
package com.jadlsoft.ajaxaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ContextLoaderPlugIn;

/**
 * @author sky 功能：处理新增报警
 * 
 */
public class CheckData extends HttpServlet {
	Logger log = Logger.getLogger(CheckData.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String message = "";
		String res = "";
		String dataname = (String) request.getParameter("dataname");
		String bjxxid = (String) request.getParameter("update");
		String gajno = (String) request.getParameter("gajno");

		if ("".equals(bjxxid) || bjxxid == null) {
			message = this.getXzBjxxid(getSpringDatasource(), dataname, gajno);
			if ("".equals(message)) {
				message = "none";// 当没有查到新增报警信息时，message设为none
			}
			res = "<message>" + message + "</message>";
		} else {
			this.updateBjzt(getSpringDatasource(), dataname, bjxxid);
			res = "<message>none</message>";// 修改完新增报警信息状态时，message设为none
		}

		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-Contorl", "no-cache");
		out.println("<response>");
		out.println(res);
		out.println("</response>");
		out.close();
	}

	/**
	 * updateBjzt() 功能：修改新增报警信息状态
	 * 
	 * @param bjxxid
	 *            报警信息编号 void
	 */
	private void updateBjzt(DataSource ds, String dataname, String bjxxid) {
		try {
			JdbcTemplate jt = new JdbcTemplate(ds);
			jt.execute("update t_" + dataname + " set xzbj=1 where bjxxglid="
					+ bjxxid);
		} catch (Exception e) {
			log.error("修改新增报警信息状态时异常！", e);
		}
	}

	/**
	 * getXzBjxxid() 功能：获取新增报警信息ID
	 * 
	 * @param ds
	 *            数据源
	 * @param dataname
	 *            数据库表名
	 * @return String
	 */
	private String getXzBjxxid(DataSource ds, String dataname, String gajno) {
		final StringBuffer strBuffer = new StringBuffer();
		try {
			JdbcTemplate jt = new JdbcTemplate(ds);
			RowCallbackHandler rowCallbackHandler = new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					strBuffer.append(rs.getInt("bjxxglid"));
					strBuffer.append("+");
					strBuffer.append(rs.getString("gajno"));
				}
			};
			StringBuffer strSelectSql = new StringBuffer();
			strSelectSql.append("Select bjxxglid,gajno from t_");
			strSelectSql.append(dataname);
			strSelectSql.append(" where xzbj='0'");
			strSelectSql.append(" and gajno='");
			strSelectSql.append(gajno);
			strSelectSql.append("'");
			strSelectSql.append(" and rownum < 2");

			jt.query(strSelectSql.toString(), rowCallbackHandler);

		} catch (Exception e) {
			log.error("获取新增报警信息ID异常！", e);
		}
		return strBuffer.toString();
	}

	/**
	 * Description: 获取Spring容器中配置的数据源
	 * 
	 * @return 数据源 2006-10-9
	 */
	private DataSource getSpringDatasource() {
		return (DataSource) getSpringBean("dataSource");
	}

	/**
	 * getSpringBean() 功能：IOC
	 * 
	 * @param beanid
	 * @return Object
	 */
	private Object getSpringBean(String beanid) {
		WebApplicationContext wac = (WebApplicationContext) this
				.getServletContext().getAttribute(
						ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX);
		if (wac == null) {
			log.info("AJAX Servlet无法获取Spring容器！");
			return null;
		}
		return wac.getBean(beanid);
	}
}
