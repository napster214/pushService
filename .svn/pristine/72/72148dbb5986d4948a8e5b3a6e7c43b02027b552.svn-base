package com.jadlsoft.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.jadlsoft.business.DxfsManager;

public class DxfsWebserviceClient { 
	private DxfsManager  dxfsManager;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test2();
	}
//	public String Test1(){
//		String a=dxfsManager.GetNote("ddd");
//		return a;
//	}
	
	private static void test2() { 		
		
		
		String str="";
		String url = "http://192.168.20.21:8080/dxfs/services/dxfsWebservice";//          
		Service service = new Service();
	    Call call;
    	try {	    		
		    call = (Call) service.createCall();
		    call.setTargetEndpointAddress(new java.net.URL(url) );
		    call.setOperationName("Dxfs");
		    
		
			call.addParameter("note", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("phone", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING); 
		
			str=(String)call.invoke(new Object[]{"【京安丹灵】感谢您使用个人安全中心，验证码为816859，请及时输入，并勿泄露给他人。","18600755757"});
			
		    //dh,dxnr,jssj,fssj,bm,yy
//		    String res =  (String) call.invoke( new Object[] {"15910647536","żȻ","2013-09-23","2013-09-23","1","1"} );
		    //String res =  (String) call.invoke( new Object[] {"","","","","1","1"} );
		    System.out.println(str);
		  
	   } catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   } catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   } catch (ServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }
	   System.out.print("str:"+str);
	 }  
}
