package com.jadlsoft.domain;

import java.io.Serializable;

import com.jadlsoft.utils.StringUtils;

/**
 * 邮箱bean对象
 * @类名: EmailBean
 * @作者: lcx
 * @时间: 2017-7-24 下午3:47:52
 */
public class EmailBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String[] emails; 
	private String subject; 
	private String content; 
	private String emailCc; 
	private String emailBcc;
	private AttachmentBean[] atts;
	private Boolean isAtt;
	
	/*
	 * 校验
	 */
	public Object[] validate() {
		//1、非空校验
		if (emails == null || emails.length==0) {
			return new Object[]{false,"收件人不能为空！"};
		}
		if (StringUtils.isBlank(content) && (atts == null || atts.length==0)) {
			//内容和附件都为空
			return new Object[]{false,"内容和附件不能同时为空！"};
		}
		//2、emails正则校验
		String check = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
		StringBuffer sb = new StringBuffer();
		for (String email : emails) {
			if (!email.matches(check)) {
				sb.append("【"+email+"】,");
			}
		}
		if (sb.length()>0) {
			//有不满足正则校验的信息
			return new Object[]{false, "以下邮箱格式不正确："+sb.substring(0, sb.length()-1)};
		}
		return new Object[]{true};
	}
	
	public String[] getEmails() {
		return emails;
	}
	public void setEmails(String[] emails) {
		this.emails = emails;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
	public String getEmailBcc() {
		return emailBcc;
	}
	public void setEmailBcc(String emailBcc) {
		this.emailBcc = emailBcc;
	}
	public AttachmentBean[] getAtts() {
		return atts;
	}
	public void setAtts(AttachmentBean[] atts) {
		this.atts = atts;
	}
	public Boolean isAtt() {
		//判断，如果是null，说明没有设置，就根据是否有附件去设置
		if (isAtt == null) {
			if (atts != null && atts.length>0) {
				isAtt = true;
			}else {
				isAtt = false;
			}
		}
		return isAtt;
	}
	public void setIsAtt(Boolean isAtt) {
		this.isAtt = isAtt;
	}
}
