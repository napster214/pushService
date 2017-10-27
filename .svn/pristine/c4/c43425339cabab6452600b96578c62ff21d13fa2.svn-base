package com.jadlsoft.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.DelegatingActionUtils;

import com.jadlsoft.business.DxfsManager;
import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.utils.MD5Utils;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlsoft.utils.SysProperUtils;
import com.jadlsoft.utils.httpRequestProxy.HttpRequestProxy;
/**
 * 
 * 短信发送定时器
 * @author wujiaxu
 * @Time 2017-10-12 下午4:08:11
 *
 */
public class DxfsPlugin implements PlugIn{

	private static final Logger logger = Logger.getLogger(DxfsPlugin.class);
	private boolean BOOL_SEND = false; 		//发送短信开关
	private boolean BOOL_GETSTATUS = false; //获取短信状态开关
	private DxfsManager dxfsManager;
	private WebApplicationContext wac = null;
	private Timer timer;

	@Override
	public void init(ActionServlet servlet, ModuleConfig config)
			throws ServletException {
		wac = DelegatingActionUtils.findRequiredWebApplicationContext(servlet,
				config);
		final DaoUtils daoUtils = (DaoUtils) wac.getBean("daoUtils");
		dxfsManager = new DxfsManager();
		dxfsManager.setDaoUtils(daoUtils);
		timer = new Timer();
		timer.schedule( new SendSmsTask(), 1000, 1000 * 3);//3秒执行1次
		timer.schedule( new GetSmsStatusTask(), 5000,1000 * 5);//5秒执行1次
	}
	
	/**
	 * 
	 * 定时发送短信任务
	 * @author wujiaxu
	 * @Time 2017-10-12 下午5:07:21
	 *
	 */
	public  class SendSmsTask extends TimerTask {
		public void run() {
			if (BOOL_SEND) {
				return;
			}
			BOOL_SEND = true;
			try {
				sendSmsManager();
			} catch (Exception e) {
				logger.error("定时发送短信任务出错!", e);
			} finally {
				BOOL_SEND = false;
			}
		}
		
	}
	
	/**
	 * 
	 * 执行短信发送：发送内容包含两部分，具体如下
	 * 			1）未发送短信
	 * 			2）系统当前时间-重发时间 >= 重发次数 * 重发间隔分钟 并且取回回执内容
	 * @author wujiaxu
	 * @Time 2017-10-12 下午5:12:04
	 */
	private void sendSmsManager(){
		List dxList = dxfsManager.getDfsdxList();
		if(dxList != null && dxList.size() > 0){
			int size = dxList.size();
			for (int i = 0; i < size; i++) {
				Map dxMap = (Map) dxList.get(i);
				String mobile = (String) dxMap.get("mobile");//手机号
				String content = (String) dxMap.get("content");//发送内容
				if(mobile == null || "".equals(mobile) || content == null || "".equals(content)){
					logger.info("手机号或内容不能为空！");
					continue;
				}
				//1、向短信网关提交短信信息
				String resStr = sendSms(mobile,content);
				//2、处理发送结果
				dxfsManager.dealWithYfsSms(dxMap,resStr);
			}
		}
	}
	
	/**
	 * 向短信网关提交短信信息
	 * @param mobile
	 * @param content
	 * @return
	 * @author wujiaxu
	 * @Time 2017-10-16 下午5:06:23
	 */
	private static String sendSms(String mobile,String content){

		String url = SysProperUtils.getProperty("sms.url.sendmsg");
		String userid = SysProperUtils.getProperty("sms.post.userid");
		String account = SysProperUtils.getProperty("sms.post.account");
		String password = SysProperUtils.getProperty("sms.post.password");
		
		Map postData = new HashMap();
		postData.put("userid", userid);
		postData.put("account", account);
		postData.put("password", MD5Utils.getMD5(password));
		postData.put("sendTime", "");
		postData.put("action", "send");
		postData.put("extno", "");
		postData.put("mobile", mobile);
		postData.put("content", content);
		String str = null;

		try {
			/*
			 * 成功返回：{'returnstatus':'Success','message':'操作成功','remainpoint':'33','taskID':'1704113916236027','successCounts':'1'}
			 * 失败返回：{"returnstatus":"Faild","message":"需要签名","remainpoint":"0","taskID":"","successCounts":"0"}
			 * 
			 */
			
			HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
			str = httpRequestProxy.doRequest(url, postData, null, "utf-8");
			
			JSONObject jsonObj = JSONObject.fromObject(str);
			String returnstatus = jsonObj.getString("returnstatus");
			if("Success".equals(returnstatus)){
				logger.info("向短信网关" + mobile + "提交短信成功，返回内容："+str);
			}else if("Faild".equals(returnstatus)){
				logger.info("向短信网关" + mobile + "提交短信失败，失败原因："+jsonObj.getString("message"));
			}
		} catch (Exception e) {
			logger.error("向手机号【"+mobile+"】发送短信【"+content+"】失败！", e);
		}
		
		return str;
	}
	
	
	
