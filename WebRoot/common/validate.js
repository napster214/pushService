/** ===============================================================
 *
 * 此JS包含FORM表单的验证。HTML中只需引入此JS即可。
 * 此JS包含STRING的两个方法：
 * 1、计算实际长度的方法（汉字算两个字符）
 * 2、删除字符串两头空格的方法
 *
 * =================================================================*/
function checkCharFormat(cValue){
	var asciiFormat = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
	return asciiFormat.indexOf(cValue);	
}

function isLicenseNo(str) {
    return /(^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$)/.test(str);
}

String.prototype.endWith = function (s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length) {
		return false;
	}
	if (this.substring(this.length - s.length) == s) {
		return true;
	} else {
		return false;
	}
	return true;
};
String.prototype.startWith = function (s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length) {
		return false;
	}
	if (this.substr(0, s.length) == s) {
		return true;
	} else {
		return false;
	}
	return true;
};
String.prototype.length2 = function() {
	/*
	 * 原方法
	 * var cArr = this.match(/[^\x00-\xff]/ig);
     * return this.length + (cArr == null ? 0 : cArr.length);
	 * */
	var iLength = 0;
	for(var i = 0; i < this.length; i ++ ){
		//var k = checkCharFormat(this.charAt(i));
		if(checkCharFormat(this.charAt(i)) != -1){
			iLength += 1;
		}else{
			iLength += 2;
		}
	}
	return iLength;
}
String.prototype.trim = function() {
	var  i,b=0,e=this.length;  
    for(i=0;i<this.length;i++)
    	if(this.charAt(i)!=' ')
        	{
        		b=i;
        		break;
        	}  
     if(i==this.length)  
        return  "";  

   	for(i=this.length-1;i>=b;i--) 
   		if(this.charAt(i)!=' ')
      		{
      			e=i;
      			break;
      		}  
  return  this.substring(b,e+1);
}
var x_flag = "\n";
function setFlag(f){
	x_flag = f;
}
function alertErrorMsg(errorMsg){
	alert(errorMsg);
}
// 页面覆盖此方法继续校验
function customCheck(f){
	return "";
}
/*如果有页签，需要转换页签*/
function setTagVisible(obj){
	if(obj == null) return;
	var tab = obj.parentElement;
	while((tab.tagName != "DIV" || tab.id.toUpperCase().substr(0,3) != "TAG") && tab.tagName != "BODY"){		
		tab = tab.parentElement;
	}		
	if(tab.tagName == "BODY") return;	
	var maxtab=0;
	for(var i=1;i<15;i++){
		var objtd = document.getElementById("td" + i);
		if(objtd!=null) 
			maxtab=i;
		else
			break;
	}
	if(maxtab>1){
		ShowTag(tab.id.substr(3),maxtab);
	}
}
// 检查form
function checkForm(f) {
	var errorMsg = "";
	var hasFocus = false;
	for (var elementIndex = 0;elementIndex < f.elements.length;elementIndex++) {
		var ele;
		try	{
			ele = f.elements[elementIndex];
			var eleTagn = ele.tagName;
			var eleType = ele.type;
			if((eleTagn == "INPUT" && (eleType == "text" || eleType == "checkbox" || eleType == "radio" )) || ele.tagName == "TEXTAREA"){
				/*检查CHECKBOX。CHECKBOX仅检查是否必须选择以及选择的项数*/
				if(eleType == "checkbox"){
					errorMsg += checkCheckBox(ele);
				}else if(eleType == "radio"){
					errorMsg += checkRadio(ele);
				}else{
					errorMsg += checkElement(ele);	
				}
				/*定位*/
				if(!hasFocus && errorMsg != ""){
					try{
						hasFocus = true;
						/*如果有页签，需要转换页签*/
						setTagVisible(ele);
						ele.focus();
					}catch(e){}
				}
			}else if(ele.tagName == "SELECT"){
				errorMsg += checkSelect(ele);
				/*定位*/
				if(!hasFocus && errorMsg != ""){
					hasFocus = true;
					ele.focus();
				}
			}		
		}
		catch(e){}
	}
	errorMsg += customCheck(f);  
	//lihonglei 修改 2009-08-27 加入了对errorMsg的处理方法operateErrorMsg  应用时在页面中覆盖此方法，写入自己的处理
	errorMsg = operateErrorMsg(errorMsg);
	if(errorMsg != ""){
		alertErrorMsg(errorMsg);
		return false;
	}
	return true;
}
function operateErrorMsg(errorMsg) {
	return errorMsg;
}

