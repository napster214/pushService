package com.jadlsoft.listener;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.jadlsoft.constants.WeChatConstants;
import com.jadlsoft.domain.WeChatXtszBean;
import com.jadlsoft.handler.GzhHandler;
import com.jadlsoft.manager.IWeChatSysManager;
import com.jadlsoft.utils.SpringBeanFactory;
import com.jadlsoft.utils.StringUtils;


public class GzhUpdateListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(GzhUpdateListener.class);
	
	private Timer gzhAccessTokenTimer;	//accessToken定时器
	private int delay = 5;  		//定时器延时执行时间(秒)
	private int period = 30 * 60;		//间隔时间
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (gzhAccessTokenTimer != null) {
			gzhAccessTokenTimer.cancel();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		gzhAccessTokenTimer = new Timer();
		gzhAccessTokenTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateGzhAccessToken();
			}
		}, delay * 1000, period * 1000);
		
	}
	
	/**
	 * 更新公众号的AccessToken值
	 * @功能: 循环判断每个公众号的最后更新时间，如果超过了设置的失效时间，就重新获取
	 * 		在循环时候，要先去库中判断该appid是否存在、可用   
	 * @return: void
	 */
	private void updateGzhAccessToken() {
		
		IWeChatSysManager weChatSysManager = (IWeChatSysManager) SpringBeanFactory.getBean("weChatSysManager");
		
		//1、从库中获取所有公众号信息
		List wechatList = weChatSysManager.getAllWithXtsz();
		if (StringUtils.isNullOrEmpty(wechatList)) {
			return;
		}
		//2、遍历所有公众号信息
		for (Map wechatMap : (List<Map>)wechatList) {
			//3、获取最后更新时间
			long lastTimeMillis = wechatMap.get("lasttimemillis") == null ? 0L : ((BigDecimal) wechatMap.get("lasttimemillis")).longValue();
			long currentTimeMillis = System.currentTimeMillis();
			if (StringUtils.isNullOrEmpty(wechatMap.get("accesstoken")) || 
					currentTimeMillis - lastTimeMillis >= WeChatConstants.GZH_ACCESSTOKEN_EXPIRES ) {
				//已经失效（自己定义的失效，其实可能还没有真正的失效）
				//4、重新获取并设置
				String appno = wechatMap.get("appno") == null ? "" : (String) wechatMap.get("appno");
				String appID = wechatMap.get("appid") == null ? "" : (String) wechatMap.get("appid");
				String appsecret = wechatMap.get("appsecret") == null ? "" : (String) wechatMap.get("appsecret");
				String accessToken = GzhHandler.getInstance(appno, appID, appsecret).getAccessToken();
				if (accessToken == null) {
					//获取失败
					log.error("【公众号更新】获取access_token失败！");
					continue;
				}else {
					WeChatXtszBean bean = new WeChatXtszBean();
					bean.setId((String) wechatMap.get("xtszid"));
					bean.setLasttimemillis(currentTimeMillis);
					bean.setAccessToken(accessToken);
					weChatSysManager.updateXtsz(bean, "accessToken,lasttimemillis");
				}
			}
		}
	}
}
