var win_search;
/**
 * ��ȡҳ��Ԫ�ض���
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
 * ��ȡRadioԪ�ض���
 */
function getRadioElement(e) {
	var radios = document.getElementsByName(e);
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].checked) {
			return radios[i];
		}
	}
}

//��Ԫ�ظ�ֵ
function putValue(target, val) {
	var obj = getElement(target);
	if (obj) {
		obj.value = val;
	}
}

//����Ԫ�ص��Ƿ�ֻ�
function setDisabled(tar, val) {
	var obj = getElement(tar);
	if (obj) {
		obj.disabled = val;
	}
}

//ȫѡ��ȫ��ѡһ��checkbox
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
//��ѡ����µ�����
function newSearchLine(fr){
}

//ѡ����
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

//ȡ��������������
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
//ȡ����������
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
 * ���ܣ��򿪲�ѯҳ��
 * forward:��ת·��
 * condition:����
 * searchfields:�����ֶ�
 * targetfields:ҳ���Ӧ��
 * consistfields:�ϲ���Ӧ��
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

//��ѯҳ���е�˫���¼���show Div ģʽ��
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

//��ѯҳ�渳ֵ���
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
		win_search = showwin("��ѯ", url, 600, 400);
	} else {
		navwin(win_search, url);
	}
}
