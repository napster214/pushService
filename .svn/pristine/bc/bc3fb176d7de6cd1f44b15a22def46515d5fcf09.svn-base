var win_search;
/**
 * 获取页面元素对象
 */
function getElement(e) {
	var elem;
	switch (typeof e) {
	  case ("object"):
		elem = e;
		break;
	  case ("string"):
		elem = document.getElementById(e);
		if (!elem) {
			elem = document.getElementsByName(e)[0];
		}
		if (elem) {
			if (elem.type == "radio") {
				return getRadioElement(e);
			}
		}
		return elem;
	  default:
	}
	return elem;
}

/**
 * hide a element
 */
function hideElement(e) {
	var elem = getElement(e);
	if (elem) {
		elem.style.display = "none";
	}
}
/**
 * show a element
 */
function showElement(e) {
	var elem = getElement(e);
	if (elem) {
		elem.style.display = "block";
	}
}

function inlineElement(e) {
	var elem = getElement(e);
	if (elem) {
		elem.style.display = "inline";
	}
}

/**
 * 获取Radio元素对象
 */
function getRadioElement(e) {
	var radios = document.getElementsByName(e);
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].checked) {
			return radios[i];
		}
	}
}

//给元素赋值
function putValue(target, val) {
	var obj = getElement(target);
	if (obj) {
		obj.value = val;
	}
}

//设置元素的是否恢化
function setDisabled(tar, val) {
	var obj = getElement(tar);
	if (obj) {
		obj.disabled = val;
	}
}

//全选或全不选一组checkbox
function allCheck(obj, target) {
	var items = document.getElementsByName(target);
	if (items) {
		if (obj.checked) {
			for (var i = 0; i < items.length; i++) {
				items[i].checked = true;
			}
		} else {
			for (var i = 0; i < items.length; i++) {
				items[i].checked = false;
			}
		}
	}
}
//多选情况下的新行
function newSearchLine(fr){
}

//选中行
function selectSearchRow(obj) {
	if (obj == null) {
		return;
	}
	if (selectrow > 0) {
		objTable = obj.parentElement;
		if (objTable.tagName != "TABLE") {
			objTable = objTable.parentElement;
		}
		objTable.rows[selectrow].className = "Pop_TR";
	}
	selectrow = obj.rowIndex;
	obj.className = "Pop_TR1";
}

//取行政区划条件串
function getXzqhConditions(col, xzqh, type) {
	if (xzqh == "") {
		return "";
	}
	if (type == "1") {
		var m = xzqh;
		if (m == "000000") {
			m = "";
		} else {
			if (m.substring(2, 6) == "0000") {
				m = m.substring(0, 2);
			} else {
				if (m.substring(4, 6) == "00") {
					m = m.substring(0, 4);
				}
			}
		}
		return col + "@likeLeft@" + m;
	} else {
		if (type == "2") {
			return col + "@equals@" + xzqh;
		} else {
			if (type == "3") {
				return col + "@likeLeft@" + xzqh;
			}
		}
	}
}
//取级别条件串
function getLevelConditions(col, xzqh, type) {
	if (xzqh == "") {
		return "";
	}
	if (type == "1") {
		var m = xzqh;
		if (m == "000000") {
			m = "";
		} else {
			if (m.substring(2, 6) == "0000") {
				m = m.substring(0, 2);
			} else {
				if (m.substring(4, 6) == "00") {
					m = m.substring(0, 4);
				}
			}
		}
		return col + "@levellikeLeft@" + m;
	} else {
		if (type == "2") {
			return col + "@levelequals@" + xzqh;
		} else {
			if (type == "3") {
				return col + "@levellikeLeft@" + xzqh;
			}
		}
	}
}
/**
 * 功能：打开查询页面
 * forward:跳转路径
 * condition:条件
 * searchfields:返回字段
 * targetfields:页面对应域
 * consistfields:合并对应域
 */
function buildSearchUrl(forward, conditions, searchfields, targetfields, consistfields, multiselect) {
	var url = "../search/search.do?forward=" + forward 
			+ "&baseconditions=" + conditions 
			+ "&searchfields=" + searchfields 
			+ "&targetfields=" + targetfields 
			+ "&consistfields=" + consistfields
			+ "&multiselect=" + multiselect;
	return url;
}

//查询页面中的双击事件【show Div 模式】
function onSearchdblClick(obj, keys) {
	var fieldArray = keys["fields"];
	var targetArray = keys["targets"];
	var targetobj = keys["parobj"];
	var objTR = obj;
	var isel = objTR.rowIndex;
	var str = "";
	if (fieldArray) {
		var fields = fieldArray.split(",");
		var targets;
		if (targetArray) {
			targets = targetArray.split(",");
		}
		for (var i = 0; i < fields.length; i++) {
			if (targets && targets[i]) {
				parent.putValue(targets[i], document.getElementsByName(fields[i])[isel - 1].value);
			}
			var pre = "--";
			if (i == 0) {
				pre = "";
			}
			str += pre + document.getElementsByName(fields[i])[isel - 1].value;
		}
		if (targetobj) {
			parent.putValue(targetobj, str);
		}
	}
}
function closesearchwin() {
	if (win_search != null) {
		win_search.close();
	}
}

//查询页面赋值完毕
function doSearchFinished() {
	closesearchwin();
}
function setSearchParamter(keys) {
	if (keys) {
		var conditions = getElement("queryparamter");
		var colArray = keys["searchcol"];
		var operArray = keys["searchoper"];
		var fieldArray = keys["searchfield"];
		if (colArray && operArray && fieldArray) {
			var cols = colArray.split(",");
			var opers = operArray.split(",");
			var fields = document.getElementsByName(fieldArray);
			if (cols.length == opers.length && opers.length == fields.length) {
				var str = "";
				for (var i = 0; i < cols.length; i++) {
					var tmp = fields[i].value;
					
					if (tmp != null && tmp != "") {
						str = "~" + cols[i] + "@" + opers[i] + "@" + tmp;
					}
				}
				if (str == "") {
					str = "~";
				}
				conditions.value = str.substr(1);
			}
		}
	}
}
function openSeachWindow(forward, conditions, searchfields, targetfields, consistfields, multiselect) {
	if(!multiselect) multiselect=0;
	var url = buildSearchUrl(forward, conditions, searchfields, targetfields, consistfields, multiselect);
	if (win_search == null) {
		win_search = showwin("查询", url, 600, 400);
	} else {
		navwin(win_search, url);
	}
}
