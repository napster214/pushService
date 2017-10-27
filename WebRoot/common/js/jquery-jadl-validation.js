//autor:dlm

$(document).ready(function(){
try{
	 //自定义验证电话号码
jQuery.validator.addMethod("isTel",function(value,element){
	return this.optional(element) ||/(^\d{11,13}$)|(^\d{3,4}-\d{7,9}$)/.test(value);
	},"请输入正确的电话号码<br />");
//邮政编码验证
jQuery.validator.addMethod("isPostCode", function(value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码<br />");
//登陆名验证，只能是英文字母、下划线或数字
jQuery.validator.addMethod("isChar", function(value, element) {
    var tel = /^\w+$/;
    return this.optional(element) || (tel.test(value));
}, "登陆名只能为英文字母、下划线或数字<br />");
//机构号验证，只能是字母数字横杠，并且横杠不能开头
jQuery.validator.addMethod("orgNum", function(value, element) {
	var tel = /^(?!-)(?!.*?-$)[a-zA-Z0-9]+(-)?[a-zA-Z0-9]*$/;
	return this.optional(element) || (tel.test(value));
}, "该选项只能为英文字母、横线或数字<br />");

jQuery.validator.addMethod("isChinese",function(value,element){
	var tel=/^[A-Za-z0-9\u4e00-\u9fa5]+[-]*[A-Za-z0-9\u4e00-\u9fa5]+$/;
	return this.optional(element) || (tel.test(value));
},"只能输入字母、数字、汉字，中间不能带空格<br />");
//下限的值必须小于上限的值
jQuery.validator.addMethod("little",function(value,element, param){
	value =value.replace(/(^\s*)|(\s*$)/g, "");

	if(value ==""){
		return true;
	}
	if( value !="" ){
    	var index1 = value.indexOf("0");
    	if( index1 == 0  ){
    		value = value.substring(0);
    	}
    }
    var paramvalue = $(param).val();
    if( paramvalue !="" ){
    	var index2 = paramvalue.indexOf("0");
    	if( index2 == 0  ){
    		paramvalue = paramvalue.substring(0);
    	}
    	return eval(value) < eval(paramvalue);
    }else{
    	return true;
    }
    
	
	},"下限的值必须小于上限的值<br />");
//上限的值必须大于下限的值
jQuery.validator.addMethod("big",function(value,element, param){
	value =value.replace(/(^\s*)|(\s*$)/g, "");
	if(value ==""){
		return true;
	}	
	if( value !="" ){
    	var index1 = value.indexOf("0");
    	if( index1 == 0  ){
    		value = value.substring(0);
    	}
    }
    var paramvalue = $(param).val();
    if( paramvalue !="" ){
    	var index2 = paramvalue.indexOf("0");
    	if( index2 == 0  ){
    		paramvalue = paramvalue.substring(0);
    	}
    	return eval(value) > eval(paramvalue);
    }else{
    	return true;
    }
    	
},"上限的值必须大于下限的值<br />");
//中文字两个字节
jQuery.validator.addMethod("byteRangeLength", function(value, element, param) {
    var length = value.length;
    for(var i = 0; i < value.length; i++){
        if(value.charCodeAt(i) > 127){
        length++;
        }
    }
    return this.optional(element) || ( length >= param[0] && length <= param[1] );
}, jQuery.format("请确保输入的值在{0}-{1}个字节之间(一个中文字算2个字节)"));
jQuery.validator.addMethod("byteLength", function(value, element, param) {
	  var length = value.length;
	    for(var i = 0; i < value.length; i++){
	        if(value.charCodeAt(i) > 127){
	        length++;
	        }
	    }
    return this.optional(element) || ( length <= param);
}, jQuery.format("请确保输入的值不多于{0}字节(一个中文字算2个字节)<br />"));
}catch(e){
	
}
});
