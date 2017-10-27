<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>请登录</title>
    <link type="text/css" href="../common/total.css" rel="stylesheet">

</head>
<body bgcolor="#F5F5F5" style="margin:0;">
<table border="0" cellspacing="0" cellpadding="0" height="60%" width="100%">
  <tr align="center" >
    <td class="td_prompt">您没有登录或者离开时间太长，请先<a href="${pageContext.request.contextPath }/commons/promptlogin.html" target=_top>登录！</a></td>
  </tr>
</table>
<script type="text/javascript">
if("undefined"!=typeof(parent.unloading)){
parent.unloading();
}
</script>
</body>
</html>
