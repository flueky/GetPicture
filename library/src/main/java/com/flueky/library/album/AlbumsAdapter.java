package com.flueky.library.album;

import java.io.File;
import java.util.ArrayList;

import com.flueky.library.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @file Framework:com.flueky.library.album.AlbumsAdapter.java
 * @author flueky zuokefei0217@163.com
 * @time 2017年2月27日 下午9:23:54
 */
public class AlbumsAdapter extends BaseAdapter {
	private Context mContext;

	private ArrayList<AlbumCoverVo> covers;

	/**
	 * @param mContext
	 */
	public AlbumsAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * @file Framework:com.flueky.library.album.AlbumsAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:23:54
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (covers == null)
			return 0;
		return covers.size();
	}

	/**
	 * @file Framework:com.flueky.library.album.AlbumsAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:23:54
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return covers.get(position);
	}

	/**
	 * @file Framework:com.flueky.library.album.AlbumsAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:23:54
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData(ArrayList<AlbumCoverVo> covers){
		this.covers = covers;
		notifyDataSetChanged();
	}

	/**
	 * @file Framework:com.flueky.library.album.AlbumsAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午9:23:54
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.album_item, null);
			holder = new Holder();
			holder.ivCover = (ImageView) convertView.findViewById(R.id.album_item_iv_cover);
			holder.txtName = (TextView) convertView.findViewById(R.id.album_item_txt_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Picasso.with(mContext).load(new File(covers.get(position).getCoverPath())).placeholder(R.drawable.drawable_none)
				.resize(300, 300).centerCrop().into(holder.ivCover);
		holder.txtName.setText(
				String.format("%s(%d)", covers.get(position).getCoverName(), covers.get(position).getAlbumSize()));
		return convertView;
	}

	class Holder {
		ImageView ivCover;
		TextView txtName;
	}

}
