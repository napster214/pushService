package com.jadlsoft.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ListFilter implements Filter {

	private String suffix = "list.do"; // 后缀
	
	private String jsonsuffix = "listjson.do";

	private String commonlist = "/commonlist.do"; // 基本列表URL
	
	private String commonjsonlist = "/commonlistjson.do"; // 

	private String realpathparamter = "realservletpath"; // 实际URL参数名称

	public void init(FilterConfig config) throws ServletException {
		String value = config.getInitParameter("suffix");
		if (value != null && value.trim().length() > 0) {
			suffix = value.trim();
		}
		value = config.getInitParameter("commonlist");
		if (value != null && value.trim().length() > 0) {
			commonlist = value.trim();
		}
		value = config.getInitParameter("realpathparamter");
		if (value != null && value.trim().length() > 0) {
			realpathparamter = value.trim();
		}
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String servletPath = ((HttpServletRequest) request).getServletPath();
		if(servletPath.endsWith(jsonsuffix) && !servletPath.equals(commonjsonlist)){
			request.setAttribute(realpathparamter, servletPath);
			RequestDispatcher dispatch = request.getRequestDispatcher(commonjsonlist);
			dispatch.forward(request, response);
		}else if (servletPath.endsWith(suffix) && !servletPath.equals(commonlist)) {
			request.setAttribute(realpathparamter, servletPath);
			RequestDispatcher dispatch = request.getRequestDispatcher(commonlist);
			dispatch.forward(request, response);
		} else {
			// 传递控制到下一个过滤器
			chain.doFilter(request, response);
		}
	}
	
	public void destroy() {
	}
}
