package com.flueky.library.album;

/**
 * @file Framework:com.flueky.library.album.AlbumCoverVo.java
 * @author flueky zuokefei0217@163.com
 * @time 2017年2月27日 下午9:17:57
 */
public class AlbumCoverVo {
	private String coverName;
	private int albumSize;
	private String coverPath;
	
	/**
	 * @param coverName
	 * @param albumSize
	 * @param coverPath
	 */
	public AlbumCoverVo(String coverName, int albumSize, String coverPath) {
		super();
		this.coverName = coverName;
		this.albumSize = albumSize;
		this.coverPath = coverPath;
	}

	/**
	 * @return the coverName
	 */
	public String getCoverName() {
		return coverName;
	}

	/**
	 * @return the albumSize
	 */
	public int getAlbumSize() {
		return albumSize;
	}

	/**
	 * @return the coverPath
	 */
	public String getCoverPath() {
		return coverPath;
	}
	
	
}
