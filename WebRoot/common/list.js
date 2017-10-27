    //列表保存
	function savelist()
	{
		var pageform = document.getElementById("pager");
		var url = window.location.href;
		if(url.indexOf("saveaction=savelist")<0) {
			if(url.indexOf("?")>0) {
				url = url + "&";
			} else {
				url = url + "?";
			}
			pageform.action = url + "saveaction=savelist";
		}
		pageform.submit();
		pageform.action = "";
	}
	
	//排序
	function setSort(fieldname){
		var pageform = document.getElementById("pager");
		
		var queryparamter = document.getElementById("queryparamter");
		if(queryparamter) {
			var param = queryparamter.value;
			if(param.indexOf("db_resultorderby~=~")>=0){
				if(param.indexOf("db_resultorderby~=~" + fieldname)>=0){
					if(param.substr(param.length-5)==" DESC") {
						queryparamter.value = param.substr(0, param.length-5);
					} else {
						queryparamter.value = param + " DESC";
					}
				} else {
					queryparamter.value = param.substr(0,param.indexOf("db_resultorderby~=~")) 
						+ "db_resultorderby~=~" + fieldname;
				}
			} else {
				if(param!=null && param!="" && param.substring(0,2)=="&&") param = param + "~";
				else param = "&&";
				queryparamter.value = param + "db_resultorderby~=~" + fieldname;
				alert(param + "db_resultorderby~=~" + fieldname);
			}
		} else {
			var url = window.location.href;
			if(url.indexOf("?")>0) {
				url = url + "&";
			} else {
				url = url + "?";
			}
			pageform.action = url + "queryparamter="+encodeURIComponent("&&db_resultorderby~=~" + fieldname);
		}
		var choice = document.getElementById("choice");
		if(choice) choice.value = "first";
		
		pageform.submit();
	}
	
	//点击排序
	function clickSort(indexlist,fieldlist) {
		var objTD = window.event.srcElement; 
		
		if(objTD.tagName != "TD" && objTD.tagName != "TH"){
			objTD = objTD.parentElement;
			if(objTD.tagName != "TD" && objTD.tagName != "TH") return;
		}
		
		for(var i=0;i<indexlist.length && i<fieldlist.length;i++) {
			if(indexlist[i]==objTD.cellIndex) {
				setSort(fieldlist[i]);
				break;
			}
		}
	}
	
	//设置排序标志
	function setSortflag(indexlist,fieldlist,tableid){
		var queryparamter = document.getElementById("queryparamter");
		if(queryparamter) {
			var param = queryparamter.value;
			if(param.indexOf("db_resultorderby~=~")>=0){
				var sortfield = param.substr(param.indexOf("db_resultorderby~=~")+19);
				var sortstr = "↑";
				if(sortfield.substr(sortfield.length-5)==" DESC") {
					sortfield = sortfield.substr(0, sortfield.length-5);
					sortstr = "↓";
				} 
				
				for(var i=0;i<fieldlist.length && i<indexlist.length;i++){
					if(fieldlist[i]==sortfield) {
						setCellFlag(tableid, indexlist[i],sortstr);
						break;
					}
				}
			}
		}
	}
	
	//设置对应表格的标志
	function setCellFlag(tableid, cellindex, flagtext) {
		var objTable = document.getElementById(tableid);
		if(!objTable) {
			try {
				objTable = document.getElementsByTagName("TABLE")[tableid];
			} catch(e) {
				return;
			}
		}
		if(objTable) {
			var objTr = objTable.rows[0];
			var objCell = objTr.cells[cellindex];
			if(objCell) {
				objCell.innerHTML = objCell.innerHTML + "<font color=orange>"  + flagtext + "</font>";
			}
		}
	}
