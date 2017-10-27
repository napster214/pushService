/*
 * 查询参数相关功能
*/
function getspecialparam(field, defvalue)
{
	return defvalue;
}
function getparamter(fields, queryfield)
{
 	var result = "";
 	for(var i=0;i<fields.length;i++)
 	{
 		var field = fields[i];
 		var subsearch = false;
 		var rangesearch = false;
 		if(field.indexOf("*")>0) {
 			field = field.substr(0, field.indexOf("*"));
 			result += getspecialparam(field, getOneFieldLikeParamter(field));
 			continue;
 		}
 		if(field.indexOf("#")>0) {
 			field = field.substr(0, field.indexOf("#"));
 			result += getspecialparam(field,getOneFieldRangeParamter(field));
 			continue;
 		}
 		result += getspecialparam(field,getOneFieldParamter(field));
 	}
 	if(result!=="") {
 		result = "&&" + result.substr(1);
 	}
 	var objparamter=document.getElementsByName(queryfield);
 	if(objparamter && objparamter.length>0){
		objparamter[0].value = result;
	}
}
function getOneFieldResult(fieldname, oper, value){
	if(value!==null && value!=="" && value.indexOf("~")<0 && value.indexOf("&"))
	{
		return "~" + fieldname +"~"+oper+"~" + value;
	}
	return "";
}
function getOneFieldParamter(fieldname){
	var obj = document.getElementById(fieldname);
	if(obj!==null)
	{
		return getOneFieldResult(fieldname, "=", obj.value);
	}
	return "";
}
function getOneFieldLikeParamter(fieldname){
	var obj = document.getElementById(fieldname);
	if(obj!==null && obj.value)
	{
		return getOneFieldResult(fieldname, "like","%" + obj.value + "%");
	}
	return "";
}
function getOneFieldRangeParamter(fieldname){
	var objfrom = document.getElementById(fieldname + "from");
	var objto = document.getElementById(fieldname + "to");
	var s = "";
	if(objfrom!==null){
		s = getOneFieldResult(fieldname, ">=", objfrom.value);
	}
	if(objto!==null){
		s += getOneFieldResult(fieldname, "<=", objto.value);
	}
	return s;
}