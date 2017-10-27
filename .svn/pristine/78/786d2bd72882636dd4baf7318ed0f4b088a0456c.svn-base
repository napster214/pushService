package com.jadlsoft.webservice;

import com.jadlsoft.business.DxfsManager;
import com.jadlsoft.utils.SpringBeanFactory;
/**
 * 
 * 短信接收接口
 * @author wujiaxu
 * @Time 2017-10-13 下午3:57:32
 *
 */
public class DxfsWebservice {

	private DxfsManager dxfsManager = (DxfsManager) SpringBeanFactory.getBean("dxfsManager");
   
    
    /**
     * 短信信息接收
     * @param content
     * @param mobile
     * @return
     * @author wujiaxu
     * @Time 2017-10-13 下午3:57:57
     */
    public String Dxfs(String content,String mobile){
    	//1、判断非法请求
    	if(content == null || "".equals(content) || mobile == null || "".equals(mobile)){
    		return "-1";
    	}
    
    	//2、加上短信头
    	if(content.indexOf("【") == -1 ){
    		content = "【京安丹灵】"+content;
    	}
    		
    	//3、保存短信业务
    	return dxfsManager.saveDx(content, mobile)+"";
    	
    }


	public DxfsManager getDxfsManager() {
		return dxfsManager;
	}


	public void setDxfsManager(DxfsManager dxfsManager) {
		this.dxfsManager = dxfsManager;
	}
 
}
