package com.jadlsoft.bean;

public class SmsStayBean{
	public static final String db_tablename = "t_sms_stay";
	public static final String db_tablepkfields = "id";
   /*
    * 短信成功状态 ‘0’ 失败 ‘1’ 成功
    */
    private String sucstatus;
   /*
    * 短信网关返回的错误标识
    */
    private String sms_status;
   /*
    * 是否获取回执报告 ‘0’ 未获取  ‘1’ 已获取
    */
    private String isgetres;
   /*
    * 短信网关分配的短信ID
    */
    private String taskid;
   /*
    * 重发短信时间
    */
    private java.util.Date resendtime;
   /*
    * 重发次数
    */
    private String resendcount;
   /*
    * 发送短信时间
    */
    private java.util.Date sendtime;
   /*
    * 生成短信时间
    */
    private java.util.Date intime;
   /*
    * 发送状态 ‘0’ 未发送 ‘1’ 已发送
    */
    private String state;
   /*
    * 电话号码
    */
    private String mobile;
   /*
    * 短信内容
    */
    private String content;
   /*
    * 自增ID
    */
    private String id;
  	
    public void setSucstatus(String sucstatus){
	    this.sucstatus = sucstatus;
    }
    public String getSucstatus(){
	    return this.sucstatus;
    }
    public void setSms_status(String sms_status){
	    this.sms_status = sms_status;
    }
    public String getSms_status(){
	    return this.sms_status;
    }
    public void setIsgetres(String isgetres){
	    this.isgetres = isgetres;
    }
    public String getIsgetres(){
	    return this.isgetres;
    }
    public void setTaskid(String taskid){
	    this.taskid = taskid;
    }
    public String getTaskid(){
	    return this.taskid;
    }
    public void setResendtime(java.util.Date resendtime){
	    this.resendtime = resendtime;
    }
    public java.util.Date getResendtime(){
	    return this.resendtime;
    }
    public void setResendcount(String resendcount){
	    this.resendcount = resendcount;
    }
    public String getResendcount(){
	    return this.resendcount;
    }
    public void setSendtime(java.util.Date sendtime){
	    this.sendtime = sendtime;
    }
    public java.util.Date getSendtime(){
	    return this.sendtime;
    }
    public void setIntime(java.util.Date intime){
	    this.intime = intime;
    }
    public java.util.Date getIntime(){
	    return this.intime;
    }
    public void setState(String state){
	    this.state = state;
    }
    public String getState(){
	    return this.state;
    }
    public void setMobile(String mobile){
	    this.mobile = mobile;
    }
    public String getMobile(){
	    return this.mobile;
    }
    public void setContent(String content){
	    this.content = content;
    }
    public String getContent(){
	    return this.content;
    }
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
  	
    public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("自增ID为空！");
		}
		/* 检查长度 */
		if(sucstatus != null && sucstatus.getBytes().length > 1){
		    errMsg.append("短信成功状态 ‘0’ 失败 ‘1’ 成功超长！容许长度：1。"); 
		}	   	
		if(sms_status != null && sms_status.getBytes().length > 100){
		    errMsg.append("短信网关返回的错误标识超长！容许长度：100。"); 
		}	   	
		if(isgetres != null && isgetres.getBytes().length > 1){
		    errMsg.append("是否获取回执报告 ‘0’ 未获取 ‘1’ 已获取超长！容许长度：1。"); 
		}	   	
		if(taskid != null && taskid.getBytes().length > 50){
		    errMsg.append("短信网关分配的短信ID超长！容许长度：50。"); 
		}	   	
		if(resendcount != null && resendcount.getBytes().length > 1){
		    errMsg.append("重发次数超长！容许长度：1。"); 
		}	   	
		if(state != null && state.getBytes().length > 1){
		    errMsg.append("发送状态 ‘0’ 未发送 ‘1’ 已发送超长！容许长度：1。"); 
		}	   	
		if(mobile != null && mobile.getBytes().length > 50){
		    errMsg.append("电话号码超长！容许长度：50。"); 
		}	   	
		if(content != null && content.getBytes().length > 2000){
		    errMsg.append("短信内容超长！容许长度：2000。"); 
		}	   	
		if(id != null && id.getBytes().length > 50){
		    errMsg.append("自增ID超长！容许长度：50。"); 
		}	   	
	  	return errMsg.toString();
    }
}