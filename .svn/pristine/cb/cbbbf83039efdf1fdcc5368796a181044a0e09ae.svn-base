package com.jadlsoft.manager.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.constants.WeChatConstants;
import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.domain.WeChatJbxxBean;
import com.jadlsoft.domain.WeChatXtszBean;
import com.jadlsoft.handler.GzhHandler;
import com.jadlsoft.manager.IWeChatSysManager;
import com.jadlsoft.utils.StringUtils;

public class WeChatSysManager extends BaseManager implements IWeChatSysManager {

	private DaoUtils daoUtils;
	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}

	@Override
	public int save(WeChatJbxxBean weChatJbxxBean) {
		
		String id = null;
		synchronized (weChatJbxxBean) {
			id = String.valueOf(daoUtils.getNextval("Q_WX_JBXX"));
		}
		weChatJbxxBean.setId(id);
		weChatJbxxBean.setZt(SystemConstants.ZT_TRUE);
		
		return daoUtils.save(weChatJbxxBean);
	}

	@Override
	public int update(WeChatJbxxBean weChatJbxxBean) {
		return daoUtils.update(weChatJbxxBean);
	}

	@Override
	public int update(WeChatJbxxBean weChatJbxxBean, String fields) {
		return daoUtils.updateBlob(weChatJbxxBean, fields);
	}

	@Override
	public WeChatJbxxBean get(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		Object object = daoUtils.findObjectCompatibleNull(WeChatJbxxBean.class, condition);
		if (object == null) {
			return null;
		}
		return (WeChatJbxxBean) object;
	}

	@Override
	public WeChatJbxxBean getByGzhmc(String gzhmc) {
		Map condition = new HashMap();
		condition.put("gzhmc", gzhmc);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(WeChatJbxxBean.class, condition);
		return object == null ? null : (WeChatJbxxBean) object;
	}
	
	@Override
	public WeChatJbxxBean getByAppidAndYyname(String appid, String yyname) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("yyname", yyname);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object object = daoUtils.findObjectCompatibleNull(WeChatJbxxBean.class, condition);
		return object == null ? null : (WeChatJbxxBean) object;
	}
	
	@Override
	public List getAllWithXtsz() {
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#wechatsys.getAllWithXtsz", condition);
	}
	
	@Override
	public Map getWithXtszByAppid(String appid) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = daoUtils.find("#wechatsys.getWithXtszByAppid", condition);
		if (StringUtils.isNullOrEmpty(list)) {
			return null;
		}
		return (Map) list.get(0);
	}
	
	@Override
	public Map getWithXtszByWxid(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = daoUtils.find("#wechatsys.getWithXtszByWeid", condition);
		if (StringUtils.isNullOrEmpty(list)) {
			return null;
		}
		return (Map) list.get(0);
	}
	
	@Override
	public Map getWithXtszByAppno(String appno) {
		Map condition = new HashMap();
		condition.put("appno", appno);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = daoUtils.find("#wechatsys.getWithXtszByAppno", condition);
		if (StringUtils.isNullOrEmpty(list)) {
			return null;
		}
		return (Map) list.get(0);
	}
	
	//-------------------------------------系统设置操作--------------------------------------
	
	@Override
	public int saveXtsz(WeChatXtszBean xtszBean) {
		String id = null;
		synchronized (xtszBean) {
			id = String.valueOf(daoUtils.getNextval("Q_WX_XTSZ"));
		}
		xtszBean.setId(id);
		xtszBean.setZt(SystemConstants.ZT_TRUE);
		return daoUtils.save(xtszBean);
	}
	
	@Override
	public int updateXtsz(WeChatXtszBean xtszBean) {
		return daoUtils.update(xtszBean);
	}
	
	@Override
	public int updateXtsz(WeChatXtszBean xtszBean, String fields) {
		return daoUtils.update(xtszBean, fields);
	}
	
	@Override
	public int deleteXtszByWxid(String wxid) {
		Map condition = new HashMap();
		condition.put("wxid", wxid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.execSql("#wechatsys.deleteXtszByWxid", condition);
	}
	
	//-----------------------------------handle处理操作---------------------------------
	@Override
	public String getCurrentAccessToken(String appid) {
		String accessToken = "";
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List find = daoUtils.find("#wechatsys.getWithXtszByAppid", condition);
		Map map = this.getWithXtszByAppid(appid);
		if (StringUtils.isNullOrEmpty(map)) {
			Log.error("【公众号处理器】获取appid为"+appid+"的当前accessToken失败！原因为：当前库中没有该appid对应的公众号信息！");
			return null;
		}else {
			if (StringUtils.isNullOrEmpty(map.get("accesstoken"))) {
				accessToken = "";
			}else {
				accessToken = (String) map.get("accesstoken");
			}
			//如果为空或者已经过期，就重新获取
			long lasttimemillis = map.get("lasttimemillis") == null ? 0l : ((BigDecimal) map.get("lasttimemillis")).longValue();
			long currenttimemills = System.currentTimeMillis();
			if (StringUtils.isBlank(accessToken) || 
					currenttimemills - lasttimemillis > WeChatConstants.GZH_ACCESSTOKEN_EXPIRES) {
				//需要重新获取
				String xtszid = (String) map.get("xtszid");
				String appno = (String) map.get("appno");
				String appsecret = (String) map.get("appsecret");
				accessToken = GzhHandler.getInstance(appno, appid, appsecret).getAccessToken();
				//获取成功之后，更新数据，将最新的信息保存在库中
				WeChatXtszBean xtszBean = new WeChatXtszBean();
				xtszBean.setId(xtszid);
				xtszBean.setAccessToken(accessToken);
				xtszBean.setLasttimemillis(System.currentTimeMillis());
				this.updateXtsz(xtszBean, "accessToken,lasttimemillis");
			}
			return accessToken;
		}
	}
}
