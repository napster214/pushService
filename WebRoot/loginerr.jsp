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
<title>�����̨����- �������¾�������Ƽ��ɷݹ�˾</title>
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
				<a href="${pageContext.request.contextPath }" class="appbrand"><span>��������̨</span>
				</a>
				<ul class="pull-right">
					<li><span>��ӭ����</span>
					</li>
				</ul>
			</div>

		</div>

		<div class="container" style="padding-top:100px;">

			<div class="col-center">
				 	<div style="text-align:center;">
				 	<span>${empty errMsg  ? param.errMsg : errMsg}</span><br/>
				��ҳ����<span id="sec"></span>���Ӻ���ת����δ��ת�ɹ�������<a href="<%=SSOConstants.urlMap.get(SSOConstants.SSO_URLMAP_LOGIN).toString()%>">��¼</a>
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
