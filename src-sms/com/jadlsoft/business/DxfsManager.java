package com.jadlsoft.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jadlsoft.bean.SmsStayBean;
import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.DxConstants;

/**
 * 
 * 短信业务处理类
 * @author wujiaxu
 * @Time 2017-10-12 下午5:13:48
 *
 */
public class DxfsManager extends BaseManager{
	
	private static final Logger logger = Logger.getLogger(DxfsManager.class); 
	
	/**
	 * 保存待发送短信
	 * @param content
	 * @param mobile
	 * @return
	 * @author wujiaxu
	 * @Time 2017-10-16 上午9:39:49
	 */
	public int saveDx(String content,String mobile){
		Map condition = new HashMap();
		condition.put("content", content);
		condition.put("mobile", mobile);
		condition.put("state", DxConstants.SEND_STATUS_NO);
		
		return daoUtils.execSql("#sms.insertDx", condition);
	}
	
	
	/**
	 *  获取待发送短信列表
	 * （1）未发送的短信（state='0'）
	 * （2）发送失败的短信
	 * 		已取得回执（ isgetres=1）
	 *      && 
	 *      发送失败（sucstatus =0）
	 *      &&
	 *      重发次数小于5次（resendcount < 5）
	 *      &&
	 *      大于重发间隔时间（系统当前时间 - 重发时间  > 重发次数 * 重发间隔时间）
	 * @return
	 * @author wujiaxu
	 * @Time 2017-10-12 下午5:14:09
	 */
	public List getDfsdxList() {
		Map condition = new HashMap();
		condition.put("state", DxConstants.SEND_STATUS_NO);
		condition.put("isgetres", DxConstants.ISGETRES_Y);
		condition.put("sucstatus", DxConstants.DATA_STATUS_FAILE);
		condition.put("resendcount", DxConstants.RESENDCOUNT);
		condition.put("resendtime", DxConstants.RESENDTIME);
		return daoUtils.find("#sms.getDfsdxList", condition);
	}

	
	/**
	 *  处理发送结果
	 *    	1）提交成功返回taskID的短信
	 *    		更新发送状态为已发送、发送时间、任务ID、回执状态
	 * 		2）提交失败返回错误信息的短信
	 * 			更新错误信息，并将短信转移到错误表中，删除待发送表中的数据
	 * @param dxMap
	 * @param resStr
	 * @author niutongda
	 * @Time 2017-4-11 下午03:11:21 
	 *
	 */
	public void dealWithYfsSms(Map dxMap, String resStr) {
		if(resStr == null || "".equals(resStr)){
			return;
		}
		
		//1、更新短信信息表
		String dxid = (String) dxMap.get("id");
		JSONObject jsonObj = JSONObject.fromObject(resStr);
		String returnstatus = jsonObj.getString("returnstatus") == null ? "" : jsonObj.getString("returnstatus");//返回的状态
		String taskID = jsonObj.getString("taskID") == null ? "" : jsonObj.getString("taskID");//返回的任务ID
		String sms_status = jsonObj.getString("message") == null ? "" : jsonObj.getString("message");//返回的描述
		
		SmsStayBean stayBean = new SmsStayBean();
		stayBean.setId(dxid);
		stayBean.setState(DxConstants.SEND_STATUS_YES);
		stayBean.setSendtime(DateUtils.createCurrentDate());
		stayBean.setIsgetres(DxConstants.ISGETRES_N);
		stayBean.setSms_status(sms_status);
		stayBean.setTaskid(taskID);
		
		String updateFile = "state,sendtime,isgetres,sms_status,taskid";
		
		int re = this.daoUtils.update(stayBean,updateFile);
	
		//2、有问题的短信转移到错误信息表
		if(re > 0 && "Faild".equalsIgnoreCase(returnstatus)){
			moveErrSms(dxid);
			this.daoUtils.delete(stayBean);
		}

	}
	
