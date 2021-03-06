package com.jadlsoft.constants;

/**
 * 微信公众号使用常量信息
 * @类名: WeChatConstants
 * @作者: lcx
 * @时间: 2017-8-2 上午10:31:06
 */
public class WeChatConstants {

	/**
	 * AccessToken的失效时间，微信提供的是7200s，项目中使用1个小时20分钟
	 * 	然后会有定时器30分钟检测一次，超过了这个时间，就会去重置该值，并更新最新时间
	 */
	public static final Long GZH_ACCESSTOKEN_EXPIRES = (long) (80 * 60 * 1000);
	
	/**
	 * 微信发送默认参数设置
	 */
	public static final String MSG_TOPCOLOR = "#173177";	//默认字体颜色
	public static final int EWM_EXPIRESECONDS = 10 * 60;	//创建二维码默认时长
	
	/**
	 * 微信发送成功状态码
	 */
	public static final String MSG_SUCCESS_STATUS = "0";
	
	/**
	 * 微信申请公众号时候使用的TOKEN值
	 */
	public static final String TOKEN_JADLSOFT = "jadlsoft";
	
	/**
	 * 接口地址URL
	 */
	/*
	 * 系统相关
	 */
	//获取ACCESSTOKEN
	public static final String SYS_GETACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?" +
			"grant_type=client_credential&appid=${APPID}&secret=${APPSECRET}";
	
	/*
	 * 用户相关
	 */
	//创建标签
	public static final String USER_CREATETAG = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=${ACCESS_TOKEN}";
	
	//获取标签下粉丝列表
	public static final String USER_GETFANSONTAG = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=${ACCESS_TOKEN}";
	
	//批量为用户打标签
	public static final String USER_BATCHTAGGING = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=${ACCESS_TOKEN}";
	//批量为用户取消标签
	public static final String USER_BATCHUNTAGGING = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=${ACCESS_TOKEN}";
	
	/*
	 * 二维码相关
	 */
	//创建带参二维码
	public static final String EWM_CREATEWITHPARA = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=${ACCESS_TOKEN}";
	
	//获取带参二维码
	public static final String EWM_GETWITHPARA = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${TICKET}";
	
	
	
	
	
	/*
	 * 消息管理相关
	 */
	//发送模板消息
	public static final String MSG_SENDTEMPLATEMSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${ACCESS_TOKEN}";
	
	//根据标签进行群发
	public static final String MSG_SENDBYTAG = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=${ACCESS_TOKEN}";
	
	
}
