<%@ page language="java" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%
String pageType = (String) request.getAttribute("pageType");
if(pageType == null || pageType.equals("")) pageType = "TEXT";
%>
<page:pager total="${total}" defaultPageSize="${pagesize}">
	<page:navigator type="<%=pageType %>" />
</page:pager>