function checkNull(checkType , checkValue , titleValue){
	var pat = /notnull/g;
	if(pat.test(checkType) && checkValue.trim() == ""){
		return titleValue + "不能为空，请输入。" + x_flag;
	}
	return "";
}
function checkNumber(checkType , checkValue , titleValue){
	var pat = new RegExp("number\\([=]{0,1}\\d+,{0,1}\\d*\\)", "g");
	if(!pat.test(checkType) || checkValue == ""){
		return "";
	}		
	pat.compile("number\\((\\d+),(\\d+)\\)", "g");
	if(pat.test(checkType)){
		var zs = RegExp.$1;	
		var xs = RegExp.$2;
		/*张方俊 2008-11-4 根据ORACLE的要求，（13,3）代表10位整数和3位小数*/
		zs = zs - xs;
		if(checkValue.indexOf(".") == -1){
			checkValue = checkValue + ".";
		}
		pat.compile("^\\d{1," + zs + "}\\.{0,1}\\d{0," + xs + "}$" , "g");
		if(pat.test(checkValue)){
			return "";
		}else{
			return titleValue + "有误。请输入数字，且整数部分长度不能超过" +  zs + "位，小数部分不能超过" + xs + "位。" + x_flag;
		}
	}		
	pat.compile("number\\((\\d+)\\)" , "g");
	if(pat.test(checkType)){				
		var zs = RegExp.$1;	
		pat.compile("^\\d{1," + zs + "}$" , "g");
		if(pat.test(checkValue)){
			return "";
		}else{
			return titleValue + "有误。请输入整数，且长度不能超过" +  zs + "位。" + x_flag;
		}
	}
	/*张方俊 2008-11-4 增加等于的判断*/
	pat.compile("number\\(=(\\d+)\\)" , "g");
	if(pat.test(checkType)){				
		var zs = RegExp.$1;	
		pat.compile("^\\d{" + zs + "}$" , "g");
		if(pat.test(checkValue)){
			return "";
		}else{
			return titleValue + "有误。请输入" +  zs + "位整数。" + x_flag;
		}
	}
}
function checkDate(checkType , checkValue , titleValue){
	var pat = /date/g;
	if(!pat.test(checkType) || checkValue == ""){
		return "";
	}	
	var errMsg = __checkDate__(checkValue);
	return errMsg == "" ? "" : (titleValue + errMsg + "，请重新输入。" + x_flag);
}
function __checkDate__(dateValue){
	var pat = /^(\d{4})-(\d{2})-(\d{2})$/g;
	if(!pat.test(dateValue))
	{
		return "格式有误！标准格式如：2006-01-01";
	}
	var year = parseInt(RegExp.$1, 10);
	var month = parseInt(RegExp.$2, 10);
	var day = parseInt(RegExp.$3, 10);
	var thisDay = 0;
	var errMsg = "";
	switch(month) {
		case 1:case 3:case 5:case 7:case 8:case 10:case 12:thisDay = 31;break;
		case 4:case 6:case 9:case 11:thisDay = 30;break;
		case 2:(((year % 4 == 0) && ((!(year % 100 == 0)) || (year % 400 == 0))) ? thisDay=29 : thisDay=28);break;
	}
	var pre = "";
	if((year > 3000 ) || (year < 1900)) {
		errMsg += pre + "格式中的年份有误";
		pre = "，";
	}
	if((month > 12 ) || (month < 1)) {
		errMsg += pre + "格式中的月有误";
		pre = "，";
	}else if((day > thisDay) || (day < 1)) {
		errMsg += pre + "格式中的日有误";
		pre = "，";
	}
	return errMsg;
}

