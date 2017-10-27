package com.jadlsoft.struts.form;

import org.apache.struts.action.ActionForm;

import com.jadlsoft.domain.WeChatJbxxBean;
import com.jadlsoft.domain.WeChatXtszBean;

public class WeChatSysForm extends ActionForm {

	private static final long serialVersionUID = 2811828614476459209L;
	
	private WeChatJbxxBean weChatJbxxBean = new WeChatJbxxBean();
	
	private WeChatXtszBean weChatXtszBean = new WeChatXtszBean();
	
	private String xtszid;
	
	public String getXtszid() {
		return weChatXtszBean.getId();
	}
	public void setXtszid(String xtszid) {
		weChatXtszBean.setId(xtszid);
	}
	
	public WeChatJbxxBean getWeChatJbxxBean() {
		return weChatJbxxBean;
	}
	public void setWeChatJbxxBean(WeChatJbxxBean weChatJbxxBean) {
		this.weChatJbxxBean = weChatJbxxBean;
	}
	
	public WeChatXtszBean getWeChatXtszBean() {
		return weChatXtszBean;
	}
	public void setWeChatXtszBean(WeChatXtszBean weChatXtszBean) {
		this.weChatXtszBean = weChatXtszBean;
	}
	public String getId() {
		return weChatJbxxBean.getId();
	}
	public void setId(String id) {
		weChatJbxxBean.setId(id);
	}
	public String getGzhmc() {
		return weChatJbxxBean.getGzhmc();
	}
	public void setGzhmc(String gzhmc) {
		weChatJbxxBean.setGzhmc(gzhmc);
	}
	public String getAppid() {
		return weChatJbxxBean.getAppid();
	}
	public void setAppid(String appid) {
		weChatJbxxBean.setAppid(appid);
	}
	public String getAppno() {
		return weChatJbxxBean.getAppno();
	}
	public void setAppno(String appno) {
		weChatJbxxBean.setAppno(appno);
	}
	public String getAppsecret() {
		return weChatJbxxBean.getAppsecret();
	}
	public void setAppsecret(String appsecret) {
		weChatJbxxBean.setAppsecret(appsecret);
	}
	public String getZt() {
		return weChatJbxxBean.getZt();
	}
	public void setZt(String zt) {
		weChatJbxxBean.setZt(zt);
	}
	public String getHddz() {
		return weChatXtszBean.getHddz();
	}
	public void setHddz(String hddz) {
		weChatXtszBean.setHddz(hddz);
	}
	
	//-------------------------系统设置bean-----------------------------
	
	
}