	/**
	 * 错误短信转移错误表
	 * @param id
	 * @author wujiaxu
	 * @Time 2017-10-16 下午4:16:58
	 */
	private void moveErrSms(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		daoUtils.execSql("#sms.moveErrSms", condition);
	}
	
	
	/**
	 * 未获取回执的短信列表
	 * @return
	 * @author wujiaxu
	 * @Time 2017-10-16 下午4:25:07
	 */
	public List getWhqSmsList() {
		Map condition = new HashMap();
		condition.put("isgetres", DxConstants.ISGETRES_N);
		condition.put("state", DxConstants.SEND_STATUS_YES);
		return daoUtils.find("#sms.getWhqSmsList", condition);
	}
	
	
	/**
	 * 根据短信回执更新短信信息
	 * 		1、发送成功的短信:
	 * 					发送状态变更为“发送成功”（sucstatus=1）
	 * 					回执状态变更为“已取回（isgetres=1）
	 * 		2、发送失败且重发次数等于5
	 * 					发送状态变更为“发送失败”（sucstatus=0）
	 * 					回执状态变更为“已取回”（isgetres=1）
	 * 		3、发送失败且重发次数小于5 
	 * 					更新重发次数、重发时间
	 * 					回执状态变更为“已取回”（isgetres=1）
	 * @param statusbox
	 * @author niutongda
	 * @param dxMap 
	 * @Time 2017-4-12 下午03:21:37 
	 *
	 */
	public void updateSmsSendResult(JSONArray statusbox) {
		String sql = "";
		Map condition = new HashMap();
		for (int i = 0 ; i < statusbox.size(); i++) {
			JSONObject jsonObject =  statusbox.getJSONObject(i);
			String errorcode = (String) jsonObject.get("errorcode");
			String taskid = (String) jsonObject.get("taskid");
			condition.put("sms_status", errorcode);
			condition.put("taskid", taskid);
			condition.put("isgetres", DxConstants.ISGETRES_Y);
			
			if(DxConstants.SMS_STATES_SUC.equals(errorcode)){
				//发送成功
				sql = " update t_sms_stay set sms_status=:sms_status,sucstatus=:sucstatus,isgetres=:isgetres where taskid=:taskid ";
				condition.put("sucstatus", DxConstants.DATA_STATUS_SUCCESS);
				
			}else{
				//发送失败
				sql = " update t_sms_stay set resendcount=decode(resendcount,'',0,resendcount+1),resendtime=sysdate,sms_status=:sms_status,sucstatus=:sucstatus,isgetres=:isgetres where taskid=:taskid ";
				condition.put("sucstatus", DxConstants.DATA_STATUS_FAILE);
			}
			daoUtils.execSql(sql, condition);
		}
		
	}
	
	
	/**
	 * 批量处理发送成功的短信
	 * @author niutongda
	 * @Time 2017-4-12 上午11:36:27 
	 *
	 */
	public void batchDealDxfsSuc() {
		//1.获取发送成功的短信
		int dxCount = getDxfsSucSize();
		if(dxCount > 0){
			//2.将发送成功的短信批量加入到成功表中
			batchInsertDxSuc();
			//3.删除待发送表中的数据
			batchDeleteDxSuc();
			logger.info("---处理发送成功短信任务结束，共处理" + dxCount + "条短信，时间："+DateUtils.getCurrentDataTime()+"---");
		}
		
	}
	
	/**
	 * 批量处理发送失败的的短信
	 * 
	 * @author niutongda
	 * @Time 2017-4-12 下午01:56:01 
	 *
	 */
	public void batchDealDxfsErr() {
		//1.获取发送失败的短信
		int dxCount = getDxfsErrSize();
		if(dxCount > 0){
			//2.批量将数据加入到t_note_err表中
			batchInsertDxErr();
			//3.删除待发送信息表中的数据
			batchDeleteDxErr();
			logger.info("---处理发送失败短信任务结束，共处理" + dxCount + "条短信，时间："+DateUtils.getCurrentDataTime()+"---");
		}
	}


