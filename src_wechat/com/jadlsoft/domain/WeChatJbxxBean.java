package com.jadlsoft.domain;

public class WeChatJbxxBean {

	private String id;
	private String gzhmc;
	private String appid;
	private String appno;
	private String appsecret;
	private String zt;
	
	public static final String db_tablename = "T_WX_JBXX";
	public static final String db_tablepkfields = "id";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGzhmc() {
		return gzhmc;
	}
	public void setGzhmc(String gzhmc) {
		this.gzhmc = gzhmc;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
}
