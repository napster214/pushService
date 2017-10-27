package com.jadlsoft.cache;

/**
 * 公众号缓存信息
 * @类名: WeChatCache
 * @描述: 存储所有微信服务相关的缓存信息
 * @作者: lcx
 * @时间: 2017-8-2 上午11:01:38
 */
public class WeChatCache {

	
	//存储接收微信信息的时间（避免扫码一下扫多次产生冗余信息）
	public static long lastDealMillis = 0l;
	
	//单例
	private WeChatCache() {}
	private static WeChatCache weChatCache = new WeChatCache();
	public static WeChatCache getInstance() {
		return weChatCache;
	}
	
}