	/**
	 * 获取发送成功短信数量
	 * @return
	 * @author niutongda
	 * @Time 2017-4-12 上午11:36:58 
	 *
	 */
	private int getDxfsSucSize() {
		String sql = "select count(id) dxsl  from t_sms_stay where sucstatus=:sucstatus";
		Map condition = new HashMap();
		condition.put("sucstatus", DxConstants.DATA_STATUS_SUCCESS);
		return daoUtils.queryForInt(sql, condition);
	}
	
	/**
	 * 获取发送失败短信数量
	 * @return
	 * @author wujiaxu
	 * @Time 2017-10-17 上午11:33:46
	 */
	private int getDxfsErrSize() {
		String sql = "select count(id) dxsl from t_sms_stay where sucstatus=:sucstatus and resendcount >=:resendcount";
		Map condition = new HashMap();
		condition.put("sucstatus", DxConstants.DATA_STATUS_FAILE);
		condition.put("resendcount", DxConstants.RESENDCOUNT);
		return daoUtils.queryForInt(sql, condition);
	}
	
	/**
	 * 批量将发送成功的短信插入到发送成功短信表中
	 * @author niutongda
	 * @Time 2017-4-12 下午01:49:12 
	 *
	 */
	private void batchInsertDxSuc() {
		String sql = " insert into t_sms_suc "+
			" (id, content, mobile, state, intime, sendtime, resendcount, resendtime, taskid, sms_status) "+
			" select Q_sms_suc.nextval,content, mobile, state, intime, sendtime, resendcount, resendtime, taskid, sms_status  "+
			" from t_sms_stay  where sucstatus=:sucstatus "; 
		Map condition = new HashMap();
		condition.put("sucstatus", DxConstants.DATA_STATUS_SUCCESS);
		daoUtils.execSql(sql, condition);
	}
	
	/**
	 * 批量删除待发送短信表中发送成功的短信
	 * 
	 * @author niutongda
	 * @Time 2017-4-12 下午01:53:52 
	 *
	 */
	private void batchDeleteDxSuc() {
		String sql = " delete t_sms_stay where sucstatus =:sucstatus ";
		Map condition = new HashMap();
		condition.put("sucstatus", DxConstants.DATA_STATUS_SUCCESS);
		daoUtils.execSql(sql, condition);
	}

	

	/**
	 * 批量将发送失败的短信插入到发送失败短信表中
	 * @author niutongda
	 * @Time 2017-4-12 下午01:49:12 
	 *
	 */
	private void batchInsertDxErr() {
		String sql = " insert into t_sms_err "+
			" (id, content, mobile, state, intime, sendtime, resendcount, resendtime, taskid, sms_status) "+
			" select Q_sms_err.nextval,content, mobile, state, intime, sendtime, resendcount, resendtime, taskid, sms_status  "+
			" from t_sms_stay  where sucstatus=:sucstatus and resendcount >=:resendcount"; 
		Map condition = new HashMap();
		condition.put("sucstatus", DxConstants.DATA_STATUS_FAILE);
		condition.put("resendcount", DxConstants.RESENDCOUNT);
		daoUtils.execSql(sql, condition);
	}

	/**
	 * 批量将待发送表中的发送失败的短信删除
	 * 
	 * @author niutongda
	 * @Time 2017-4-12 下午01:55:03 
	 *
	 */
	private void batchDeleteDxErr() {
		String sql = " delete t_sms_stay where sucstatus =:sucstatus and resendcount >=:resendcount";
		Map condition = new HashMap();
		condition.put("sucstatus", DxConstants.DATA_STATUS_FAILE);
		condition.put("resendcount", DxConstants.RESENDCOUNT);
		daoUtils.execSql(sql, condition);
	}
	
	
}
