package com.jadlsoft.constants;

/**
 * 服务公共常量信息
 * @类名: ServiceConstants
 * @作者: lcx
 * @时间: 2017-8-3 下午2:42:22
 */
public class SystemConstants {

	/**
	 * 系统换行符
	 */
	public static final String LINESEPARATOR  = System.getProperty("line.separator", "\n");
	
	/**
	 * 文件上传的类型
	 */
	public static final String UPLOADTYPE_NORMAL = "01";
	public static final String UPLOADTYPE_TEMP  = "02";
	
	/**
	 * 对外统一返回状态码
	 */
	public static final String RESULT_SUCCESS = "00000";	//成功
	public static final String RESULT_FAIL = "11111";	//失败
	
	/**
	 * 状态
	 */
	public static final String ZT_TRUE = "0";
	public static final String ZT_FALSE = "1";
	
	/**
	 * 推送统一返回码
	 * 	失败码规则如下：
	 * 		4位数字
	 * 			首位数字代表返回结果的服务类型
	 * 				1xxx：公共通用信息（如缺少参数）
	 * 				2xxx：短信单独的信息
	 * 				3xxx：邮箱单独的信息
	 * 				4xxx：微信单独的信息
	 * 			第二位代表具体的失败原因分类
	 * 				?1xx：参数相关
	 * 				?2xx：推送结果相关
	 * 				?3xx：内容相关
	 * 				?4xx：异常信息相关
	 */
	public static final String STATUSCODE_OK = "0000";	//通用成功
	public static final String STATUSCODE_ERR = "9999";	//通用失败
	/*
	 * 公共通用信息
	 */
	public static final String STATUSCODE_ERR_COM_ARGLOSS = "1100";		//缺失参数
	public static final String STATUSCODE_ERR_COM_ARGERR = "1101";		//参数格式不对
	public static final String STATUSCODE_ERR_COM_HASPUSHERR = "1210";		//有推送失败的
	
	/*
	 * 邮箱相关
	 */
	public static final String STATUSCODE_ERR_EMAIL_NOHEADORMSG	 = "3300";		//没有标题或者内容
	public static final String STATUSCODE_ERR_EMAIL_PUSHERR = "3310";	//推送失败
	
	/*
	 * 微信相关
	 */
	public static final String STATUSCODE_ERR_WECHAT_NOTBIND = "4300";	//公众号未绑定
	public static final String STATUSCODE_ERR_WECHAT_PARSERESPERR = "4200";	//解析微信返回数据失败
	public static final String STATUSCODE_ERR_WECHAT_RESPERRCODE = "4210";	//微信返回的状态码为失败（需要将错误信息作为参数返回，方便查看）
	
}
