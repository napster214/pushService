/**
 * <p>Title: BaseListAction</p>
 * <p>Description: 列表Action的基础类 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-8
 */

package com.jadlsoft.struts.action;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.jadlsoft.business.CommonListManager;
import com.jadlsoft.domain.UserSessionBean;
import com.jadlsoft.utils.IConstants;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.UserSessionUtils;
import com.jadlsoft.utils.UserUtils;
import com.jadlsoft.utils.XzhqhUtils;

public class BaseListAction extends Action {
	private static Logger log = Logger.getLogger(BaseListAction.class);

	private static Map forwardMap = new HashMap(); //forward config

	private static int listcount = 1000; //save list count

	protected CommonListManager listManager;

	//系统启动加载
	static {
		getListConfig();
	}
	
	public static Map getForwardMap(){
		return forwardMap;
	}
	
	/**
	 * Description: 将配置文件调入内存
	 * XML format:
	 * <configs>
	 * 	<forward request="XXX.do" success="XXXX.jsp" datasource="" title="XXXX" field="XXXX" />
	 *  <listcount value="1000" /> //max record count for save as Excel
	 * </configs>
	 */
	private static void getListConfig() {
		/*
		 * 张方俊 2008－08－11 增加listConfig的多个配置文件功能
		 */
		URL fileUrl = BaseListAction.class.getResource("/listConfig.xml");
		String fileUrlFile = "";
		try {
			fileUrlFile = URLDecoder.decode(fileUrl.getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("文件路径转换错误！");
			e1.printStackTrace();
		}
		File fileParent = new File(fileUrlFile).getParentFile();
		String[] listConfigFiles = fileParent.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.startsWith("listConfig") && name.endsWith(".xml")) {
					return true;
				}
				return false;
			}
		});
		for (int i = 0; i < listConfigFiles.length; i++) {
			String fileName = listConfigFiles[i];
			try {
				URL url = BaseListAction.class.getResource("/" + fileName);
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				File file = new File(filePath);
				if (file.exists() && file.isFile() && file.canRead()) {
					SAXReader reader = new SAXReader();
					try {
						Document doc = reader.read(file);
						Element root = doc.getRootElement();
						Iterator item = root.elementIterator();
						while (item.hasNext()) {
							Element column = (Element) item.next();
							String name = column.getName();
							if (name.equals("forward")) {
								String request = column.attributeValue("request");
								String success = column.attributeValue("success");
								String datasource = column.attributeValue("datasource");
								String title = column.attributeValue("title");
								String field = column.attributeValue("field");
								String defaultCondition = column.attributeValue("defaultcondition");
								String requireCondition = column.attributeValue("requirecondition");
								/*
								 * 张方俊 2008－07－11 增加对数据源的处理
								 */
								String className = column.attributeValue("class");
								/*
								 * 张方俊 2008－07－11 增加 完成
								 */
								/*
								 * 张方俊 2012－09－24 增加对查询结果进行处理
								 */
								String afterClass = column.attributeValue("afterClass");
								/*
								 * 张方俊 2008－07－11 增加 完成
								 */
								//if has more properties, use map
								Map datamap = null;
								if (forwardMap.containsKey(request)) {
									datamap = (Map) forwardMap.get(request);
								} else {
									datamap = new HashMap();
								}
								datamap.put("success", success);
								datamap.put("datasource", datasource);
								datamap.put("title", title);
								datamap.put("field", field);
								datamap.put("defaultCondition", defaultCondition);
								datamap.put("requireCondition", requireCondition);
								datamap.put("class", className);
								datamap.put("afterClass", afterClass);
								forwardMap.put(request, datamap);
							} else {
								if (name.equals("listcount")) {
									listcount = StringUtils.convertIntDef(column.attributeValue("value"), 1000);
								}
							}
							//forwardMap.put(request, success);
						}
					} catch (DocumentException e) {
						log.error("读取List映射关系错误！", e);
					}
				}
			} catch (Throwable t) {
				// Ignore
			}
		}
	}
	
	//(2)	 执行玩static块后注入com.jadlsoft.business.CommonListManager
	public void setListManager(CommonListManager listService) {
		this.listManager = listService;
	}
	
	/** 
	 * Method execute:每次请求的时候执行
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		/*
		 * 张方俊 于 2009-08-21 为避免多线程引起的问题，将全局变量转为局部变量
		 */
		int pagesize; //每页数量

		int pageno; //页数

		int pagetotal; //页数
		
		boolean resume = false; //是否中途返回

		Map params = null; //中途返回时的参数

		boolean savelist = false; //是否列表保存
		
		List condList = new ArrayList();	
		
		//2016-12-7 add 处理传递 "车牌号",在页面显示
		String req_cph = request.getParameter("cph")!=null?request.getParameter("cph").toString():"";
		String cph="";
		try {
			cph = java.net.URLDecoder.decode(req_cph,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("cph", cph);
		
		
		/*
		 * 张方俊 于 2009-08-21 转移结束
		 */		
		
		String realServletPath = (String) request.getAttribute("realservletpath");

		if (!forwardMap.containsKey(realServletPath)) {
			return mapping.findForward(IConstants.FAILURE_KEY);
		}
		Map forwardinfo = (Map) forwardMap.get(realServletPath);
		
		/* 张方俊 2008年11月11日。某些地方的LIST列表和操作在同一页面，所以首次的页面总数必须刷新。
		if (request.getParameter("choice") == null && request.getMethod().equals("GET") && request.getQueryString() == null) {
			//是否页面相同
			String pageURI = (String) request.getSession().getAttribute("pageURI");
			if (pageURI != null && pageURI.endsWith(".jsp")) {
				pageURI = pageURI.replaceAll(".jsp", ".do");
			}
			
			//String reqURI = request.getRequestURI();
			//if (reqURI.equalsIgnoreCase(pageURI))
			if (realServletPath.equalsIgnoreCase(pageURI)) {				
				//resume = true;
			} else {
				resume = false;
			}
		}
		张方俊 2008年11月11日 屏蔽结束
		*/
		
		if (resume) {
			Object obj = request.getSession().getAttribute("pagerParameters");
			if (obj != null) {
				params = (HashMap) obj;
				//获取每页记录数,若没指定,使用默认值
				pagesize = ((Integer) params.get("pageSize")).intValue();
				//获取页号
				pageno = ((Integer) params.get("pageNo")).intValue();
				/*
				 * 张方俊 2008-09-23 查询记录总数
				 */
				pagetotal = ((Integer) params.get("pageTotal")).intValue();
			} else {
				//提供默认值(一般出在用GET方法查询且不需参数情况的处理,因为条件等同于转出后恢复)
				pagesize = 10;
				pageno = 1;
				/*
				 * 张方俊 2008-09-23 查询记录总数
				 */
				pagetotal = -1;
			}

			//获取参数
			condList.clear();
			if (!"null".equals(params.get("queryparamter")) && !getConditions((String) params.get("queryparamter"), UserUtils.getUserSessionBean(request), condList)) {
				return mapping.findForward(IConstants.FAILURE_KEY);
			}
			getConditions("&&" + (String) forwardinfo.get("requireCondition"), UserUtils.getUserSessionBean(request), condList);
			request.setAttribute("queryparamter", params.get("queryparamter"));
			request.setAttribute("conditions", params.get("conditions"));
		} else {
			DynaActionForm listForm = (DynaActionForm) form;			
			//此处设置每页条数，默认为10
			pagesize = StringUtils.convertIntDef((String) listForm.get("pageSize"), 10);
			if (pagesize <= 0) {
				pagesize = 10;
			}
			pageno = StringUtils.convertIntDef((String) listForm.get("pageNo"), 1);
			if (pageno <= 0) {
				pageno = 1;
			}
			/*
			 * 张方俊 2008-09-23 查询记录总数
			 */
			pagetotal = StringUtils.convertIntDef((String) listForm.get("pageTotal"), -1);
			
			String choice = (String) listForm.get("choice");
			if (choice == null || choice.length() == 0)
				choice = "first";
			if (choice.equals("prev")) {
				pageno--;
			} else if (choice.equals("next")) {
				pageno++;
			} else if (choice.equals("first")) {
				pageno = 1;
			} else if (choice.equals("last")) {
				pageno = 0; //后面计算页码
			}

			if (listForm.get("queryparamter") == null || "null".equals(listForm.get("queryparamter"))) {
				listForm.set("queryparamter", "&&" + forwardinfo.get("defaultCondition"));
			}
			condList.clear();
			if (!getConditions((String) listForm.get("queryparamter"), UserUtils.getUserSessionBean(request), condList)) {
				return mapping.findForward(IConstants.FAILURE_KEY);
			}
			getConditions("&&" + (String) forwardinfo.get("requireCondition"), UserUtils.getUserSessionBean(request), condList);
			if ((listForm.get("queryparamtername") != null) && !"".equals(listForm.get("queryparamtername"))) {
				request.setAttribute("queryparamtername", listForm.get("queryparamtername"));
			} else {
				request.setAttribute("queryparamtername", "");
			}
			request.setAttribute("queryparamter", listForm.get("queryparamter"));
		}
		//修改跳转后的提示信息
		if (request.getSession().getAttribute(IConstants.SYSMESSAGE_KEY) != null && !request.getSession().getAttribute(IConstants.SYSMESSAGE_KEY).equals("")) {
			request.setAttribute(IConstants.SYSMESSAGE_KEY, request.getSession().getAttribute(IConstants.SYSMESSAGE_KEY));
			request.getSession().removeAttribute(IConstants.SYSMESSAGE_KEY);
		}

		//判断是否保存列表操作
		savelist = false;
		if (request.getParameter("saveaction") != null && request.getParameter("saveaction").equals("savelist")) {
			pageno = 1;
			pagesize = listcount;
			savelist = true;
			/*
			 * 张方俊 2008-09-23 查询记录总数。导出EXCEL时，将记录总数设置为最大
			 */
			pagetotal = listcount;
			//获取配置信息
			getFieldList(request);
		}
		
		//2016-12-4 add 传递返回值(显示操作成功)
		/*if(pageno == 1){
			String bakstr = request.getParameter("bakstr")!=null?request.getParameter("bakstr").toString():"";
			request.setAttribute("bakstr", bakstr);
		}*/
		
		/*
		 * 张方俊 2008-09-23 x_count用于判断是否是计算总量
		 */
		String count = request.getParameter("x_count");
		if("yes".equals(count)){
			return doGetCount(mapping, savelist, request, response, condList, pagesize, pageno, pagetotal);
		}
		return doGetList(mapping, savelist, request, response, condList, pagesize, pageno, pagetotal);
	}
	
	/**
	 * Description: 将查询条件解析成Map或List，保存在类成员conditions/condlist中
	 * @param condition 条件字符串
	 * 2007-10-12
	 * 条件参数使用queryparamter来获得，需要在前台javascript中放在一个隐藏域中，
	 * 格式为：条件~值~条件~值，保证配对，用~隔开
	 * 或者 &&条件~操作~值~条件~操作~值，保证3个一组
	 * modified by zhaohuibin 2013-05-22 加入单位卡编号和单位代码条件过滤，适用于硝酸铵系统，dwkbh用于过滤本登录用户数据，shortdwkbh用于过滤登录用户所属单位数据
	 * .replaceAll("\\Q[$dwkbh]\\E", user.getKbh()).replaceAll("\\Q[$shortdwkbh]\\E", user.getDwdm())
	 */
	private boolean getConditions(String condition, UserSessionBean user, List condList) {
		//conditions.clear();
		//condList.clear();
		if (condition == null || condition.length() == 0)
			return true;
		if (condition.startsWith("&&")) {
			//三个一组的条件
			String[] conds = condition.substring(2).split("~");
			if (conds.length % 3 > 0) {
				return false;
			}
			for (int i = 0; i < conds.length / 3; i++) {
				/*
				 * 张方俊 2010-08-24 用于未登录系统时的调用
				 */
				if(user == null || user.getXzqh() == null){
					condList.add(conds[3 * i] + "~" + conds[3 * i + 1] + "~" + conds[3 * i + 2]);
				}else{
					condList.add(conds[3 * i] + "~" + conds[3 * i + 1] + "~" + getReplaceConditions(conds[3 * i + 2],user));
				}
			}
		} else {
			//两个一组的条件
			String[] conds = condition.split("~");
			if (conds.length % 2 > 0) {
				return false;
			}
			for (int i = 0; i < conds.length / 2; i++) {
				//conditions.put(conds[2*i], conds[2*i+1]);
				/*
				 * 张方俊 2010-08-24 用于未登录系统时的调用
				 */
				if(user == null || user.getXzqh() == null){
					condList.add(conds[2 * i] + "~=~" + conds[2 * i + 1]);
				}else{
					condList.add(conds[2 * i] + "~=~" + getReplaceConditions(conds[2 * i + 1],user));
				}
			}
		}
		return true;
	}

	/**
	 * Description: 从配置文件中获取保存列表中字段名列表
	 * @param request
	 * 2006-11-10
	 */
	private void getFieldList(HttpServletRequest request) {
		String reqURI = (String) request.getAttribute("realservletpath");
		Map datamap = (Map) forwardMap.get(reqURI);
		if (datamap != null) {
			request.setAttribute("titlelist", ((String) datamap.get("title")).split(","));
			request.setAttribute("fieldlist", ((String) datamap.get("field")).split(","));
		}
	}
	
	/**
	 * 张方俊 2008-09-23 记录总数
	 */
	protected ActionForward doGetCount(ActionMapping mapping, boolean isSavelist, HttpServletRequest request, HttpServletResponse response, List condList, int pagesize, int pageno, int pagetotal) {
		String realServletPath = (String) request.getAttribute("realservletpath");
		if (forwardMap.containsKey(realServletPath)) {
			Map forwardinfo = (Map) forwardMap.get(realServletPath);
			Map data = null;
			try {
				//data = listManager.getDataCountAndList(conditions, condList, (String) forwardinfo.get("datasource"), pagesize, pageno);
				data = listManager.getDataCount(null, condList,UserSessionUtils.getCurrentSessionUser(request.getSession()),(String) forwardinfo.get("datasource"), (String) forwardinfo.get("class"), pagesize, pageno, pagetotal);
			} catch (Exception e) {
				log.error("获取信息列表失败！", e);
			}
			
			request.setAttribute("total", data.get("total"));
			request.setAttribute("list", data.get("list"));
			request.setAttribute("pagesize", new Integer(pagesize));
			/*
			 * 张方俊 2008年10月22日 增加显示方式
			 */
			String pageType = request.getParameter("pageType");
			if(pageType != null && !"".equals(pageType)){
				request.setAttribute("pageType", pageType);
			}
			
			return mapping.findForward("x_count");
		}
		return null;
	}


	/**
	 * Description: 实际执行的获取列表操作
	 * @return
	 * 2006-9-8
	 */
	protected ActionForward doGetList(ActionMapping mapping, boolean isSavelist, HttpServletRequest request, HttpServletResponse response, List condList, int pagesize, int pageno, int pagetotal) {
		String realServletPath = (String) request.getAttribute("realservletpath");
		if (forwardMap.containsKey(realServletPath)) {
			Map forwardinfo = (Map) forwardMap.get(realServletPath);
			Map data = null;
			try {
				//data = listManager.getDataCountAndList(conditions, condList, (String) forwardinfo.get("datasource"), pagesize, pageno);
				/*
				 * 张方俊 2008-09-23 计算记录列表
				 * 张方俊 2012年09月24日 增加对查询结果的处理
				 */
				data = listManager.getDataList(null, condList, UserSessionUtils.getCurrentSessionUser(request.getSession()),(String) forwardinfo.get("datasource"), (String) forwardinfo.get("class"), (String) forwardinfo.get("afterClass"), pagesize, pageno, pagetotal);
			} catch (Exception e) {
				log.error("获取信息列表失败！", e);
			}
			
			request.setAttribute("total", data.get("total"));
			request.setAttribute("list", (List)data.get("list"));
			request.setAttribute("pagesize", new Integer(pagesize));
			if (isSavelist) {
				return mapping.findForward(IConstants.SAVELIST_KEY);
			}
			RequestDispatcher dispatch = request.getRequestDispatcher((String) forwardinfo.get("success"));
			try {
				dispatch.forward(request, response);
			} catch (Exception e) {
				log.error("ListAction get forward error!", e);
				return mapping.findForward(IConstants.SUCCESS_KEY);
			}
		}
		return null;
	}
	
	/**
	 * @功能：条件值变量替换
	 * @参数：
	 * @param condition 需替换条件值变量的条件
	 * @param user 登录用户信息
	 * @return
	 * @返回值：String 替换完$xzqh、$shortxzqh、$gaglkbh、$dwkbh、$shortdwkbh后的条件值
	 * create by zhaohuibin 2013-5-23 上午09:35:12
	 */
	private String getReplaceConditions(String condition, UserSessionBean user){
		try{
			String xzqh = user.getXzqh();
			if(IConstants.YHLX_QY.equals(user.getYhlx())){
				xzqh = user.getUserid();
			}
			condition = condition.replaceAll("\\Q[$xzqh]\\E", xzqh);
			
			String shortxzqh = XzhqhUtils.getXZHQH(user.getXzqh());
			if(IConstants.YHLX_QY.equals(user.getYhlx())){
				shortxzqh = XzhqhUtils.getShortYHBM(xzqh);
			}
			condition = condition.replaceAll("\\Q[$shortxzqh]\\E", shortxzqh);
			
			String kbh = user.getKbh();
			condition = condition.replaceAll("\\Q[$gaglkbh]\\E", kbh);
			condition = condition.replaceAll("\\Q[$dwkbh]\\E", kbh);
			
			String shortdwkbh = user.getDwdm();
			condition = condition.replaceAll("\\Q[$shortdwkbh]\\E", shortdwkbh);
			return condition;
		}catch(Exception e){//此处只有部分系统中的user不含dwdm时会有异常的可能，其他属性user都已经包含。
			log.error("替换条件值变量condition.replaceAll(\\Q[$shortdwkbh]\\E)等异常！", e);
			return condition;
		}
	}
	
}
