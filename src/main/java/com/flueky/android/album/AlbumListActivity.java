package com.flueky.android.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.flueky.android.basic.activity.FFBasicActivity;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

/**
 * @file Framework:com.flueky.library.album.AlbumListActivity.java
 * @author flueky zuokefei0217@163.com
 * @time 2017年2月27日 下午9:00:31
 */
public class AlbumListActivity extends FFBasicActivity implements OnItemClickListener, OnClickListener {

	private AlbumResult albumResult;
	private ArrayList<AlbumCoverVo> albumCover;// 封面
	private Button btnCancel;

	private LinearLayout llTitle;
	private ListView lvAlbums;
	private AlbumsAdapter albumAdapter;

	public static final int RESULT_CODE_ALBUM_CHOOSE = 0x124;


	@Override
	protected int contentLayout() {
		return R.layout.album_list;
	}


	/**
	 * @file Framework:com.flueky.library.album.AlbumListActivity.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:00:32
	 */
	protected void initView() {
		lvAlbums = (ListView) findViewById(R.id.album_list_lv_albums);
		btnCancel = (Button) findViewById(R.id.album_list_btn_cancel);
		llTitle = findViewById(R.id.album_list_ll_title);

		ImmersionBar.with(this).titleBar(llTitle).init();

		lvAlbums.setOnItemClickListener(this);
		btnCancel.setOnClickListener(this);
	}


	/**
	 * @file Framework:com.flueky.library.album.AlbumListActivity.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:00:32
	 */
	protected void initData() {
		if (getIntent() == null) {
			return;
		}
		albumResult = (AlbumResult) getIntent().getSerializableExtra("albums");
		if (albumResult == null)
			return;
		albumCover = new ArrayList<AlbumCoverVo>();
		albumCover.add(new AlbumCoverVo("所有照片", albumResult.getRecent().size(), albumResult.getRecent().get(0)));
		for (int i = 0; albumResult.getAlbums() != null && i < albumResult.getAlbums().size(); i++) {
			albumCover.add(new AlbumCoverVo(albumResult.getAlbums().get(i).getParentName(),
					albumResult.getAlbums().get(i).getPics().size(), albumResult.getAlbums().get(i).getPics().get(0)));
		}
		albumAdapter = new AlbumsAdapter(this);
		lvAlbums.setAdapter(albumAdapter);
		albumAdapter.setData(albumCover);
	}

	/**
	 * @file Framework:com.flueky.library.album.AlbumListActivity.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:37:46
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
	 *      android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent data = getIntent();
		data.putExtra("name", albumCover.get(position).getCoverName());
		if (position == 0) {
			data.putStringArrayListExtra("pics", albumResult.getRecent());
		} else {
			data.putStringArrayListExtra("pics", albumResult.getAlbums().get(position - 1).getPics());
		}
		setResult(RESULT_CODE_ALBUM_CHOOSE, data);
		finish();
	}

	/**
	 * @file Framework:com.flueky.library.album.AlbumListActivity.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:54:39
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.album_list_btn_cancel) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

}
