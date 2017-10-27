package com.jadlsoft.email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.jadlsoft.domain.AttachmentBean;
import com.jadlsoft.domain.EmailBean;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SysProperUtils;

public class EmailUtils {

	private static Properties properties;
	private static String fromEmail  = SysProperUtils.getProperty("mail.from");// 发件人邮箱
	private static String fromAuthCode  = SysProperUtils.getProperty("mail.fromAuthCode");// 发件人认证信息
	
	public static void main(String[] args) throws Exception {
//		sendSingleEmail(new String[]{"18337127032@163.com","1017392558@qq.com"}, "主题", "<hr/><h3>你好啊</h3>", "", "");
//		sendByQQ("18337127032@163.com", "主题", "<hr/><h3>你好啊</h3>");
//		AttachmentBean att1 = new AttachmentBean("C:\\Users\\Administrator\\Desktop\\GIS服务说明.docx");
//		AttachmentBean att2 = new AttachmentBean("C:\\Users\\Administrator\\Desktop\\kuaidi100.txt", "快递使用.txt");
//		sendEmailWithAtt(new String[]{"18337127032@163.com","1017392558@qq.com"}, "主题", 
//				"<hr/><h3>你好啊</h3>", "", "", new AttachmentBean[]{att1,att2});
		EmailBean emailBean = new EmailBean();
		emailBean.setEmails(new String[]{"1017392558@qq.com"});
//		emailBean.setAtts(new AttachmentBean[]{att1,att2});
		emailBean.setSubject("测试邮件工具类");
		emailBean.setContent("<center><h1>这个是邮件的标题</h1><center><hr/><p>这个是正文</p>");
		sendEmail(emailBean);
	}
	
	static {
		properties = new Properties();
		properties.put("mail.transport.protocol", SysProperUtils.getProperty("mail.transport.protocol"));// 连接协议
		properties.put("mail.smtp.host", SysProperUtils.getProperty("mail.smtp.host"));// 发件人的邮箱的 SMTP 服务器地址
		properties.put("mail.smtp.port", SysProperUtils.getProperty("mail.smtp.port"));// 端口号
		properties.put("mail.smtp.auth", SysProperUtils.getProperty("mail.smtp.auth"));// 是否需要请求认证
		properties.put("mail.smtp.ssl.enable", SysProperUtils.getProperty("mail.smtp.ssl.enable"));// 设置是否使用ssl安全连接 ---一般都使用
		properties.put("mail.debug", SysProperUtils.getProperty("mail.debug"));// 设置是否显示debug信息 true 会在控制台显示相关信息
	}
	
	/**
	 * 发送邮件，支持带附件和不带附件的
	 * @param emailBean	封装起来的邮件对象
	 * @return: void
	 */
	public static void sendEmail(EmailBean emailBean) {
		/*
		 * 校验邮箱对象是否格式正确
		 */
		if (emailBean == null) {
			return;
		}
		Object[] validate = emailBean.validate();
		if (!(Boolean) validate[0]) {
			//格式不正确
			throw new RuntimeException((String) validate[1]);
		}
		//获取bean对象
		String[] emails = emailBean.getEmails();
		String subject = emailBean.getSubject();
		String content = emailBean.getContent();
		String emailCc = emailBean.getEmailCc();
		String emailBcc = emailBean.getEmailBcc();
		AttachmentBean[] atts = emailBean.getAtts();
		try {
			sendEmailTemplete(emails, subject, content, emailCc, emailBcc, emailBean.isAtt(), atts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 发送邮件信息
	 */
	private static void sendEmailTemplete(String[] emails, String subject, String content, 
			String emailCc, String emailBcc,boolean isAtt, AttachmentBean[] atts) throws Exception {
		if (emails == null || emails.length==0) {
			return;
		}
		//1、获取session
		Session session = Session.getInstance(properties);
		//2、获取邮件对象
		Message message = new MimeMessage(session);
		//3、设置message信息
		fillMessage(message, emails, subject, content, emailCc, emailBcc, isAtt, atts);
		//4、发送
		Transport transport = session.getTransport();	//得到邮差对象
		transport.connect(fromEmail, fromAuthCode);// 连接自己的邮箱账户
		transport.sendMessage(message, message.getAllRecipients());	//发送邮件 
	}
	
	/*
	 * 设置message信息
	 */
	private static void fillMessage(Message message, String[] emails, String subject, String content, 
			String emailCc, String emailBcc, boolean isAtt, AttachmentBean[] atts) throws Exception {
		message.setFrom(new InternetAddress(fromEmail));		//发件人的邮箱
		//设置收件人地址 
		InternetAddress[] toEmails = new InternetAddress[emails.length];
		for (int i = 0; i < emails.length; i++) {
			toEmails[i] = new InternetAddress(emails[i]);
		}
		message.setRecipients( RecipientType.TO, toEmails);
		//设置抄送人 
		if (!StringUtils.isBlank(emailCc)) {
			message.setRecipient( RecipientType.CC, new InternetAddress(emailCc));
		}
		//设置密送人
		if (!StringUtils.isBlank(emailBcc)) {
			message.setRecipient( RecipientType.BCC, new InternetAddress(emailBcc));
		}
		//设置邮件标题
		message.setSubject(subject);
		//设置邮件内容（支持HTML）
		fillContent(message, content, isAtt, atts);
	}
	
	/*
	 * 填充邮件内容信息
	 */
	private static void fillContent(Message message, String content, 
			boolean isAtt, AttachmentBean[] atts) throws Exception {
		if (isAtt) {
			
			//0、创建最终要加进内容中的大节点
			MimeMultipart bigPart = new MimeMultipart();
			//1、创建文本节点，并放在外层节点中
			MimeBodyPart textContent = new MimeBodyPart();
			textContent.setContent(content, "text/html;charset=UTF-8");
			bigPart.addBodyPart(textContent);
			//2、创建附件节点并放进外层节点中
			if (atts != null && atts.length>0) {
				for (AttachmentBean attachmentBean : atts) {
					MimeBodyPart attachment = new MimeBodyPart();
					DataHandler dh = new DataHandler(new FileDataSource(attachmentBean.getFilepath()));  // 读取本地文件
					attachment.setDataHandler(dh);                                          	// 将附件数据添加到“节点”
					attachment.setFileName(MimeUtility.encodeText(attachmentBean.getFilename()));     // 设置附件的文件名（需要编码）
					//加进外层节点中
					bigPart.addBodyPart(attachment);
				}
			}
			bigPart.setSubType("mixed");   //设置混合方式
			message.setContent(bigPart);
		}else {
			//普通文本内容
			message.setContent(content, "text/html;charset=UTF-8");
		}
	}
}
