package com.jadlsoft.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class FileUploadUtils {

	private static Logger log = Logger.getLogger(FileUploadUtils.class);
	
	public static List<String> dealUpload(HttpServletRequest request, String destPath) {  
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");  
		
		List<String> pathList = new ArrayList<String>();
		
        File destDir = new File(destPath);
        if (!destDir.isDirectory()) {
			destDir.mkdirs();
		}
        File tmpDir = new File(destPath+"\\tmpDir"); //初始化上传文件的临时存放目录,必须是绝对路径  
        try {  
            if (ServletFileUpload.isMultipartContent(request)) {  
                DiskFileItemFactory factory = new DiskFileItemFactory();  
                //指定在内存中缓存数据大小,单位为byte,这里设为1Mb  
                factory.setSizeThreshold(1 * 1024 * 1024);   
                //设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录  
                factory.setRepository(tmpDir);   
                ServletFileUpload sfu = new ServletFileUpload(factory);  
                 // 指定单个上传文件的最大尺寸,单位:字节，这里设为5Mb  
                sfu.setFileSizeMax(5 * 1024 * 1024);  
                //指定一次上传多个文件的总尺寸,单位:字节，这里设为10Mb  
                sfu.setSizeMax(10 * 1024 * 1024);   
                sfu.setHeaderEncoding("UTF-8"); //设置编码，因为我的jsp页面的编码是utf-8的  
//                FileItemIterator fii = sfu.getItemIterator(request);// 解析request请求
                List<FileItem> parseRequest = sfu.parseRequest(request);
                if (parseRequest != null && parseRequest.size()>0) {
					for (FileItem fileItem : parseRequest) {
						InputStream is = fileItem.getInputStream();
						String filename = fileItem.getName();
						String path = saveFile(filename, destDir.getAbsolutePath(), is);
						pathList.add(path);
					}
				}
            }  
        } catch (Exception e) {  
        	log.error("【文件上传】文件上传失败！");
        	return null;
        }  
        return pathList;
    } 
	
	/**
	 * 保存文件
	 * @param filename
	 * @param destDir
	 * @param is
	 * @return: void
	 */
	public static String saveFile(String filename, String destDir, InputStream is) {
		
		if (StringUtils.isBlank(filename) || StringUtils.isBlank(destDir) || is == null) {
			return "";
		}
		
		File destFile = new File(destDir,filename);
		//如果目标文件已存在，就自动在后面加上数字
		if (destFile.exists()) {
			//获取下一个文件名
			String nextFileName = FileUtils.getNextFileName(destFile);
			destFile = new File(destDir, nextFileName);
		}
		
		
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(destFile));
			
			byte[] bytes = new byte[1024];
			int len;
			log.info("++++++++++++++++++++++++++开始接收文件上传，文件名："+filename+"+++++++++++++++++++++++++++++++++++");
			while((len=bis.read(bytes)) != -1) {
				bos.write(bytes, 0, len);
			}
			log.info("++++++++++++++++++++++++++文件上传结束，保存路径为："+destFile.getAbsolutePath()+"+++++++++++++++++++++++++++++++++++");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return destFile.getAbsolutePath();
	}
}
