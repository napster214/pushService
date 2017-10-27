package com.jadlsoft.handler;

import org.apache.log4j.Logger;

import com.jadlsoft.domain.EmailBean;
import com.jadlsoft.email.EmailUtils;
import com.jadlsoft.utils.StringUtils;

/**
 * 具体完成邮件推送的处理
 * @类名: EmailHandler
 * @作者: lcx
 * @时间: 2017-8-18 上午11:02:43
 */
public class EmailHandler {

	private EmailHandler() {};
	
	public static EmailHandler getInstance() {
		return new EmailHandler();
	}
	
	private Logger logger = Logger.getLogger(EmailHandler.class);
	
	/**
	 * 发送简单消息
	 * @功能: 给单个指定用户发送简单消息，不涉及附件、密送、抄送等信息
	 * @param touser	目标邮箱
	 * @param msg	信息
	 * @param title	标题
	 * @return: void
	 */
	public boolean sendSingleMsg(String touser, String msg, String title) {
		if (StringUtils.isBlank(touser) || StringUtils.isBlank(msg) 
				|| StringUtils.isBlank(title)) {
			logger.error("【邮箱发送处理】发送简单消息参数为空！");
			return false;
		}
		EmailBean emailBean = new EmailBean();
		emailBean.setContent(msg);
		emailBean.setSubject(title);
		emailBean.setEmails(new String[]{touser});
		
		try {
			synchronized (emailBean) {
				EmailUtils.sendEmail(emailBean);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
