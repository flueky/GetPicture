package com.flueky.library.album;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @file Framework:com.flueky.library.album.PictureVo.java
 * @author flueky zuokefei0217@163.com
 * @time 2017年2月27日 上午11:34:57
 */
public class PictureVo implements Serializable {
	
	private String parentName;//相册文件夹名称
	private ArrayList<String> pics;//相册单个文件夹目录下图片集合
	
	/**
	 * @param parentName
	 */
	public PictureVo(String parentName,String path) {
		super();
		this.parentName = parentName;
		pics = new ArrayList<String>();
		pics.add(path);
	}
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @return the pics
	 */
	public ArrayList<String> getPics() {
		return pics;
	}
	
	public void add(String path){
		if(pics==null)
			pics = new ArrayList<String>();
		pics.add(path);
	}
	

}
