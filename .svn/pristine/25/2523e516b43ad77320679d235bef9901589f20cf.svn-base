<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" uri="/WEB-INF/taglib/page.tld" %>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type">
	<title>微信后台管理系统 - 北京创新京安丹灵科技股份公司</title>
	<!-- Bootstrap -->
	
	<jsp:include page="/include/textjs.jsp" flush="true"></jsp:include>
	
	 <script src="${pageContext.request.contextPath }/common/list.js"></script>
	 
		<script type="text/javascript" src="${pageContext.request.contextPath }/common/xwin.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/common/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/common/searchUtils.js"></script>
		<script type="text/javascript">
			function removeGzh(id) {
				if (confirm("确认删除？")) {
					window.location.href = "${pageContext.request.contextPath }/wechat/wechatsys.do?method=deleteGzh&id="+id;
				}
			}
		</script>
	</head>
<body style="margin:0;">
  <div class="topCenterNav">
	<ol class="breadcrumb breadcrumbAdd">
		<li><img class="img-icon" src="${pageContext.request.contextPath }/common/image/home.jpg"/>首页</li>
		<li class="active">接入公众号信息查询</li>
	</ol>
  </div>
 <div class="content-main">
<div class="panel panel-info">
<div class="panel-heading">接入公众号信息查询</div>
  <div class="panel-body" style="padding-left:15px;">
   <table class="table table-hover table-condensed" >
		<thead>
			<th >序号</td>
			<th >公众号名称</th>
			<th >APPID</th>
			<th >APPNO</th>
			<th >自动回复地址</th>
   			 <th >操作</th>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="b" varStatus="state">
			<tr>
			<td>${state.index+1 }</td>
			<td>${b.gzhmc}</td>
			<td>${b.appid}</td>
			<td>${b.appno}</td>
			<td>${b.hddz}</td>
			 <td>
			<a href="${pageContext.request.contextPath }/wechat/wechatsys.do?method=editGzh&id=${b.id}" >修改</a>
			||
			<a href="javascript:void(0);" onclick="removeGzh('${b.id}')" >删除</a>
			</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
  </div>
  <div class="panel-footer"><%@ include file="../../include/page.inc"%></div>
</div>
	<script type="text/javascript" src="${pageContext.request.contextPath }/common/js/iframe.js"></script>
 </body>
</html>		
