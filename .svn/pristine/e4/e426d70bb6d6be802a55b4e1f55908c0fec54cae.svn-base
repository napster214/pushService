/**
 *
 * 功能说明:
 *   1)对数据字典下拉框的操作公用函数
 *	 2)对数据存在性检查的操作公用函数
 */
//标准发送AJAX函数
function sendToDispatch(paramters, handle, responseType)
{
	var d=new Date();
	$.ajax({url: "../ajax/dispatch?sjc="+d.toLocaleTimeString()+"&"+paramters,
			dataType: responseType.toLowerCase(),
          success:function(msg){ 
                         handle(msg);
                } 
    });
}
//同步AJAX函数
function waitDispatch(paramters,handle, responseType)
{
	var d=new Date();
	var ajax = $.ajax({url: "../ajax/dispatch?sjc="+d.toLocaleTimeString()+"&"+paramters,
			async: false,
          success:function(msg){ 
                         handle(msg);
                } 
    });
    
    if(responseType.toLowerCase()=="xml"){
    	return handle(ajax.responseXML);
    }else{
    	return handle(ajax.responseText);
    }
}
///////////////////////////////////////////////////////////////////////////
//
//  数据字典相关函数
//
///////////////////////////////////////////////////////////////////////////
 
 	//基本的获取数据字典函数
 	function getDicData(dicname,dmcol,valuecol,cond,handle)
	{
		sendToDispatch("action=getdic&dicname=" +dicname+ "&dm=" + dmcol +"&value=" + valuecol
			+ "&cond=" + cond, handle, "XML");
	}
	//清除下拉框obj
	function clearoption(obj)
  	{
  		if(obj==null) return;
  		while(obj.length>0)
  			obj.remove(0);
  	}
  	//使用items填充下拉框obj（先清除），items为从缓存中获取的XML
  	function filloptions(items, obj)
  	{
  		if (obj==null) return;
  		var objOption;
  		clearoption(obj);
  		objOption = document.createElement("option");
		objOption.value = "";
		objOption.text = "";
		obj.options.add(objOption);
		
		if (items==null) return;
  		for(var i=0;i<items.length;i++)
  		{
  			var item = items[i];
  			objOption = document.createElement("option");
		    objOption.text = item.getAttribute("name");
		    objOption.value = item.getAttribute("dm");
            obj.options.add(objOption);
  		}
  	}
  	//使用从AJAX返回的XML字符串doc来填充下拉字典obj
  	function setOptions(doc,obj)
  	{
  		var result = doc.getElementsByTagName("result")[0];
  		if(result.getElementsByTagName("status")[0].firstChild.nodeValue!="0")
		{
			//出错了
			var content=result.getElementsByTagName("content")[0].firstChild.nodeValue;
			//把选项置空
			filloptions(null,obj);
			//显示错误信息
			alert(content);
		}else
		{
			//刷新选项
			var content=result.getElementsByTagName("content")[0];
			filloptions(content.getElementsByTagName("row"),obj);
		}
  	}
  	//获取字典完整函数
  	function getDicDataKey(obj, keys)
  	{
	    
	    if (obj && keys) {
	    	var targetobj = document.getElementById(keys["target"]);
	    	if(targetobj==null)
	    	{
	    		alert("没有设置目标域！");
	    		return;
	    	}
	    	
	    	if(obj.value!="")
	  		{
	  			var condcol = keys["condcols"];
	  			var valuefield = keys["valuefields"];
	  			//组织条件字符串
	  			var cols = condcol.split(",");
	  			var fields = valuefield.split(",");
	  			var scond = "";
	  			for(var i=0;i<cols.length && i<fields.length;i++)
	  			{
	  				if(cols[i]!="" && fields[i]!="")
	  				{
	  					var field = document.getElementById(fields[i]);
	  					if(scond!="") scond = scond + "~";
	  					scond = scond + cols[i] + "#equal#" + field.value;
	  				}
	  			}
	  			
	  			getDicData(keys["dicname"],keys["dmcol"],
	  				keys["valuecol"],scond,
	  				function(doc){setOptions(doc, targetobj);});
	  		} 
	  		else
	  		{
	  			clearoption(targetobj);
	  		}    
	  	}
  	}
  	
///////////////////////////////////////////////////////////////////////////
//
//  数据存在性检查相关函数
//
/////////////////////////////////////////////////////////////////////////// 	

	//基本的数据检查函数
 	function checkDataExist(dataname,cond,handle)
	{
		sendToDispatch("action=checkdataexist&dataname=" +dataname+ "&cond=" + cond, handle, "TEXT");
	} 
	function checkDataExistWait(dataname,cond,handle)
	{
		return waitDispatch("action=checkdataexist&dataname=" +dataname+ "&cond=" + cond, handle, "TEXT");
	}  
	
	function isExist(doc, title, shownormal)
	{
		var rescode = doc.substring(0,1);
  		if(rescode!="0")
		{
			if(rescode == "3") //存在
			{
				if(title!=null && title!="") alert(title + "在系统中已经存在！");
				return false;
			}
			else
			{
				if(rescode>0 && rescode<10){
					//出错了
					var content=doc.substr(1);
					//显示错误信息
					alert(content);
					return false;
				}else{
					alert("系统错误！");
				}
			}
		}else  //不存在
		{
			if(shownormal && title!=null && title!="") alert(title + "在系统中不存在！");
			return true;
		}
	}	
	
	//获取字典完整函数
  	function checkDataExistTitle(title, dataname, cond)
  	{
  		checkDataExist(dataname,cond,
	  				function(doc){isExist(doc, title, false);});
  	}
  	function checkDataExistTitleWait(title, dataname, cond)
  	{
  		return checkDataExistWait(dataname,cond,
	  				function(doc){return isExist(doc, title, false);});
  	}
  	
///////////////////////////////////////////////////////////////////////////
//
//  获取数据相关函数
//
/////////////////////////////////////////////////////////////////////////// 	

	//基本的获取数据函数
 	function getDBDataContent(dataname,cols, cond,handle)
	{
		sendToDispatch("action=getdata&dataname=" +dataname + "&cols=" + cols + "&cond=" + cond, handle, "XML");
	} 
	function getDBBlobDataContent(dataname,cols, cond,handle)
	{
		sendToDispatch("action=getdata&dataname=" +dataname + "&cols=" + cols + "&cond=" + cond + "&blobflag=1", handle, "XML");
	}
	
	function treatData(doc, func)
	{
		var result = doc.getElementsByTagName("result")[0];
  		if(result.getElementsByTagName("status")[0].firstChild.nodeValue!="0")
		{
			//出错了
			var content=result.getElementsByTagName("content")[0].firstChild.nodeValue;
			//显示错误信息
			alert(content);
		}else
		{
			//刷新选项
			var content=result.getElementsByTagName("content")[0];
			func(content.getElementsByTagName("row"));
			content = null;
		}
	}	
	
	//获取数据完整函数
  	function getDBData(dataname, cols, cond, func)
  	{
  		getDBDataContent(dataname,cols, cond,
	  				function(doc){treatData(doc, func);});
  	}
  	
  	//获取数据完整函数
  	function getDBBlobData(dataname, cols, cond, func)
  	{
  		getDBBlobDataContent(dataname,cols, cond,
	  				function(doc){treatData(doc, func);});
  	}
