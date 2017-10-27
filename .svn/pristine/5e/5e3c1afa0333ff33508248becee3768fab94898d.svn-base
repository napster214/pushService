<%@ page language="java" contentType="text/html; charset=GBK"%>
	<table width="100%" align="center" class="makepage_table">
    	<tr>
    		<td align="center" class="makepage_td" nowrap>
    			<div id="x_wait_div"  style="display:block;">
    				<img src="../images/loading.gif" width="20" align="absmiddle">&nbsp;&nbsp;&nbsp;正在查询，请稍候……   				
    			</div>
    			<div id="x_count_div" style="display:none;">
	      			<page:pager total="${total}" defaultPageSize="${pagesize}">
	      				<page:navigator type="TEXT" />
	      			</page:pager>
      			</div>
    		</td>
    	</tr>		
	</table>
	<script type="text/javascript">
		var x_total = document.getElementsByName("pageTotal")[0].value;
		if (x_total == "-1") {
			var queryparamter = pager.queryparamter.value;
			var queryparamtername = pager.queryparamtername.value;
			var conditions = pager.conditions.value;
			var type = pager.type.value;
			var method = pager.method.value;
			var pageNo = pager.pageNo.value;
			var pageSize = pager.pageSize.value;
			var choice = pager.choice.value;
			var pageTotal = pager.pageTotal.value;
			var url = window.location.href;
			if (url.substring(url.length - 1) == "#") {
				url = url.substring(0 , url.length - 1);
			}
			$.ajax({
				type: "POST",
				url: url,
				/*张方俊 2008年10月24日。WebSphere下AJAX乱码处理：由于统一在jquery的AJAX中通过encodeURI处理，而这里有&&符号，无法使用，所以这里传递jquery_encode=来判断是否使用encodeURI来编码*/
				data: "jquery_encode=&type=type&queryparamter=" + encodeURIComponent(queryparamter) + "&x_count=yes" + "&queryparamtername=" + encodeURIComponent(queryparamtername),
				dataType: "html",
				success: function(msg) {
					x_count_div.innerHTML = msg;
					x_wait_div.style.display = "none";
					x_count_div.style.display = "block";
				},
				error: function(a,b,c) {
					console.log(a);
					console.log(a.responseText);
					alert("服务器故障，请与管理员联系！");
				}
				/* error: function(e) {
					alert("服务器故障，请与管理员联系！");
				} */
			});
		} else {
			x_wait_div.style.display = "none";
			x_count_div.style.display = "block";
		}
	</script>