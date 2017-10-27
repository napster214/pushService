package com.jadlsoft.struts.action.tjbb;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jadlsoft.business.tjbb.TjbbBaseManager;

public class TjbbBaseAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception	{
		response.reset();
		response.setContentType ( "application/vnd.ms-excel;charset=UTF-8");
		String code = (String) request.getAttribute("code");
		Map data = (Map) request.getAttribute("data");		
		OutputStream out = response.getOutputStream();
		
		TjbbBaseManager tjbbBaseManager = new TjbbBaseManager();
		tjbbBaseManager.templete(code, out, data);
		
		out.flush();
		out.close();
		
		return null;
	}
}
