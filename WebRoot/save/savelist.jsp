<%@ page language="java" contentType="application/vnd.ms-excel; charset=GBK" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib uri="jadllogic.tld" prefix="jadllogic"%> 
 
<html:html> 
	<head>
		<meta http-equiv='Expires' content='0'> 
		<meta http-equiv='Pragma' content='No-cache'> 
		<meta http-equiv='Cache-Control' content='No-cache'>
		<meta http-equiv="Content-Type" content="text/html;charset=GBK">
		<title>列表保存</title>
	</head>
	<body>
		<table border="1">
		    <tr align="center">
			    <logic:iterate id="titleitem" name="titlelist">
			    <td><bean:write name="titleitem" /></td>
			    </logic:iterate>
		    </tr>
			<logic:present name="list">
		    <logic:iterate id="item" name="list">
		      <tr>
		    	<logic:iterate id="field" name="fieldlist">
<%
					String fieldname = (String)pageContext.getAttribute("field");
%>
		        <td>&nbsp;<bean:write name="item" property="<%=fieldname %>"/></td>
		        </logic:iterate>
		      </tr>
		    </logic:iterate>
			</logic:present>
		</table>
	</body>
</html:html>

