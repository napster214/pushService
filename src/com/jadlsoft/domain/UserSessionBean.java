package com.jadlsoft.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * 登录系统后保存到SESSION中的用户信息
 * @author 张方俊 2008-5-26 : 上午09:59:39
 *
 */
public class UserSessionBean implements Serializable, HttpSessionBindingListener{
	
	private static final long serialVersionUID = 8324605811779623585L;		
	
	private static Map GLOBAL_SESSION = new HashMap();
	
	private String logintime;
	/*
	 *  过滤条件行政区划字段
	 */
	private String xzqh;	
	/* 
	 * 行政区划中文名??????????
	 */
	private String xzqh_cn;
	/*
	 * 用户的功能代码
	 */
	private List gndm;
	/*
	 * 卡编号。暂存用户公民身份号码。ZFJ_TODO
	 */	
	private String kbh;
	/*
	 * 证书序号
	 */
	private String caId;
	/*
	 * 用户名
	 */
	private String userName;
	/*
	 * 用户公民身份号码
	 */
	private String userSfz;
	/*
	 * 单位代码
	 */
	private String dwdm;
	/*
	 * 单位名称
	 */
	private String dwmc;
	/*
	 * 状态
	 */
	private String zt;
	/*
	 * 登陆IP
	 */
	private String loginip;
	/*
	 * 用户类型 
	 */
	private String yhlx;
	/*
	 * 用户id
	 */	
	private String userid;
	
	/*
	 * 具体行政区划
	 */
	private String  zxzqh ;
	
	private String userType;
	private String linkUrl;
	
	
	public String getZxzqh() {
		return zxzqh;
	}
	public void setZxzqh(String zxzqh) {
		this.zxzqh = zxzqh;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getYhlx() {
		return yhlx;
	}
	public void setYhlx(String yhlx) {
		this.yhlx = yhlx;
	}
//	public UserSessionBean(LoginManager loginManager) {
//		this.loginManager = loginManager;
//	}
	public UserSessionBean() {}
	
//	public LoginManager getLoginManager() {
//		return loginManager;
//	}
//	public void setLoginManager(LoginManager loginManager) {
//		this.loginManager = loginManager;
//	}
	
	public String getDwmc()
	{
		return dwmc;
	}

	public void setDwmc(String dwmc)
	{
		this.dwmc = dwmc;
	}
	
	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getXzqh()
	{
		return xzqh;
	}

	public void setXzqh(String xzqh)
	{
		this.xzqh = xzqh;
	}

	public String getXzqh_cn()
	{
		return xzqh_cn;
	}

	public void setXzqh_cn(String xzqh_cn)
	{
		this.xzqh_cn = xzqh_cn;
	}

	public String getZt()
	{
		return zt;
	}

	public void setZt(String zt)
	{
		this.zt = zt;
	}

	public List getGndm() {
		return gndm;
	}

	public void setGndm(List gndm) {
		this.gndm = gndm;
	}

	public static Map getGLOBAL_SESSION() {
		return GLOBAL_SESSION;
	}

	public static void setGLOBAL_SESSION(Map global_session) {
		GLOBAL_SESSION = global_session;
	}
	
	//session绑定的时候自动调用,比如：request.getSession(true).setAttribute("test", your class object)
	public void valueBound(HttpSessionBindingEvent arg0) {
		/*GLOBAL_SESSION.put(userSfz, arg0.getSession());
		this.setLogintime(DateUtils.getCurrentDataTime());
		Map condition = new HashMap();
		condition.put("kbh", userSfz);
		condition.put("dwdm", dwdm);
		condition.put("username", userName);
		condition.put("logintime", logintime);
		condition.put("ip", loginip);
		condition.put("state", "0");
		loginManager.saveSessionInfo(condition);*/
	}

	// session解绑的时候自动调用,比如：request.getSession(true).removeAttribute("test")
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		/*GLOBAL_SESSION.remove(userSfz);
		Map condition = new HashMap();
		condition.put("kbh", userSfz);
		condition.put("logintime", logintime);
		condition.put("logouttime", DateUtils.getCurrentDataTime());
		condition.put("state", "1");
		loginManager.updateSessionInfo(condition);*/
	}
	public String getLoginip() {
		return loginip;
	}
	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	public String getKbh() {
		return kbh;
	}
	public void setKbh(String kbh) {
		this.kbh = kbh;
	}
	public String getUserSfz() {
		return userSfz;
	}
	public void setUserSfz(String userSfz) {
		this.userSfz = userSfz;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
}
