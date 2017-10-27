package com.jadlsoft.handler;

import java.util.HashMap;
import java.util.Map;

import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.httpRequestProxy.HttpRequestProxy;
import com.jadlsoft.utils.httpRequestProxy.HttpUtils;

public class TestUse {
	
	private static String lineSeparator = System.getProperty("line.separator", "\r\n");
	
	private static String appid = "wxfbd19e4f6f8796d9";
	private static String appsecret = "0b89e63850462ca8f72ad0468aa66424";
	private static String appno = "gh_998b22f2adad";
	private static String accessToken = "sNn2nrRtSINBvPmwxADRX_7hgVkrmQxV1I620ictGu4ku0CpjmD-sajoW4k0TjOGwdM527eVMgPSUlD9Yfpqz1qW5XuKkiM05qhp2xlP2Ld7JlKPbYKeSmLiqz5hvuCbWMGbAHAEUO";
	private static GzhHandler handler = GzhHandler.getInstance(appno, appid, appsecret);
	
	private static String getAccessToken() {
		String token = handler.getAccessToken();
		System.out.println(token);
		return token;
	}
	
	
	//发送客服消息（必须在用户主动动作的两天之内可以发送，否则不能发送）
	/*
	 * {
		    "touser":"OPENID",
		    "msgtype":"text",
		    "text":
		    {
		         "content":"Hello World"
		    }
		}
	 */
	public static void sendKfMsg() throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=${ACCESS_TOKEN}";
		url = url.replace("${ACCESS_TOKEN}", accessToken);
		HttpRequestProxy hrp = new HttpRequestProxy();
		Map postData = new HashMap();
		postData.put("touser", "oqXx50ra0u_5FZPNMlEq6WaIUSNY");
		postData.put("msgtype", "text");
		Map textMap = new HashMap();
		StringBuffer sb = new StringBuffer();
		
		
		
		sb.append("故障中心推送：")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】")
			.append(lineSeparator)
			.append("              服务器发生异常信息，请尽快去查看处理！IP地址为：【192.168.20.124】");
		textMap.put("content", sb.toString());
		postData.put("text", textMap);
//		String res = hrp.doRequest(url, postData, null, "UTF-8");
		
		String res = HttpUtils.doRequest(url, "POST", JsonUtil.map2json(postData));
		
		System.out.println(res);
	}
	
	
	public static void main(String[] args) throws Exception {
		sendKfMsg();
//		getAccessToken();
	}
	
}
