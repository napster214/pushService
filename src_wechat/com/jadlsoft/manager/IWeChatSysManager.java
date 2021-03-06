package com.jadlsoft.manager;

import java.util.List;
import java.util.Map;

import com.jadlsoft.domain.WeChatJbxxBean;
import com.jadlsoft.domain.WeChatXtszBean;

/**
 * 微信后台业务处理接口
 * @类名: IWeChatSysManager
 * @作者: lcx
 * @时间: 2017-8-4 下午2:30:14
 */
public interface IWeChatSysManager {

	/**
	 * 保存
	 * @param weChatJbxxBean
	 * @return
	 * @return: int
	 */
	int save(WeChatJbxxBean weChatJbxxBean);
	
	/**
	 * 更新
	 * @param weChatJbxxBean
	 * @return: int
	 */
	int update(WeChatJbxxBean weChatJbxxBean);
	
	/**
	 * 按字段更新
	 * @param weChatJbxxBean
	 * @param fields
	 * @return: int
	 */
	int update(WeChatJbxxBean weChatJbxxBean, String fields);

	/**
	 * 根据id获取公众号信息
	 * @param id
	 * @return: WeChatJbxxBean
	 */
	WeChatJbxxBean get(String id);

	/**
	 * 根据公众号名称获取对象
	 * @param gzhmc 公众号名称
	 * @return: WeChatJbxxBean
	 */
	WeChatJbxxBean getByGzhmc(String gzhmc);

	/**
	 * 根据appid和应用名称获取公众号对象
	 * @param appid appid
	 * @param yyname 应用名称
	 * @return: WeChatJbxxBean
	 */
	WeChatJbxxBean getByAppidAndYyname(String appid, String yyname);
	
	/**
	 * 获取所有公众号信息，包含系统设置信息 
	 * @return: List
	 */
	List getAllWithXtsz();
	
	/**
	 * 根据appid获取公众号信息，包含系统设置
	 * @param appid
	 * @return: Map
	 */
	Map getWithXtszByAppid(String appid);
	
	/**
	 * 根据公众号的id获取公众号信息，包含系统设置
	 * @param id	微信的id
	 * @return: Map
	 */
	Map getWithXtszByWxid(String id);
	
	/**
	 * 根据公众号微信号获取公众号信息，包含系统设置
	 * @param appno	公众号的微信号
	 * @return: Map
	 */
	Map getWithXtszByAppno(String appno);
	
	//---------------------------系统设置操作--------------------------------------
	
	/**
	 * @param xtszBean
	 * @return: int
	 */
	int saveXtsz(WeChatXtszBean xtszBean);
	
	/**
	 * 更新
	 * @param xtszBean
	 * @return: int
	 */
	int updateXtsz(WeChatXtszBean xtszBean);
	
	/**
	 * 按字段更新
	 * @param xtszBean
	 * @param fields
	 * @return: int
	 */
	int updateXtsz(WeChatXtszBean xtszBean, String fields);
	
	/**
	 * 删除微信id下的系统设置信息
	 * @param wxid 微信的id
	 * @return: int
	 */
	int deleteXtszByWxid(String wxid);

	//-----------------------------------handle处理操作---------------------------------
	/**
	 * 获取当前的accesstoken
	 * @param appid
	 * @return: String
	 */
	String getCurrentAccessToken(String appid);

}
