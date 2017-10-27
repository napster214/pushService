package com.jadlsoft.domain;

import java.io.File;

/**
 * 邮件附件类
 * @类名: AttachmentBean
 * @作者: lcx
 * @时间: 2017-7-24 下午3:08:20
 */
public class AttachmentBean {

	private String filepath;
	private String filename;
	
	public AttachmentBean() {
		
	}
	
	public AttachmentBean(String filepath) {
		this.filepath = filepath;
		File file = new File(filepath);
		if (file.isFile()) {
			this.filename = file.getName();
		}
	}
	
	public AttachmentBean(String filepath, String filename) {
		this.filepath = filepath;
		this.filename = filename;
	}
	
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
