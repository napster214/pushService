package com.jadlsoft.domain;

public class WeChatXtszBean {

	private String id;
	private String wxid;
	private String accessToken;
	private Long lasttimemillis;
	private String hddz;
	private String zt;
	
	public static final String db_tablename = "T_WX_XTSZ";
	public static final String db_tablepkfields = "id";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getLasttimemillis() {
		return lasttimemillis;
	}
	public void setLasttimemillis(Long lasttimemillis) {
		this.lasttimemillis = lasttimemillis;
	}
	public String getHddz() {
		return hddz;
	}
	public void setHddz(String hddz) {
		this.hddz = hddz;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
}
