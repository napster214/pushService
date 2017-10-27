package com.jadlsoft.utils.httpRequestProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;


public class HttpUtils {

	/** 
     * 发送https请求 
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return String 
     */  
    public static String doRequest(String requestUrl,  
                                          String requestMethod,  
                                          String outputStr) {
    	String result = "";
        try {  
            URL url = new URL(requestUrl);  
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            conn.setRequestMethod(requestMethod);  
            // 当outputStr不为null时向输出流写数据  
            if (null != outputStr) {  
                OutputStream outputStream = conn.getOutputStream();  
                // 注意编码格式  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
            // 从输入流读取返回内容  
            InputStream inputStream = conn.getInputStream();  
            InputStreamReader inputStreamReader =  
                new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader =  
                new BufferedReader(inputStreamReader);  
            String str = null;  
            StringBuffer buffer = new StringBuffer();  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            // 释放资源  
            bufferedReader.close();  
            inputStreamReader.close();  
            inputStream.close();  
            inputStream = null;  
            conn.disconnect();  
            result = buffer.toString();
        } catch (ConnectException ce) {  
//            MyLog.writelogfile("连接超时：{}", ce);  
        } catch (Exception e) {  
//            MyLog.writelogfile("https请求异常：{}", e);  
        }  
        return result;  
    }  
    
    public static String asyncHttpRequest(String url, FutureCallback<HttpResponse> callback) throws Exception {
		
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
		final CountDownLatch latch = new CountDownLatch(1);
        final HttpGet request = new HttpGet("https://www.alipay.com/");
        System.out.println(" caller thread id is : " + Thread.currentThread().getId());
        httpclient.execute(request, callback);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            httpclient.close();
        } catch (IOException ignore) {

        }
        return null;
	}
}