function checkInputLength(checkType , checkValue , titleValue){
	var pat = /length(<=|=)(\d+)/g;
	if(!pat.test(checkType) || checkValue == ""){
		return "";
	}
	var opt = RegExp.$1;
	var val = parseInt(RegExp.$2, 10);
	var len = checkValue.length2();
	var l = ((val%2)==1) ?((val-1)/2) : val/2;
	var m = "。";
	if(l != 0){
		m = "或" + l  + "位汉字。";
	}	
	if(opt == "<=" && len > val){		
		return titleValue + "有误。请输入不超过" + val + "位字符" + m + x_flag;
	}else if(opt == "=" && len != val){
		return titleValue + "有误。请输入" + val + "位字符" +  m + x_flag;
	}
	return "";
}
function checkDHHM(checkType , checkValue , titleValue){
	var pat = /dhhm/g;
	if(!pat.test(checkType) || checkValue == ""){
		return "";
	}
	pat.compile("^[\\d,\\-,\\+,\\ ]+$","g");
	if(!pat.test(checkValue)){
		return titleValue + "有误。请输入正确的号码。" + x_flag;
	}	
	var patrn=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	if(patrn.test(checkValue)){
		return "";
	}
	return titleValue + "有误。请输入正确的号码。" + x_flag;
}
function checkSFZ(checkType , checkValue , titleValue){
	var pat = /sfz/g;
	if(!pat.test(checkType) || checkValue == ""){
		return "";
	}
	if(checkValue.indexOf("x") != -1){
		checkValue = checkValue.replace("x","X");
	}
	var s = checkValue;	
	pat.compile("^\\d{15}|\\d{17}[0-9,X]$" , "g");
	var sfzErrMsg = titleValue + "有误。请输入15位或者18位公民身份号码！" + x_flag;
	if(!pat.test(s)) {
		return(sfzErrMsg);
	}
	if(s.length == 18 && s.substr(17) != getSfzLastCode(s.substring(0,17))){
		var err = "校验码有误";
		return(sfzErrMsg);
	}
	if(s.length == 15){
		s = sfz15to18(s);
	}	
	var birthErrMsg = __checkDate__(getBirth(s));
	if(birthErrMsg != "") {
		var err = "出生日期有误";
		return(sfzErrMsg);
	}		
	return "";
}
//身份证15位转18位
function sfz15to18(sfz) {
	if(sfz==null || (sfz.length!=15 && sfz.length!=17 && sfz.length!=18)) return "";
	var retsfz = sfz;
	if(retsfz.length==15) retsfz = retsfz.substr(0,6)+"19"+retsfz.substr(6);
	if(retsfz.length==17) retsfz = retsfz + getSfzLastCode(retsfz);
	return retsfz;
}
function getSfzLastCode(sfz)
{
	if(sfz==null || sfz.length<17 || sfz.length>18) return "";
	var Num=0;
	for(i=0;i<17;i++)
	{
		Num += Math.pow(2,17-i) % 11 * Number(sfz.substr(i,1));
	}
	Num = Num % 11;
	var code = (12-Num) % 11;
	return code<10?String(code):"X";
}
//获取身份证出生日期
function getBirth(sfz) {
	if(sfz == null || sfz.length!=18) return "";
	return sfz.substring(6,10) + "-" + sfz.substring(10,12) + "-" + sfz.substring(12,14);
}
function checkElement(field){
	var titleValue = field.title;
	if(!titleValue){
		return "";
	}
	var checkValue = field.value;
	//var checkType = field.alt;
	var fieldName = field.name;
	var checkType = document.getElementsByName(fieldName)[0].getAttribute("alt");
	if(!checkType){
		return "";
	}
	var errorMsg = "";
	/*检查空值。通过notnull标识*/
	errorMsg += checkNull(checkType , checkValue , titleValue);
	/*检查数字，包括小数点。通过number标识，如number(3,2)表示111.11、11.1合法，而1111.不合法*/
	errorMsg += checkNumber(checkType , checkValue , titleValue);
	/*检查日期。通过date标识*/
	errorMsg += checkDate(checkType , checkValue , titleValue);
	/*检查输入项的长度，汉字的长度为2。通过length<=标识*/
	errorMsg += checkInputLength(checkType , checkValue , titleValue);
	/*检查身份证。通过sfz标识*/
	errorMsg += checkSFZ(checkType , checkValue , titleValue);
	/*检查电话号码。通过dhhm标识*/
	errorMsg += checkDHHM(checkType , checkValue , titleValue);
	return errorMsg;
}
function checkSelect(field){
	var titleValue = field.title;	
	if(!titleValue || titleValue == ""){
		return "";
	}
	var fieldName = field.name;
	var checkType = document.getElementsByName(fieldName)[0].getAttribute("alt");
	//var checkType = field.alt;
	if(!checkType || checkType == ""){
		return "";
	}	
	var errorMsg = "";
	var val = field.value;
	var pat = /notnull/g;
	if(pat.test(checkType) && val == ""){
		return titleValue + "不能为空，请选择。" + x_flag;
	}
	return "";
}
function checkRadio(field){
	var fieldName = field.name;
	var fields = document.getElementsByName(fieldName);
	if(field != fields[0]){
		return "";
	}
	var titleValue = field.title;	
	if(!titleValue) titleValue = "";
	//var checkType = field.alt;
	var checkType = document.getElementsByName(fieldName)[0].getAttribute("alt");
	if(!checkType) checkType = "";	
	var errorMsg = "";
	var count = 0;
	for(var i=0;i<fields.length;i++){
		var altTmp = fields[i].alt;
		if(altTmp && altTmp.length > checkType){
			titleValue = fields[i].title;
			checkType = altTmp;
		}
		if(fields[i].checked){
			count++;
		}
	}	
	if(!titleValue){
		return "";
	}
	if(!checkType){
		return "";
	}
	var pat = /notnull/g;
	if(pat.test(checkType) && count == 0){
		return titleValue + "不能为空，请选择。" + x_flag;
	}
	return "";
}
function checkCheckBox(field){
	var fieldName = field.name;
	var fields = document.getElementsByName(fieldName);
	if(field != fields[0]){
		return "";
	}
	var titleValue = field.title;	
	if(!titleValue) titleValue = "";
	//var checkType = field.alt;
	var checkType = document.getElementsByName(fieldName)[0].getAttribute("alt");
	if(!checkType) checkType = "";	
	var errorMsg = "";
	var count = 0;
	for(var i=0;i<fields.length;i++){
		var altTmp = fields[i].alt;
		if(altTmp && altTmp.length > checkType){
			titleValue = fields[i].title;
			checkType = altTmp;
		}
		if(fields[i].checked){
			count++;
		}
	}	
	if(!titleValue){
		return "";
	}
	if(!checkType){
		return "";
	}
	/*检查CHECKBOX，如：notnull;length=3标识不能必须选择，且只能选择三项。length<=5标识可以选择，
	可以不选。但选择最多不能超过5项*/
	var pat = /notnull/g;
	if(pat.test(checkType) && count == 0){
		return titleValue + "不能为空，请选择。" + x_flag;
	}	
	pat = /length(<=|=)(\d+)/g;
	if(!pat.test(checkType) || count == 0){
		return "";
	}
	var opt = RegExp.$1;
	var val = parseInt(RegExp.$2, 10);
	if(opt == "<=" && val < count){
		return titleValue + "选择了" + count + "项，超过了最大允许的" + val + "项。请重新选择。" + x_flag; 
	}
	if(opt == "=" && val != count){
		return titleValue + "选择了" + count + "项，而只允许选择" + val + "项。请重新选择。" + x_flag; 
	}
	return "";
}
function fromOnSubmit() {
	//treatInput();	
	var sform = event.srcElement;	
	if(!checkForm(sform)){
		return false;
	}
	if(sform.oldfun && sform.oldfun.func) {
		return sform.oldfun.func();
	}
	return true;
}
function treatForm() {
	if(document.readyState!="complete") return;
	var curforms = document.forms;
	if(curforms.length>0) {
		for(var i=0;i<curforms.length;i++) {
			var ss = new Object();
			ss.func = curforms[i].onsubmit;
			curforms[i].oldfun = ss;
			curforms[i].onsubmit= fromOnSubmit;
		}
	}
	document.body.onpaste = docPaste;
}
function docPaste()
{
	var src = event.srcElement;
	var value = src.value;
	if(src.type=="text" || src.type=="textarea")
	{
		var str = window.clipboardData.getData("Text");
		//modified by zhaohuibin 2014-06-03 解决拷贝字符串至文本框时无法粘贴的问题
		if(str.indexOf('`') > 0 || str.indexOf('$') > 0 || str.indexOf('#') > 0 
				|| str.indexOf('@') > 0 || str.indexOf('%') > 0 || str.indexOf('~') > 0 
				|| str.indexOf('&') > 0 || str.indexOf('*') > 0 || str.indexOf('<') > 0 
				|| str.indexOf('>') > 0 || str.indexOf(';') > 0 || str.indexOf(':') > 0 
				|| str.indexOf('\'') > 0 || str.indexOf('\"') > 0 || str.indexOf('\/') > 0 
				|| str.indexOf('\\') > 0 || str.indexOf(',') > 0){
			src.innerText = value + str.replace(/[`$#@%~&*<>;:\'\"\/\\\\]/g,'').replace(/[\,]/g,"，");
			window.clipboardData.setData("Text", str.replace(/[`$#@%~&*<>;:\'\"\/\\\\]/g,'').replace(/[\,]/g,"，"));
		}
	}
}
function docKeyPress()
{
	var src = event.srcElement;
	if(src.type=="text" || src.type=="textarea"){
		if(event.keyCode>=33 && event.keyCode<=41) return false;
		if(event.keyCode>=43 && event.keyCode<=44) return false;
		if(event.keyCode>=58 && event.keyCode<=64) return false;
		if(event.keyCode>=91 && event.keyCode<=96) return false;		
		if(event.keyCode==47) return false;
		if(event.keyCode==126) return false;
	}
	return true;
}
function docKeyDown()
{
	var src = event.srcElement;
	if(src.type != "text" && src.type != "password" && src.type != "textarea" && event.keyCode == 8){
	
		return false;
	}else if(src.readOnly){
		
		return false;
	}
	return true;
}
document.onkeypress = docKeyPress;
document.onkeydown = docKeyDown;
document.onreadystatechange = treatForm;


//全部清空功能
function clearInput(formObj, condition, flag) {
	var form = null;
	if(formObj==null || trim(formObj) == '') {
		form = document.forms[0];
	} else {
		form = formObj;
	}
	condition = ',' + condition + ',';
	if(flag == null || flag == '') {
		flag = 'all';
	}
	for(var i = 0; i < form.elements.length; i++) {
		var ele = form.elements[i];
		var eleTagn = ele.tagName;
		var eleType = ele.type;
		if((eleTagn == "INPUT" && (eleType == "text" || eleType == "checkbox" || eleType == "radio")) || ele.tagName == "TEXTAREA"){
			partClearInput(flag, condition, ele, eleType);
		} else if(eleTagn == "SELECT"){
			partClearSelect(flag, condition, ele);
		}	
	}
}
function partClearSelect(flag, condition, ele) {
	var param = ',' + ele.name + ',';
	if((flag == 'checked') && (condition.indexOf(param) != -1)) {
		var selectLength = ele.options.length;
		if(selectLength > 1) {
			ele.value = ele.options[0].value;
		}
	}
	if((flag == 'unchecked') && (condition.indexOf(param) == -1)) {
		var selectLength = ele.options.length;
		if(selectLength > 1) {
			ele.value = ele.options[0].value;
		}
	}
	if(flag == 'all') {
		var selectLength = ele.options.length;
		if(selectLength > 1) {
			ele.value = ele.options[0].value;
		}
	}
}

function partClearInput(flag, condition, ele, type) {
	var param = ',' + ele.name + ',';
	if((flag == 'checked') && (condition.indexOf(param) != -1)) {
		if(type == 'text' || type == 'textarea') {
			ele.value = '';	
		} else {
			ele.checked = false;
		}
	}
	if((flag == 'unchecked') && (condition.indexOf(param) == -1)) {
		if(type == 'text' || type == 'textarea') {
			ele.value = '';	
		} else {
			ele.checked = false;
		}
	}
	if(flag == 'all') {
		if(type == 'text' || type == 'textarea') {
			ele.value = '';	
		} else {
			ele.checked = false;
		}
	}
}

/***  汉字验证 value是要判断的值，title是提示信息。返回true为汉字，false为非汉字 **/
function chineseName(value, title) {
	var reg = /^[\u4e00-\u9fa5]+$/i;
	if (!reg.test(value)) {
		alert(title + "只能输入汉字！");
		return false; 
	}
	return true;
}
//去左右空格
String.prototype.trim2 = function()
	{
		 return this.replace(/(^\s*)|(\s*$)/g, "");
	}

/*验证是否为手机号码。当前只有吉林验证是否为手机号码*/
function checkMobile(obj,xzqh){
 	if(xzqh.substring(0,2) != "22") return true;
    var sMobile = obj.value;
    if( sMobile == ""){
    	alert( obj.title + "不能为空，请填写！"); 
        return false;
    }
    if(sMobile.length != 11 || isNaN(sMobile)){
    	alert( obj.title + "必须为11位数字，请重新填写！"); 
        return false;
    }
    if(!(/^1[3|5|8][0-9]\d{4,8}$/.test(sMobile))){ 
        alert( obj.title + "不是正确的手机号码！"); 
        return false;
    } 
    return  true;
}

function checkMobile_all(obj){
 	
    var sMobile = obj.value;
    if( sMobile == ""){
    	alert( obj.title + "不能为空，请填写！"); 
        return false;
    }
    if(sMobile.length != 11 || isNaN(sMobile)){
    	alert( obj.title + "必须为11位数字，请重新填写！"); 
        return false;
    }
    if(!(/^1[3|5|8|7|4][0-9]\d{4,8}$/.test(sMobile))){ 
        alert( obj.title + "不是正确的手机号码！"); 
        return false;
    } 
    return  true;
}
/*
 * 验证维吾尔族名字中带·的姓名是否符合规则
 */ 
 function checkWwezXm(value){
 	var reg = /^[\u4e00-\u9fa5]+\·{0,1}[\u4e00-\u9fa5]+$/i;
	if (!reg.test(value)) {
		alert("姓名输入格式不正确！");
		return false; 
	}
	return true;
 }
 
//判断是否是数字
function isSz(str){
	var re = /^(([1-9][0-9]*\.[0-9][0-9]*)|([0]\.[0-9][0-9]*)|([1-9][0-9]*)|([0]{1}))$/;   //判断字符串如果是整数不能以0开头后面加正整数，如果是浮点数整数部分不能为两个0：如00.00，如果是整数，
    if (!re.test(str)){
        return false;
    }
    return true;
}
//加法   
Number.prototype.add = function(arg){   
    var r1,r2,m;   
    try{r1=this.toString().split(".")[1].length}catch(e){r1=0}   
    try{r2=arg.toString().split(".")[1].length}catch(e){r2=0}   
    m=Math.pow(10,Math.max(r1,r2))   
    return parseFloat(((this*m+arg*m)/m).toFixed(5));   
}   
 
//减法   
Number.prototype.sub = function (arg){   
    return parseFloat((this.add(-arg)).toFixed(5));   
}   
 
//乘法   
Number.prototype.mul = function (arg)   
{   
    var m=0,s1=this.toString(),s2=arg.toString();   
    try{m+=s1.split(".")[1].length}catch(e){}   
    try{m+=s2.split(".")[1].length}catch(e){}   
    return parseFloat((Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)).toFixed(5));
}   
 
//除法   
Number.prototype.div = function (arg){   
    var t1=0,t2=0,r1,r2;   
    try{t1=this.toString().split(".")[1].length}catch(e){}   
    try{t2=arg.toString().split(".")[1].length}catch(e){}   
    with(Math){   
        r1=Number(this.toString().replace(".",""))   
        r2=Number(arg.toString().replace(".",""))   
        return parseFloat(((r1/r2)*pow(10,t2-t1)).toFixed(5));   
    }   
}  