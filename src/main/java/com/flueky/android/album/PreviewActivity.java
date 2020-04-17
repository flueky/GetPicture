package com.flueky.android.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.flueky.android.basic.activity.FFBasicActivity;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author flueky zuokefei0217@163.com
 * @file Framework:com.flueky.library.album.PreviewActivity.java
 * @time 2017年2月27日 下午10:15:09
 */
public class PreviewActivity extends FFBasicActivity
        implements OnPageChangeListener, OnClickListener, OnCheckedChangeListener {
    private ViewPager vpContent;
    private PreviewAdapter previewAdapter;
    private TextView txtTitle;
    private CheckBox cbChoose;
    private int currentIndex = 0;
    private List<String> pics;
    private ArrayList<String> choose;
    private int size = 0;
    private int maxCount;

    private Button btnPhoto;

    private View vTitle;


    @Override
    protected int contentLayout() {
        return R.layout.preview;
    }


    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月27日 下午10:15:09
     */
    protected void initView() {
        vpContent = (ViewPager) findViewById(R.id.preview_vp_content);
        txtTitle = (TextView) findViewById(R.id.preview_tv_title);
        cbChoose = (CheckBox) findViewById(R.id.preview_cb_choose);
        btnPhoto = (Button) findViewById(R.id.preview_btn_photo);
        vTitle = findViewById(R.id.preview_title);

        ImmersionBar.with(this).titleBar(vTitle).init();


        vpContent.setOnPageChangeListener(this);
        btnPhoto.setOnClickListener(this);
        cbChoose.setOnCheckedChangeListener(this);
        cbChoose.setOnClickListener(this);
    }


    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月27日 下午10:15:09
     */
    protected void initData() {
        previewAdapter = new PreviewAdapter(this);
        vpContent.setAdapter(previewAdapter);
        if (getIntent() == null) {
            return;
        }

        maxCount = getIntent().getIntExtra("max", -1);
        currentIndex = getIntent().getIntExtra("index", 0);
        pics = getIntent().getStringArrayListExtra("pics");
        if (pics == null) {
            LocalPicturesManager picturesManager = LocalPicturesManager.getInstance();
            picturesManager.loadPictures(this);
            pics = picturesManager.getRecentPics();
        }
        choose = getIntent().getStringArrayListExtra("choose");
        previewAdapter.setPics(pics);
        size = pics.size();
        vpContent.setCurrentItem(currentIndex);
        txtTitle.setText((currentIndex + 1) + "/" + size);
        updateCheckState();

    }

    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月28日 下午12:02:16
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月28日 下午12:02:16
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int,
     * float, int)
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月28日 下午12:02:16
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int arg0) {
        currentIndex = arg0;
        txtTitle.setText((currentIndex + 1) + "/" + size);
        updateCheckState();
    }

    public void updateCheckState() {
        int index = checkIndex();
        if (index < 0) {
            cbChoose.setChecked(false);
            cbChoose.setText("");
        } else {
            cbChoose.setChecked(true);
            cbChoose.setText(index + 1 + "");
        }
    }

    private int checkIndex() {
        String path = null;
        if (pics.size() == 0)
            return -1;
        path = pics.get(currentIndex);
        for (int i = 0; i < choose.size(); i++)
            if (choose.get(i).equals(path))
                return i;
        return -1;
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年3月8日 下午5:05:37
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preview_btn_photo) {
            Intent data = getIntent();
            data.putStringArrayListExtra("choose", choose);
            setResult(RESULT_OK, data);
            finish();
        } else if (v.getId() == R.id.preview_cb_choose) {
            boolean isChecked = cbChoose.isChecked();
            if (isChecked) {// 选中
                if (maxCount > 0 && choose.size() >= maxCount) {
                    ((CheckBox) v).setChecked(false);
                    return;
                }
                choose.add(pics.get(currentIndex));
                updateCheckState();
            } else {// 取消选中
                choose.remove(pics.get(currentIndex));
                updateCheckState();
            }
        }
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年3月8日 下午5:36:04
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent data = getIntent();
        data.putStringArrayListExtra("choose", choose);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * @file Framework:com.flueky.library.album.PreviewActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年3月8日 下午5:08:37
     * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
     * boolean)
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("TAG", "isChecked " + isChecked);

    }
}
