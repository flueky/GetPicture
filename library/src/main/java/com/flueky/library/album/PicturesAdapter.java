package com.flueky.library.album;

import java.io.File;
import java.util.ArrayList;

import com.flueky.library.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * @file Framework:com.flueky.library.album.PicturesAdapter.java
 * @author flueky zuokefei0217@163.com
 * @time 2017年2月27日 下午3:46:45
 */
public class PicturesAdapter extends BaseAdapter {

	private ArrayList<String> pics;
	private Context mContext;
	private ArrayList<String> choose;
	private int max = 0;

	/**
	 * @param mContext
	 */
	public PicturesAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void setData(ArrayList<String> pics) {
		this.pics = pics;
		notifyDataSetChanged();
	}

	public ArrayList<String> getData() {
		return pics;
	}

	/**
	 * @return the choose
	 */
	public ArrayList<String> getChoose() {
		return choose;
	}

	/**
	 * @param choose
	 *            the choose to set
	 */
	public void setChoose(ArrayList<String> choose) {
		this.choose = choose;
		notifyDataSetChanged();
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @file Framework:com.flueky.library.album.PicturesAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午3:46:45
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (pics == null)
			return 0;
		return pics.size();
	}

	/**
	 * @file Framework:com.flueky.library.album.PicturesAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午3:46:45
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return pics.get(position);
	}

	/**
	 * @file Framework:com.flueky.library.album.PicturesAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午3:46:45
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @file Framework:com.flueky.library.album.PicturesAdapter.java
	 * @author flueky zuokefei0217@163.com
	 * @time 2017年2月27日 下午3:46:45
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.pics_item, null);
			holder = new Holder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.pics_item_iv_pic);
			holder.cbChoose = (CheckBox) convertView.findViewById(R.id.pics_item_cb_choose);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		int flag = -1;
		for (int i = 0; choose != null && i < choose.size(); i++) {
			if (pics.get(position).equals(choose.get(i))) {
				flag = i;
			}
		}
		if (flag < 0) {
			holder.cbChoose.setChecked(false);
			holder.cbChoose.setText("");
		} else {
			holder.cbChoose.setChecked(true);
			holder.cbChoose.setText(flag + 1 + "");
		}
		Picasso.with(mContext).load(new File(pics.get(position))).placeholder(R.drawable.drawable_none).resize(200, 200)
				.centerCrop().into(holder.ivPic);
		holder.ivPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PreviewActivity.class);
				intent.putExtra("index", position);
				intent.putStringArrayListExtra("pics", pics);
				intent.putStringArrayListExtra("choose", choose);
				intent.putExtra("max", max);
				if (mContext instanceof Activity)
					((Activity) mContext).startActivityForResult(intent, PictureListActivity.REQUEST_CODE_PREVIEW);
			}
		});
		holder.cbChoose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean flag = false;
				for (int i = 0; choose != null && i < choose.size(); i++) {
					if (pics.get(position).equals(choose.get(i))) {
						choose.remove(i);
						flag = true;
					}
				}

				if (!flag && (choose.size() < max || max < 0))
					choose.add(pics.get(position));
				if (max > 0 && choose.size() > max) {
					((CheckBox) v).setChecked(false);
				}
				if (mContext instanceof PictureListActivity) {
					((PictureListActivity) mContext).updateBottom();
				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class Holder {
		ImageView ivPic;
		CheckBox cbChoose;
	}

}
