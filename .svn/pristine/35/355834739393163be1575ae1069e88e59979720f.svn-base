<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib uri="jadllogic.tld" prefix="jadllogic"%>
<%@ taglib uri="page.tld" prefix="page" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<META http-equiv=Content-Type content="text/html; charset=utf-8">
	<script type="text/javascript" src="../common/validator.js"></script>
	<script type="text/javascript" src="../common/tableManager.js"></script>
	<link rel="stylesheet" type="text/css" href="../common/total.css">

    <title>查询结果</title>

	<script type="text/javascript" src="../common/searchUtils.js"></script>
	
  </head>
  
  <body>
  <script type="text/javascript">
  var selectrow=0;
  var titlelen = 0;
  //提交查询前设置查询条件
  function checkparamter()
  {
  	return setSearchParamter({searchcol:document.getElementById("searchcol").value,
  			searchoper:document.getElementById("searchcond").value,
  			searchfield:'searchfield'});
  }
  
  //双击行事件
  function dblClick(obj){
  	onSearchdblClick(obj, {fields:document.getElementById("searchfields").value,
  			targets:document.getElementById("targetfields").value,
  			parobj:document.getElementById("consistfields").value});
  	parent.doSearchFinished();
  }
  
  </script>
    <form action="" method="post" onsubmit="return checkparamter()">
      <input type="hidden" name="searchfields" value="<%=request.getParameter("searchfields") %>">
      <input type="hidden" name="targetfields" value="<%=request.getParameter("targetfields") %>">
      <input type="hidden" name="consistfields" value="<%=request.getParameter("consistfields") %>">
    
      <input type="hidden" name="baseconditions" value="<%=request.getParameter("baseconditions") %>">
      <input type="hidden" name="queryparamter" value="<bean:write name="queryparamter"/>">
      <input type="hidden" name="forward"  value="<%=request.getParameter("forward") %>" >
      
      <input type="hidden" name="searchcol"  value="<bean:write name="searchcol"/>" >
      <input type="hidden" name="searchcond"  value="<bean:write name="searchcond"/>" >
      
      <table class="Table_main" align="center" border="1" cellspacing="1" cellpadding="1" width="100%">
      	<tr><td>
      		<table class="Table_main">
     		<%
      			String searchcol = (String)request.getAttribute("searchcol");
      			String[] cols = searchcol.split(",");
      			int i=0;
      		 %>
      			<logic:iterate id="stitle" name="searchtitle">
      			<tr>
		          	<td><bean:write name="stitle" /></td>
		    <%
		    	if(cols[i].indexOf("[")<=0){
		     %>
		          	<td><input type="text" name="searchfield" value="" size="20"></td>
			<%
				}else{
					String dics = cols[i].substring(cols[i].indexOf("[")+1,cols[i].indexOf("]"));
					String[] diccol = dics.split("//");
			 %>		  
			        <td><select name="searchfield">
			        	<option></option>
			        	<jadlhtml:optionsCollection name="dic" property="<%=diccol[0] %>" value="<%=diccol[1] %>" label="<%=diccol[2] %>"/>
			        </select></td>
			<%
				}
				i++;
			 %>
		        </tr>
		        </logic:iterate>
      		</table>
      	</td>
      	<td>
      		<table class="Table_main">
      			<tr>
		          	<td><input type="submit" Class="Button_Blue" value="查询"/></td>
		        </tr>
		        <tr>
		          	<td><input class="Button_Blue" type="button" name="search_bnt" value="关闭" onclick="parent.closesearchwin();"/></td>
		        </tr>
      		</table>
      	</td></tr>
      </table>
    </form>
    <input type="hidden" name="curItem">
      <table class="Table_main" align="center" border="1" width="100%">
      	<logic:present name="colwidth">
      		<logic:iterate id="col" name="colwidth">
		    	<colgroup style="width:<bean:write name="col" />">
		    </logic:iterate>
		</logic:present>
		    <tr>
		    <logic:iterate id="title" indexId="idxid" name="titles">
		    	<td class="Td_Title"><bean:write name="title" /></td>
		    </logic:iterate>
		    </tr>
		    <%
		    	String titlelength = ((Integer)request.getAttribute("titlelength")).toString();
		     %>
		    <logic:iterate id="result" name="list">
		    <tr class="Pop_TR" onclick="selectSearchRow(this);" ondblclick="dblClick(this)" style="cursor:hand">
		    	<logic:iterate id="rescol" indexId="idxid" name="resultcol">
		    		<%
		    			String idrescol = (String)pageContext.getAttribute("rescol");
		    		 %>
		    		<logic:lessThan name="idxid" value="<%=titlelength %>">
		    			<td class="Pop_Text"><bean:write name="result" property="<%=idrescol %>"/><html:hidden name="result" property="<%=idrescol %>"/></td>
		    		</logic:lessThan>
		    		<logic:greaterEqual name="idxid" value="<%=titlelength %>">
		    			<html:hidden name="result" property="<%=idrescol %>"/>
		    		</logic:greaterEqual>
		    	</logic:iterate>
		    </tr>
		    </logic:iterate>
		    <tr>
				<td colspan="<%=titlelength %>" align="right">
				    <page:pager total="${total}" defaultPageSize="${pagesize}">
						<page:navigator type="TEXT" />
					</page:pager>
				</td>
			</tr>
	  </table>
  </body>
</html:html>
