package com.jadlsoft.utils.icutil;

import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.icutil.DealOCXParameter;


/**
 * 关于OCX的基本参数处理。以后只需修改此处即可
 * 
 * @author 张方俊  2006/10/19
 * 
 */
public class ICOcxCanShuChuLiUtils {
	/**
	 * 识别卡的字符串
	 */
	public static final String shiBieKa(){
		return jiaMi("@00=F0001");
	}
	/**
	 * IC卡的初始化
	 * @param UserName：用户名称
	 * @param CardNum：卡编号
	 * @param Unit：单位名称
	 * @param CreateTime：发卡日期
	 * @param DestroyTime：注销日期
	 * @return 写卡的字符串
	 */
	public final String icChuShiHua(String UserName, String CardNum, String Unit, String CreateTime, String DestroyTime){
		return jiaMi("@00=F0002@01="+UserName+"@02="+CardNum+"@03="+Unit+"@04="+CreateTime+"@05="+DestroyTime+"@06=0");		
	}
	/**
	 * IC卡重新初始化。主要用于修改有效期
	 * @param UserName
	 * @param Unit
	 * @param DestroyTime
	 * @return 重新初始化的字符串
	 */
	public static final String recreate(String UserName , String Unit , String  DestroyTime){
		return jiaMi("@00=F0003@01=" + UserName + "@02=" + Unit + "@03=" + DestroyTime);
	}
	/**
	 * 修改密码
	 * @param oldPw：旧密码
	 * @param newPw：新密码
	 * @param inputMode：密码输入方式。默认为PC
	 * @return 修改密码的字符串
	 */
	public final String changePassword(String oldPw, String newPw, String inputMode){
		return jiaMi("@00=F0004@01=" + oldPw + "@02=" + newPw + "@03=" + inputMode);
	}
	/**
	 * 重置密码
	 * @return 重置密码的字符串
	 */
	public final String resetPassword(){
		return jiaMi("@00=F0005");
	}	
	/**
	 * 读目录
	 * @return 读目录的字符串
	 */
	public static final String duMuLu(){
		return jiaMi("@00=F0006");
	}
	/**
	 * 根据目录位置读数据流
	 * @param position：数据流位置
	 * @return 读数据流的字符串
	 */
	public static final String readStreamByPosition(String position){
		return jiaMi("@00=F0007@01="+position);
	}
	/**
	 * 根据所给的信息获取数据流
	 * @param Modal：形码
	 * @param Type：型码
	 * @param State：流状态
	 * @return 读指定数据流的字符串
	 */
	public final String readStreamByNames(String Modal , String Type , String State){
		return jiaMi("@00=F0008@01="+Modal+"@02="+Type+"@03="+State);
	}
	/**
	 * 写流
	 * @param Modal：形码
	 * @param Type：型码
	 * @param StreamLen：所写流的长度
	 * @param StreamContent：流内容
	 * @return 写流的字符串
	 */
	public final String writeStream(String Modal, String Type, String StreamLen, String StreamContent){
		return jiaMi("@00=F0009@01="+Modal+"@02="+Type+"@03="+StreamLen+"@04="+StreamContent);
	}
	/**
	 * 根据状态写回执
	 * @param echoPosition：写回执流的位置
	 * @param zhuangtai：写回执流的状态
	 * @return 写回执的字符串
	 */
	public static final String writeEchoByNowTime(String echoPosition, String zhuangtai){
		zhuangtai += DateUtils.getCurrentData("yyyyMMddhhmm");
		return jiaMi("@00=F0010@01="+echoPosition+"@02="+zhuangtai);
	}
	/**
	 * 写回执
	 * @param echoPosition：写回执流的位置
	 * @param EchoContent：回执内容
	 * @return 写回执的字符串
	 */
	public final String writeEcho(String echoPosition, String EchoContent){
		return jiaMi("@00=F0010@01="+echoPosition+"@02="+EchoContent);
	}
	/**
	 * 读购买证
	 * @return 读购买证的字符串
	 */
	public static final String readGMZ(){
		return jiaMi("@00=F0011");
	}
	/**
	 * 读运输证
	 * @param TrafficSn：运输证的序号
	 * @return 读运输证的字符串
	 */
	public static final String readYSZ(String TrafficSn){
		return jiaMi("@00=F0012@01="+TrafficSn);
	}
	/**
	 * 读运输证总量
	 * @return 读运输证总量的字符串
	 */
	public static final String readYSZZongLiang(){
		return jiaMi("@00=F0013");
	}
	/**
	 * 写购买证
	 * @param OrderLevel1  购买证物品总量 1位
	 * @param OrderLevel2  暂无 1位
	 * @param OrderLevel3  暂无 1位
	 * @param OrderUnit1   开证单位代码	13位
	 * @param OrderUnit2   供货单位代码	13位
	 * @param OrderUnit3   购买单位代码	13位
	 * @param OrderName    开证人姓名	14位
	 * @param OrderNumber  购买证编号	15位
	 * @param OrderBDate   开始日期	8位
	 * @param OrderEDate   截至日期	8位
	 * @param OrderType1   购买证物品1	3位
	 * @param OrderValue1  购买证数量1	4位
	 * @param OrderType2   购买证物品2	3位
	 * @param OrderValue2  购买证数量2	4位
	 * @param OrderType3   购买证物品3	3位
	 * @param OrderValue3  购买证数量3	4位
	 * @param OrderType4   购买证物品4	3位
	 * @param OrderValue4  购买证数量4	4位
	 * @param OrderType5   购买证物品5	3位
	 * @param OrderValue5  购买证数量5	4位
	 * @param OrderState   购买证状态	1位	字符0，1，2
	 * @return
	 */
	public static final String writeGMZ(String OrderLevel1, String OrderLevel2, String OrderLevel3, String OrderUnit1, String OrderUnit2, String OrderUnit3, String OrderName, String OrderNumber, String OrderBDate, String OrderEDate, String OrderType1, String OrderValue1, String OrderType2, String OrderValue2, String OrderType3, String OrderValue3, String OrderType4, String OrderValue4, String OrderType5, String OrderValue5, String OrderState){
		return jiaMi("@00=F0014@01="+OrderLevel1+"@02="+OrderLevel2+"@03="+OrderLevel3+"@04="+OrderUnit1+"@05="+OrderUnit2+"@06="+OrderUnit3+"@07="+OrderName+"@08="+OrderNumber+"@09="+OrderBDate+"@10="+OrderEDate+"@11="+OrderType1+"@12="+OrderValue1+"@13="+OrderType2+"@14="+OrderValue2+"@15="+OrderType3+"@16="+OrderValue3+"@17="+OrderType4+"@18="+OrderValue4+"@19="+OrderType5+"@20="+OrderValue5+"@21="+OrderState);		
	}
	/**
	 * 写运输证
	 * @param TrafficStatus   运输证状态	1位
	 * @param OrderLevel1     运输证物品总量	1位
	 * @param OrderLevel2     暂无	1位
	 * @param OrderLevel3	  暂无	1位
	 * @param OrderUnit1	  开证单位代码	13位
	 * @param OrderUnit2	  供货单位代码	13位
	 * @param OrderUnit3      购买单位代码	13位
	 * @param OrderName       开证人姓名	14位
	 * @param OrderNumber	  运输证编号	15位
	 * @param OrderBDate	  开始日期	8位
	 * @param OrderEDate	  截至日期	8位	
	 * @param OrderType1	  运输证类型一	3位
	 * @param OrderValue1     运输证数量一	4位
	 * @param TrafficUnit1	  承运单位一	13位
	 * @param OrderType2	  运输证类型二	3位
	 * @param OrderValue2	  运输证数量二	4位
	 * @param TrafficUnit2    承运单位二	13位
	 * @param OrderType3	  运输证类型三	3位
	 * @param OrderValue3	  运输证数量三	4位
	 * @param TrafficUnit3	  承运单位三	13位
	 * @param OrderType4	  运输证类型四	3位
	 * @param OrderValue4	  运输证数量四	4位
	 * @param TrafficUnit4	  承运单位四	13位
	 * @param OrderType5	  运输证类型五	3位
	 * @param OrderValue5	  运输证数量五	4位
	 * @param TrafficUnit5	  承运单位五	13位
	 * @param TrafficSn		  运输证序列号	1位	0-4
	 * @return
	 */
	public static final String writeYSZ(String TrafficStatus, String OrderLevel1, String OrderLevel2, String OrderLevel3, String OrderUnit1 , String OrderUnit2 , String OrderUnit3, String OrderName , String OrderNumber , String OrderBDate , String OrderEDate , String OrderType1 , String OrderValue1 , String TrafficUnit1, String OrderType2 , String OrderValue2 , String TrafficUnit2 , String OrderType3 , String OrderValue3 , String TrafficUnit3 , String OrderType4 , String OrderValue4 , String TrafficUnit4 , String OrderType5 , String OrderValue5 , String TrafficUnit5, String TrafficSn){
		return jiaMi("@00=F0015@01="+TrafficStatus+"@02="+OrderLevel1+"@03="+OrderLevel2+"@04="+OrderLevel3+"@05="+OrderUnit1+"@06="+OrderUnit2+"@07="+OrderUnit3+"@08="+OrderName+"@09="+OrderNumber+"@10="+OrderBDate+"@11="+OrderEDate+"@12="+OrderType1+"@13="+OrderValue1+"@14="+TrafficUnit1+"@15="+OrderType2+"@16="+OrderValue2+"@17="+TrafficUnit2+"@18="+OrderType3+"@19="+OrderValue3+"@20="+TrafficUnit3+"@21="+OrderType4+"@22="+OrderValue4+"@23="+TrafficUnit4+"@24="+OrderType5+"@25="+OrderValue5+"@26="+TrafficUnit5+"@27="+TrafficSn);
	}
	/**
	 * 写运输证总量
	 * @param TrafficTotal：运输证总量
	 * @return 写运输证总量的字符串
	 */
	public static final String writeYSZZongLiang(String TrafficTotal){
		return jiaMi("@00=F0016@01="+TrafficTotal);
	}
	/**
	 * IC卡控件整理
	 * @return
	 */
	public final String icKongJianZhengLi(){
		return jiaMi("@00=F0017");
	}
	/**
	 * 检查特定数据流
	 * @param Modal：形码
	 * @param Type：型码
	 * @param State：状态
	 * @return 检查特定数据流的字符串
	 */
	public static final String checkSpecialStream(String Modal, String Type, String State){
		return jiaMi("@00=F0018@01="+Modal+"@02="+Type+"@03="+State);
	}
	/**
	 * 张方俊 2006/11/6增加
	 * 检查IC卡的使用情况
	 * @return 检查IC卡的使用情况的OCX字符串
	 */
	public static final String checkSpaceInfo(){
		return jiaMi("@00=F0023");
	}
	/**
	 * 写2201数据流
	 * @param stream：2201数据流内容
	 * @return 写卡的字符串
	 */
	public static final String write2201Stream(String stream){
		return jiaMi("@00=F0009@01=2@02=201@03="+stream.getBytes().length+"@04="+stream);
	}
	/**
	 * 写2203数据流
	 * @param stream：2201数据流内容
	 * @return 写卡的字符串
	 */
	public static final String write2203Stream(){
		String now = DateUtils.getCurrentData("yyyyMMddHHmm");		
		return jiaMi("@00=F0009@01=2@02=203@03="+(now.length()+1)+"@04=0"+now);
	}
	/**
	 * 2007/11/8增加
	 * IC卡登录
	 * @return IC卡登录的OCX字符串
	 */
	public static final String icLogin(){
		return jiaMi("@00=F0024@01=B@02=123456");
	}
	/**
	 * 李洪磊 2008-08-25 16：58 添加
	 * IC卡登陆
	 * @param password
	 * @return
	 */
	public static final String icLogin(String password){
		return jiaMi("@00=F0024@01=A@02=" + password);
	}
	/**
	 * 加密OCX参数
	 * @param ocx_para：待加密的参数
	 * @return 经过3DES，BASE64编码过后的参数
	 */
	public static final String jiaMi(String ocx_para){
		return DealOCXParameter.dealParameter(ocx_para);
	}	
}
