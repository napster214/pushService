<%@ page language="java" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/taglib/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglib/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/taglib/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglib/page.tld" prefix="page"%>
<%
String pageType = (String) request.getAttribute("pageType");
if(pageType == null || pageType.equals("")) pageType = "TEXT";
%>
<page:pager total="${total}" defaultPageSize="${pagesize}">
	<page:navigator type="<%=pageType %>" />
</page:pager>