<%@page import="com.jadlsoft.sso.constants.SSOConstants"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>服务后台管理- 北京创新京安丹灵科技股份公司</title>
<!-- Bootstrap -->
<link
	href="${pageContext.request.contextPath }/common/css/bootstrap/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath }/common/css/bootstrap/bootstrap-responsive.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath }/common/css/loginui.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath }/common/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/common/js/bootstrap/bootstrap.min.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath }/common/validate.js"></script>
</head>
<body>
	<div class="login">
		<div class="navbarline navbar-static-top" role="navigation">
			<div class="navbar main">
				<a href="${pageContext.request.contextPath }" class="appbrand"><span>服务管理后台</span>
				</a>
				<ul class="pull-right">
					<li><span>欢迎来宾</span>
					</li>
				</ul>
			</div>

		</div>

		<div class="container" style="padding-top:100px;">

			<div class="col-center">
				 	<div style="text-align:center;">
				 	<span>${empty errMsg  ? param.errMsg : errMsg}</span><br/>
				本页将在<span id="sec"></span>秒钟后跳转，如未跳转成功，请点击<a href="<%=SSOConstants.urlMap.get(SSOConstants.SSO_URLMAP_LOGIN).toString()%>">登录</a>
				 	</<div>
			</div>

		</div>
		<!-- /container -->
	</div>
	<script type="text/javascript">
		  function jumpUrl(secs,jurl){
		 	var jumpTo = document.getElementById('sec');
 			jumpTo.innerHTML=secs;  
		 	if(--secs>0){     
     			setTimeout("jumpUrl("+secs+",'"+jurl+"')",1000);     
     		}else{       
     			window.location.href=jurl;     
     		}     
		 } 
		 jumpUrl(30,"<%=SSOConstants.urlMap.get(SSOConstants.SSO_URLMAP_LOGIN).toString()%>");
	</script>
</body>
</html>
