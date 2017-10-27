package com.jadlsoft.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


/**
 * @Title: FileUtils.java
 * @Description:
 * @Copyright: Copyright (c) 2009
 * @Company: 安丹灵
 * @date 2009-9-8
 * @author zhaohuibin
 * @version 3.0
 */
public class FileUtils {
	private static Logger logger = Logger.getLogger(FileUtils.class);

	/**
	 * @函数名：writeFile
	 * @功能：写文件
	 * @param filePath
	 *            包含文件名的文件路径
	 * @param content
	 *            文件内容
	 * @param encode
	 *            编码格式 void
	 */

	public static void writeFile(String filePath, String content, String encode) {
		try {
			FileOutputStream o = new FileOutputStream(filePath);
			o.write(content.getBytes(encode));
			o.close();
		} catch (FileNotFoundException fne) {
			logger.error("未找到相关文件！FileUtils.writeFile", fne);
		} catch (IOException ioe) {
			logger.error("写文件内容异常！FileUtils.writeFile", ioe);
		}
	}
	/**
     *  新建目录
     *  @param  folderPath  String  如  c:/test
     *  @return  boolean
     */
    public static void newFolder(String folderPath) {
        try {
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.mkdir();      
            }
        } catch (Exception e) {
            logger.error("错误",e);
        }
    }
	/**
	 * @函数名：addFileContent
	 * @功能：给文件追加数据
	 * @param context
	 *            追加内容
	 * @param fileName
	 *            要追加内容的文件 void
	 */
	public static void addFileContent(String context, String fileName) {
		RandomAccessFile rfile = null;
		try {
			File file = new File(fileName);
			// 追加文件内容
			rfile = new RandomAccessFile(file, "rw");
			rfile.seek(rfile.length());
			rfile.write(context.getBytes());
			rfile.write(lineSeparator.getBytes());
		} catch (Exception e) {
			logger.error("给文件追加数据异常！FileUtils.addFileContent", e);
		} finally {
			if(rfile != null){
				try {
					rfile.close();
				} catch (IOException e) {
					logger.error("错误",e);
				}
			}
		}
	}
	private static String lineSeparator = (String) java.security.AccessController.doPrivileged(  
			new sun.security.action.GetPropertyAction("line.separator"));
	
	public static void addFileContent(byte[] context, String fileName) {
		RandomAccessFile rfile = null;
		try {
			File file = new File(fileName);
			// 追加文件内容
			rfile = new RandomAccessFile(file, "rw");
			rfile.seek(rfile.length());
			rfile.write(context);
			rfile.write(lineSeparator.getBytes());
		} catch (Exception e) {
			logger.error("给文件追加数据异常！FileUtils.addFileContent", e);
		} finally {
			if(rfile != null){
				try {
					rfile.close();
				} catch (IOException e) {
					logger.error("错误",e);
				}
			}
		}
	}

	/**
	 * @函数名：writeFile
	 * @功能：将二进制数据写入指定文件
	 * @param filePath
	 *            包含文件名的文件路径
	 * @param bytes
	 *            二进制数据 void
	 */
	public static void writeFile(String filePath, byte[] bytes) {
		File file = new File(filePath);
		OutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			bufferedOutputStream.write(bytes);
			bufferedOutputStream.flush();
		} catch (FileNotFoundException e) {
			logger.error("未找到相关二进制文件！FileUtils.writeFile", e);
		} catch (IOException e) {
			logger.error("写二进制文件异常！FileUtils.writeFile", e);
		} finally {
			try {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				logger.error("错误",e);
			}
		}
	}

	/**
	 * @函数名：moveFile
	 * @功能：移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/test.txt
	 * @param newPath
	 *            String 如：d:/test.txt
	 * @param type
	 *            String 移动类型，create新建(改变文件创建时间)、move移动(不改变文件创建时间)
	 */
	public static void moveFile(String oldPath, String newPath, String type) {
		if (type.equalsIgnoreCase("create")) {
			copyFile(oldPath, newPath);
			delFile(oldPath);
		} else if (type.equalsIgnoreCase("move")) {
			File tmp = new File(oldPath);
			int num = 0;
			File newFile = new File(newPath);
			File newFileBak = new File(newPath);
			while (newFileBak.exists() && newFileBak.isFile()) {
				newFileBak = new File(newPath + ++num);
			}
			newFile.renameTo(newFileBak);
			tmp.renameTo(newFile);
		}
	}

	/**
	 * @函数名：copyFile
	 * @功能：复制单个文件
	 * @param oldPath
	 *            String 原文件路径 如：c:/test.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/test.txt void
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			logger.error("复制单个文件操作出错！FileUtils.copyFile", e);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/test.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			logger.error("删除文件操作出错！FileUtils.copyFile", e);
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/test
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				System.gc();
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]); // 先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]); // 再删除空文件夹
			}
		}
	}
	
	 /**
     *  判断指定目录的文件是否存在
     *  @param  filePath  String  如：c:/test.txt
     */
    public static boolean isFileExists(String filePath) {
    	try {
        	File file = new File(filePath);
            if (file.exists())
            	return true;
            else
            	return false;			
		} catch (Exception e) {
			logger.error("错误",e);
			return false;
		}
    }
	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/test
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			logger.error("删除文件夹操作出错！FileUtils.copyFile", e);
		}
	}

	/**
	 * @函数名：readXMLFile
	 * @功能：读取XML文件
	 * @param filePath
	 *            包含文件名的文件路径
	 * @param columns
	 *            需解析的字段名数组
	 * @return List
	 */
	public static List readXMLFile(String xmlString, String[] columns) {
		List list = new ArrayList();
		try {
			Document doc = DocumentHelper.parseText(xmlString);
			Element root = doc.getRootElement();
			List listTemp = root.elements();
			for (int i = 0; i < listTemp.size(); i++) {
				Element item = (Element) listTemp.get(i);
				Map map = new HashMap();
				for (int j = 0; j < columns.length; j++) {
					String column = columns[j];
					map.put(column, item.element(column).getData());
				}

				list.add(map);
			}

			return list;
		} catch (Exception e) {
			logger.error("解析XML文件异常！", e);
		}

		return list;
	}

	/**
	 * @函数名：writeXML
	 * @功能：写XML文件
	 * @param docRes
	 *            XML文件对象
	 * @param fileName
	 *            包含文件名的文件路径 void
	 */
	public static void writeXML(Document docRes, String fileName) {
		FileOutputStream out = null;
		XMLWriter writer = null;
		try {
			if (docRes != null) {
				out = new FileOutputStream(fileName);

				OutputFormat of = new OutputFormat();
				of.setEncoding("GB2312");
				of.setIndent(true);
				of.setNewlines(true);

				writer = new XMLWriter(out, of);
				writer.write(docRes);

				writer.flush();
				out.flush();
			}
		} catch (Exception e) {
			logger.error("FileUtils:writeXML 写XML文件错误！", e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("错误",e);
			}
		}
	}

	/**
	 * @功能：
	 * @参数：
	 * @param doc
	 * @param childName
	 * @param keys
	 * @param values
	 * @return
	 * @返回值：Element
	 * create by zhaohuibin 2010-9-27 下午04:17:36
	 */
	public static org.w3c.dom.Element createChild(org.w3c.dom.Document doc,
			String childName, String keys, String values) {
		Map map = new HashMap();
		String[] keysArray = keys.split("@@");
		String[] valuesArray = values.split("@@");

		if (keysArray.length != valuesArray.length) {
			throw new RuntimeException("参数错误，key与value值没有一一对应！");
		}

		for (int i = 0; i < keysArray.length; i++) {
			map.put(keysArray[i], valuesArray[i]);
		}

		return DocumentUtils.createElement(doc, childName, keysArray, map);
	}

	/**
	 * @功能：将Document对象转换为xml文件
	 * @参数：
	 * @param document
	 * @param lspath
	 * @param path
	 * @param fileName
	 * @return
	 * @返回值：void
	 * create by zhaohuibin 2010-9-27 下午03:55:28
	 */
	public static void transDocToXMLFile(org.w3c.dom.Document document,
			String lspath, String path, String fileName) {
		try {
			String xmlString = toXMLString(document);
			saveXMLFile(xmlString, lspath, path, fileName);
		} catch (TransformerException e) {
			logger.error("转换XML文件错误！", e);
		} catch (IOException e) {
			logger.error("关闭文件流错误！", e);
		}
	}

	/**
	 * 将XML转换成字符串形式
	 * 
	 * @param document
	 *            Document
	 * @return
	 * @throws TransformerException
	 * @throws IOException
	 */
	private static String toXMLString(org.w3c.dom.Document document)
			throws TransformerException, IOException {
		/*
		 * 创建一个DOM转换器
		 */
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		/*
		 * 设置输出属性 encoding = "GB2312" 代表 输出的编码格式为 GB2312 indent = "yes" 代表缩进输出
		 */
		transformer.setOutputProperty("encoding", "GB2312");
		transformer.setOutputProperty("indent", "yes");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// transformer.transform()方法 将 XML Source转换为 Result
		String result = "";
		transformer.transform(new DOMSource(document), new StreamResult(
				outputStream));
		result = outputStream.toString();
		outputStream.close();// 关闭文件输出流
		return result;
	}

	private static void saveXMLFile(String fileContent, String lspath,
			String path, String fileName) {
		File file = new File(lspath);
		if (!file.isDirectory() || !file.exists()) {
			if (!mkdir(lspath)) {
				logger.error("生成临时文件夹失败！");
			}
		}

		file = new File(path);
		if (!file.isDirectory() || !file.exists()) {
			if (!mkdir(path)) {
				logger.error("生成文件夹失败！");
			}
		}

		String lsFileName = lspath + File.separator + fileName;
		String copyFileName = path + File.separator + fileName;
		logger.info("生成文件开始！");
		byte[] content = fileContent.getBytes();
		FileOutputStream fw = null;
		try {
			fw = new FileOutputStream(lsFileName);
			fw.write(content);
			fw.flush();
		} catch (IOException e) {
			logger.error("文件输出异常！", e);
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件输出流异常！", e);
			}
		}
		new File(lsFileName).renameTo(new File(copyFileName));
		logger.info("生成文件结束并转移成功！");
	}

	/**
	 * 创建不存在的指定文件夹
	 * 
	 * @param src_url
	 *            文件夹路径
	 */
	public static boolean mkdir(String src_url) {
		String[] urls = src_url.split("/");
		String pre = "";
		StringBuffer preUrl = new StringBuffer();
		for (int i = 0; i < urls.length; i++) {
			preUrl.append(pre).append(urls[i]);
			File file = new File(preUrl.toString());
			if (!file.isDirectory()) {
				if (!file.mkdir()) {
					return false;
				}
			}
			if (i == 0) {
				pre = "/";
			}
		}
		return true;
	}

	/**
	 * @函数名：createYearMonthDir
	 * @功能：创建年月目录
	 * @param dir
	 *            原始目录
	 * @return String
	 */
	public static String createYearMonthDir(String dir) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		String year = "" + cal.get(Calendar.YEAR);
		String month = "" + (1 + cal.get(Calendar.MONTH));
		File yearDir = new File(dir + File.separator + year);
		if (!yearDir.exists()) {
			yearDir.mkdir();
		}
		File monthDir = new File(dir + File.separator + year + File.separator
				+ month);
		if (!monthDir.exists()) {
			monthDir.mkdir();
		}
		return (dir + File.separator + year + File.separator + month);
	}
	
	/**
	 * @author 王庆 2013-8-22 下午05:05:59
	 * @功能：在普通java类不通过request获取当前部署项目名称
	 * */
	public static String getProjectName(){
		String url = FileUtils.class.getResource("/").toString();
		String[] str = url.split("/");
		return str[str.length-3];
		
	}
	
	/**
	 * 以行读取文件内容
	* @description:
	* @author shaoxinwei 
	* @date 2013-9-23 下午02:06:15 
	* @param fileName 包含路径的文件名称 如：F:/aa.txt
	* @return    
	* @return String
	 */
	public static String readFileContent(String fileName){
		File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbuffer = new StringBuffer();
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sbuffer.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            logger.error("错误",e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return sbuffer.toString();
	}
	
	/**
	 * 
	* @description:
	* @author shaoxinwei 
	* @date 2013-10-16 下午04:51:50 
	* @return     
	* @return byte[]
	 */
	public static byte[] readContentByte(String filePathAndfileName){
		InputStream is = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			is = new FileInputStream(filePathAndfileName);// pathStr 文件路径
			byte[] b = new byte[1024];
			int n;
			while ((n = is.read(b)) != -1) {
				out.write(b, 0, n);
			}
		} catch (Exception e) {
			logger.error("错误",e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("错误",e);
			}
		}

		return out.toByteArray();
	}
	
	/**
	 * 获取文件名，不带后缀
	 * @param file
	 * @return: String
	 */
	public static String getPureFilename(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		String name = file.getName();
		if (!name.contains(".")) {
			return name;
		}
		name = name.substring(0, name.lastIndexOf("."));
		return name;
	}
	
	/**
	 * 获取文件冲突的时候的下一个文件名
	 * @功能: 在保存文件时候，文件冲突之后，采取重命名的方式，在文件后面加上括号和数字来标识
	 *		如：第一次出现该文件时候命名为：test.xml
	 *		   那么第二次就将上传的文件命名为：test（1）.xml 	
	 *		   第三次就将上传的文件命名为：test（2）.xml
	 * @param destFile 文件
	 * @return: String
	 */
	public static String getNextFileName(File destFile) {
		
		if (destFile == null || !destFile.isFile()) {
			return null;
		}
		
		String currentFileName = getPureFilename(destFile);
		String suffix = "";
		if (destFile.getName().contains(".")) {
			suffix = destFile.getName().substring(destFile.getName().lastIndexOf("."), destFile.getName().length());
		}
		
		if (StringUtils.isBlank(currentFileName)) {
			return "";
		}
		
		//找到该目录中，所有该文件的信息
		
		List<File> sameFileList = new ArrayList<File>();
		
		File parentFile = destFile.getParentFile();
		File[] files = parentFile.listFiles();
		for (File file : files) {
			if (file.getName().startsWith(currentFileName)) {
				sameFileList.add(file);
			}
		}
		
		if (sameFileList.size() == 0) {
			//当前还没有，直接保存即可
			return destFile.getName();
		}
		
		//有的话就取当前最大值
		int max = 0;
		for (File file : sameFileList) {
			if (file.getName().equals(destFile.getName())) {
				//是第一个文件
				continue;
			}
			//取括号里面的值
			String puname = getPureFilename(file);
			String num = puname.substring(puname.lastIndexOf("（")+1, puname.length()-1);
			try {
				int numint = Integer.parseInt(num);
				if (numint > max) {
					max = numint;
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		int nextNum = max + 1;
		
		return currentFileName + "（"+nextNum+"）" + suffix;
		
	}
	
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\test.xml");
		String nextFileName = getNextFileName(file);
		System.out.println(nextFileName);
	}
}
