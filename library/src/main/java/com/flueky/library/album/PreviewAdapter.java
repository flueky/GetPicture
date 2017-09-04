package com.flueky.library.album;

import java.io.File;
import java.util.List;

import com.flueky.library.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author flueky zuokefei0217@163.com
 * @file Framework:com.flueky.library.album.PreviewAdapter.java
 * @time 2017年2月28日 上午9:20:53
 */
public class PreviewAdapter extends PagerAdapter {

    private Activity activity;
    private List<String> pics;

    public PreviewAdapter(Activity activity) {
        super();
        this.activity = activity;
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewAdapter.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月28日 上午9:20:53
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (pics == null)
            return 0;
        return pics.size();
    }

    /**
     * @param pics the pics to set
     */
    public void setPics(List<String> pics) {
        this.pics = pics;
        notifyDataSetChanged();
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewAdapter.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月28日 上午9:20:53
     * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View,
     * java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewAdapter.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月28日 下午12:14:01
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.View,
     * int)
     */
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ImageView imageView = new ImageView(activity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        Picasso.with(activity).load(new File(pics.get(arg1))).placeholder(R.drawable.drawable_none)
                .into(imageView);
        ((ViewPager) arg0).addView(imageView);
        return imageView;
    }

    // 销毁arg1位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
}
