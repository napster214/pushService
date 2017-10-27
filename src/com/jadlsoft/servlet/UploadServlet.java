package com.jadlsoft.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jadlsoft.constants.SystemConstants;
import com.jadlsoft.utils.FileUploadUtils;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.ResultBean;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SysProperUtils;

/**
 * 对外提供的http文件上传的接口
 * @类名: UploadServlet
 * @描述: 提供永久保存上传方法以及临时上传方法
 * 			临时上传会在完成操作之后将文件删除
 * @作者: lcx
 * @时间: 2017-8-7 下午6:04:39
 */
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if (StringUtils.isBlank(methodName)) {
			//方法名为空
			try {
				ResponseUtils.render(response, "该地址无效！请详细查看接口信息！");
				return ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//反射调用，执行方法
		Class clazz = this.getClass();
		try {
			Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, request, response);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			//没有找到对应的方法
			try {
				ResponseUtils.render(response, "该方法无效！请详细查看接口信息！");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (IllegalArgumentException e) {
			try {
				ResponseUtils.render(response, "上传失败！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			try {
				ResponseUtils.render(response, "上传失败！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (InvocationTargetException e) {
			try {
				ResponseUtils.render(response, "上传失败！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 对外提供的上传文件的方法
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @return: void
	 */
	protected void upload(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO 
	}
	
	/**
	 * 对外提供的上传临时文件的方法
	 * @param req
	 * @param resp
	 * @return: void
	 * @throws Exception 
	 */
	public void uploadAsTemp(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Map resultMap = new HashMap();
		
		//1、获取临时文件上传目录
		String destDir = SysProperUtils.getProperty("upload.file.temp.dir");
		//2、调用文件上传工具类完成文件的上传
		
		List<String> pathList = FileUploadUtils.dealUpload(request, destDir);
		if (StringUtils.isNullOrEmpty(pathList)) {
			//上传失败
			resultMap.put("statusCode", SystemConstants.RESULT_FAIL);
			resultMap.put("errmsg", "文件上传失败！");
			ResponseUtils.render(response, JsonUtil.map2json(resultMap));
			return ;
		}

		//上传成功
		resultMap.put("statusCode", SystemConstants.RESULT_SUCCESS);
		resultMap.put("uploadType", SystemConstants.UPLOADTYPE_TEMP);
		resultMap.put("filepathList", pathList);
		ResponseUtils.render(response, JsonUtil.map2json(resultMap));
		return;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		FileUploadUtils.dealUpload(request, SysConfigUtils.getProperty("upload.file.temp.dir"), "");
		return;
	}
}
