<%@ page language="java" contentType="text/html; charset=GBK"%>
	<table width="100%" align="center" class="makepage_table">
    	<tr>
    		<td align="center" class="makepage_td" nowrap>
    			<div id="x_wait_div"  style="display:block;">
    				<img src="../images/loading.gif" width="20" align="absmiddle">&nbsp;&nbsp;&nbsp;���ڲ�ѯ�����Ժ򡭡�   				
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
				/*�ŷ��� 2008��10��24�ա�WebSphere��AJAX���봦��������ͳһ��jquery��AJAX��ͨ��encodeURI��������������&&���ţ��޷�ʹ�ã��������ﴫ��jquery_encode=���ж��Ƿ�ʹ��encodeURI������*/
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
					alert("���������ϣ��������Ա��ϵ��");
				}
				/* error: function(e) {
					alert("���������ϣ��������Ա��ϵ��");
				} */
			});
		} else {
			x_wait_div.style.display = "none";
			x_count_div.style.display = "block";
		}
	</script>