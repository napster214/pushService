package com.jadlsoft.utils;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

public class ResponseUtils {
    /**
     * @Description 将json字符串写入到response中
     * @param response
     *            HttpServletResponse
     * @param jsonString
     *            json字符串
     * @throws Exception
     */
    public static void render(HttpServletResponse response, String jsonString) throws Exception {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }
    
    /**
     * 将resultbean返回页面，适用于ajax请求
     * @param resultBean	
     * @param response
     * @param statusCode
     * @param msg
     * @return: void
     */
    public static void renderResult(ResultBean resultBean, HttpServletResponse response,
			String statusCode, String msg) {
    	resultBean = new ResultBean(statusCode, msg);
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
