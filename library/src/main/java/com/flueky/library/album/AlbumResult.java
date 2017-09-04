package com.flueky.library.album;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

/**
 * @file Framework:com.flueky.library.album.PictureResult.java
 * @author flueky zuokefei0217@163.com
 * @time 2017年2月27日 上午11:34:44
 */
public class AlbumResult implements Serializable {

	private ArrayList<PictureVo> albums;
	private ArrayList<String> recent;

	public AlbumResult() {
		super();
		this.albums = new ArrayList<PictureVo>();
		this.recent = new ArrayList<String>();
	}

	public void add(String path) {
		if (path == null)
			return;
		recent.add(0, path);
		File file = new File(path);
		String parentName = file.getParentFile().getName();
		for (int index = 0; index < albums.size(); index++) {
			if (albums.get(index).getParentName().equals(parentName)) {
				albums.get(index).getPics().add(0, path);
				return;
			}
		}
		albums.add(0, new PictureVo(parentName, path));
	}

	/**
	 * @return the recent
	 */
	public ArrayList<String> getRecent() {
		return recent;
	}

	/**
	 * @return the albums
	 */
	public ArrayList<PictureVo> getAlbums() {
		return albums;
	}

	public void print() {
		for (int i = 0; i < albums.size(); i++) {
			for (int j = 0; j < albums.get(i).getPics().size(); j++) {
				Log.d("TAG", albums.get(i).getPics().get(j));
			}
		}
	}
}