	/**
	 * 
	 * 定时获取短信发送状态回执
	 * @author niutongda
	 * @Time 2017-4-11 下午05:09:10
	 *
	 */
	public  class GetSmsStatusTask extends TimerTask {
		public void run() {
			if (BOOL_GETSTATUS) {
				return;
			}
			BOOL_GETSTATUS = true;
			try {
				getSmsStatus();
			} catch (Exception e) {
				logger.error("定时获取短信发送回执任务出错!", e);
			} finally {
				BOOL_GETSTATUS = false;
			}
		}
	}
	
	/**
	 * 从短信网关获取发送状态
	 * 
	 * @author wujiaxu
	 * @Time 2017-10-16 下午4:23:55
	 */
	private void getSmsStatus() {
		List list = dxfsManager.getWhqSmsList();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size() ; i++){
				Map dxMap = (Map) list.get(i);
				String taskId = (String) dxMap.get("taskid");
				if(taskId != null && !"".equals(taskId)){
					JSONArray statusbox = getStatus(taskId);
					if(statusbox != null && statusbox.size() > 0){
						//根据短信回执更新短信信息
						dxfsManager.updateSmsSendResult(statusbox);
					}
				}
			}
		}
		//批量处理发送成功的短信
		dxfsManager.batchDealDxfsSuc();
		//批量处理发送失败的短信
		dxfsManager.batchDealDxfsErr();
	}
	
	/**
	 * 根据任务ID向短信网关获取短信回执
	 * @param dxMap
	 * @return
	 * @author niutongda
	 * @param id 
	 * @Time 2017-4-11 下午05:39:07 
	 *
	 */
	public static JSONArray getStatus(String taskid){
		
		String url = SysProperUtils.getProperty("sms.url.getstatus");
		String userid = SysProperUtils.getProperty("sms.post.userid");
		String account = SysProperUtils.getProperty("sms.post.account");
		String password = SysProperUtils.getProperty("sms.post.password");
		
		Map postData = new HashMap();
		postData.put("userid", userid);
		postData.put("account", account);
		postData.put("password", MD5Utils.getMD5(password));
		postData.put("statusNum", "");
		postData.put("action", "query");
		postData.put("taskid", taskid);
	
		JSONArray statusbox = null;
		try {
			/*
			 * {
			 * 	'error'		:	'1',
			 * 	'remark'	:	'成功',
			 * 	'statusbox'		:	[{
			 * 			'mobile'		:	'13716829667',
			 * 			'taskid'		:	'1704063401357930',
			 * 			'receivetime'	:	'2017-04-06 10:01:37',
			 * 			'errorcode'		:	'DELIVRD'
			 * 	}]
			 * }
			 */
			HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
			String str = httpRequestProxy.doRequest(url, postData, null, "utf-8");
			JSONObject jsonObj = JSONObject.fromObject(str);
			statusbox = jsonObj.getJSONArray("statusbox");
			if(statusbox.size() > 0 ){
				logger.info("获取taskID:"+taskid+"短信回执内容:"+str);
			}
		} catch (Exception e) {
			logger.info("获取任务ID:"+taskid+"短信回执失败！",e);
		}
		return statusbox;
	}

	public static void main(String[] args) {
		String str = "{\"error\":\"1\",\"remark\":\"成功\",\"statusbox\":[{\"mobile\":\"18600755757\",\"taskid\":\"1710173515120547\",\"receivetime\":\"2017-10-17 11:15:16\",\"errorcode\":\"DELIVRD\"}]}";
		String str1 = "{\"error\":\"1\",\"remark\":\"成功\",\"statusbox\":[]}";
		JSONObject jsonObj = JSONObject.fromObject(str);
		JSONArray statusbox = jsonObj.getJSONArray("statusbox");
		System.out.println(statusbox.size());
		System.out.println(statusbox);
		JSONObject jsonObject =  statusbox.getJSONObject(0);
	}
	@Override
	public void destroy() {
		
	}

}
