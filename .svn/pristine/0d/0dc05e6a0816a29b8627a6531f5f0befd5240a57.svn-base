<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/taglib/page.tld" prefix="page"%>
<!DOCTYPE html>
<html>

<head>
	<title>服务后台管理系统 - 北京创新京安丹灵科技股份公司</title>
	 <jsp:include page="/include/textjs.jsp" flush="true"></jsp:include>
     <script src="${pageContext.request.contextPath }/common/list.js"></script>
</head>
<body style="margin:0;">
  <div class="topCenterNav">
	<ol class="breadcrumb breadcrumbAdd">
		<li><img class="img-icon" src="${pageContext.request.contextPath }/common/image/home.jpg"/>首页</li>
		<li class="active">公众号信息维护</li>
	</ol>
  </div>
 <div class="content-main">
<div class="panel panel-info">
     <div class="panel-heading">
		公众号信息维护
  	</div>
	<form class="form-horizontal" name="mainFrm" id="mainFrm" action="" method="post" enctype="multipart/form-data">
	<div class="panel-body" style="padding:30px;">
		<%-- <div id="retTip" style="display:none;" class="col-md-offset-3 alert alert-danger">
   			${msg}
   		</div> --%>
   		<div class="control-group">
				<div class="form-group-add">
					<label class="col-xs-3 control-label" for="appid">公众号名称</label>
					<div class="col-xs-9">
						<input type="hidden" id="id" name="id" value="${wechat.id }" />
						<input type="text" name="gzhmc" value="${wechat.gzhmc }" title="gzhmc" alt="notnull;length<=50" class="form-control" id="gzhmc" />
					</div>
				</div>
		</div>
   		<div class="control-group">
				<div class="form-group-add">
					<label class="col-xs-3 control-label" for="appid">appid</label>
					<div class="col-xs-9">
						<c:if test="${not empty wechat.id}">
							<!-- 编辑 -->
							<input type="hidden" name="appid" value="${wechat.appid }"/>
							<input type="text" disabled="disabled" name="appid" value="${wechat.appid }" title="appid" alt="notnull;length<=50" class="form-control" id="appid" />
						</c:if>
						<c:if test="${empty wechat.id}">
							<!-- 修改 -->
							<input type="text" name="appid" value="${wechat.appid }" title="appid" alt="notnull;length<=50" class="form-control" id="appid" />
						</c:if>
					</div>
				</div>
		</div>
		<div class="control-group">
				<div class="form-group-add">
					<label class="col-xs-3 control-label" for="appno">appno（公众号微信号）</label>
					<div class="col-xs-9">
						<input type="text" name="appno" value="${wechat.appno}" title="appno" alt="notnull;length<=50" class="form-control" id="appno" />
					</div>
				</div>
		</div>
		<div class="control-group">
				<div class="form-group-add">
					<label class="col-xs-3 control-label" for="appsecret">appsecret（公众号秘钥）</label>
					<div class="col-xs-9">
						<input type="text" name="appsecret" value="${wechat.appsecret }" title="appsecret" alt="notnull;length<=150" class="form-control" id="appsecret" />
					</div>
				</div>
		</div>
  </div>
  
  <div class="panel-body" style="border-bottom: none;">
  	<h5 style="text-align: center;">系统信息设置</h5>
  	<input type="hidden" id="xtszid" name="xtszid" value="${wechat.xtszid}" />
	<div class="control-group">
		<div class="form-group-add">
			<label class="col-xs-3 control-label" for="hddz">回调地址</label>
			<div class="col-xs-9">
				<input type="text" name="hddz" value="${wechat.hddz }" title="hddz" alt="length<=150" class="form-control" id="hddz" />
			</div>
		</div>
	</div>
  </div>
  <div class="panel-footer">
  <button type="button" class="btn btn-primary "  onclick="subFrm()">提交</button>
   		<button type="button" class="btn"  onclick="javascript:window.location.replace('${pageContext.request.contextPath}/wechat/wechatlist.do')" >返回</button>
		</div>
   </form>
</div>
 <script type="text/javascript">
  	function subFrm(){
  		if(!checkForm(document.mainFrm)){
  			return ;
  		}
  		var gzhmc = $("#gzhmc").val();
  		var appid = $("#appid").val();
  		var appno = $("#appno").val();
  		var appsecret = $("#appsecret").val();
  		
  		var id = $("#id").val();
  		//校验是否重复（公众号名称不能重复、appid不能重复、公众号可用）
  		
  		if (id != null && id!="") {
  			//修改
  			if (!checkUnique("", appid, appno, appsecret, "${pageContext.request.contextPath}/wechat/wechatsys.do?method=checkUseable")) {
				return ;
			}
  		}else {
			//添加
			if (!checkUnique(gzhmc, appid, appno, appsecret, "${pageContext.request.contextPath}/wechat/wechatsys.do?method=checkUnique")) {
				return ;
			}
		}
		
		//保存数据
  		var url="${pageContext.request.contextPath}/wechat/wechatsys.do?method=saveGzh";
  		if (id != null && id!="") {
			//修改
			var url="${pageContext.request.contextPath}/wechat/wechatsys.do?method=udpateGzh";
		}
		document.mainFrm.action=url;
		document.mainFrm.submit();
  	}
  	
  	//校验唯一性、可用性
  	function checkUnique(gzhmc, appid, appno, appsecret, url) {
  		var res = true;
  		$.ajax({
  			url : url,
  			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
  			type : "post",
  			async : false,
  			data : "gzhmc="+gzhmc + "&appid="+appid+"&appno="+appno+"&appsecret="+appsecret,
  			dataType: "json",
  			success : function(data) {
  				if (data.statusCode == "00000") {
					res = true;
				}else {
					res = false;
					alert(data.msg);
				}
  			}
  		});
  		return res;
  	}
  </script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/common/js/iframe.js"></script>
 </body>
</html>		
