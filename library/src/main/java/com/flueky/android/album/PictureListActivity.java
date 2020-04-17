package com.flueky.android.album;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flueky.android.basic.activity.FFBasicActivity;
import com.flueky.android.basic.permission.PermissionManager;
import com.flueky.android.basic.permission.PermissionRequestCallback;
import com.flueky.android.basic.permission.RequestPermission;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flueky zuokefei0217@163.com
 * @file Framework:com.flueky.library.album.PictureListActivity.java
 * @time 2017年2月23日 下午2:18:03
 */
@RequestPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
public class PictureListActivity extends FFBasicActivity implements OnClickListener, PermissionRequestCallback {
    private GridView gvPics;// 图片内容
    private TextView txtPreview, txtDone;
    private PicturesAdapter pictureAdaper;
    private Button btnAlbum, btnCancel;
    private AlbumResult albumResult;
    private TextView txtTitle;
    private ArrayList<String> choose;
    private int maxCount = 9;

    public static final int REQUEST_CODE_ALBUM = 0x123;
    public static final int REQUEST_CODE_PREVIEW = 0x223;

    private PermissionManager permissionManager;

    private LinearLayout llTitle;

    @Override
    protected int contentLayout() {
        return R.layout.picture_list;
    }


    /**
     * @file Framework:com.flueky.library.album.PictureListActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月23日 下午2:18:04
     */
    protected void initView() {

        llTitle = findViewById(R.id.picture_list_ll_title);

        gvPics = (GridView) findViewById(R.id.picture_list_gv_pics);
        btnAlbum = (Button) findViewById(R.id.picture_list_btn_album);
        btnCancel = (Button) findViewById(R.id.picture_list_btn_cancel);

        txtTitle = (TextView) findViewById(R.id.picture_list_tv_title);
        txtPreview = (TextView) findViewById(R.id.picture_list_txt_preview);
        txtDone = (TextView) findViewById(R.id.picture_list_txt_done);

        ImmersionBar.with(this).titleBar(llTitle).init();

        btnAlbum.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        txtPreview.setOnClickListener(this);
        txtDone.setOnClickListener(this);


    }


    /**
     * @file Framework:com.flueky.library.album.PictureListActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月23日 下午2:18:04
     */
    @Override
    protected void initData() {
        super.initData();
        if (permissionManager == null)
            permissionManager = new PermissionManager(this);
        if (!permissionManager.havePermissionRequest()) {
            permissionManager.requestPermission();
            return;
        }

        pictureAdaper = new PicturesAdapter(this);

        gvPics.setAdapter(pictureAdaper);

        maxCount = getIntent().getIntExtra("max", -1);
        pictureAdaper.setMax(maxCount);
        boolean supportTakePhoto = getIntent().getBooleanExtra("flag", false);
        pictureAdaper.setSupportTakePhoto(supportTakePhoto);

        albumResult = new AlbumResult();
//        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        ContentResolver mResolver = getContentResolver();
//        Cursor cursor = mResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED);
//        if (cursor == null)
//            return;
//        while (cursor.moveToNext()) {
//            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            File file = new File(path);
//            // 文件不存在
//            if (!file.exists())
//                continue;
//            if (path.endsWith(".png") || path.endsWith(".jpg"))
//                albumResult.add(path);
//            Log.d("TAG", "initData: " + path);
//        }
        LocalPicturesManager picturesManager = LocalPicturesManager.getInstance();
        picturesManager.loadPictures(this);
        List<String> pics = picturesManager.getLocalPictures();
        for (String path : pics)
            albumResult.add(path);
        pictureAdaper.setData(albumResult.getRecent());
        picturesManager.setRecentPics(albumResult.getRecent());
        txtTitle.setText("所有照片");
        choose = new ArrayList<String>();
        pictureAdaper.setChoose(choose);
        updateBottom();

    }

    /**
     * @file Framework:com.flueky.library.album.PictureListActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月27日 下午9:05:26
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.picture_list_btn_album) {
            Intent intent = new Intent(this, AlbumListActivity.class);
            intent.putExtra("albums", albumResult);
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } else if (v.getId() == R.id.picture_list_btn_cancel) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (v.getId() == R.id.picture_list_txt_preview) {
            if (choose.size() == 0)
                return;
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putStringArrayListExtra("choose", choose);
            intent.putStringArrayListExtra("pics", choose);
            startActivityForResult(intent, PictureListActivity.REQUEST_CODE_PREVIEW);
        } else if (v.getId() == R.id.picture_list_txt_done) {
            if (choose.size() == 0) {
                setResult(RESULT_CANCELED);
            } else {
                Intent data = getIntent() == null ? new Intent() : getIntent();
                data.putStringArrayListExtra("pictures", choose);
                setResult(RESULT_OK, data);
            }
            finish();
        }
    }

    /**
     * @file Framework:com.flueky.library.album.PictureListActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月27日 下午9:49:37
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED && requestCode != PictureListActivity.REQUEST_CODE_PREVIEW) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        if (requestCode == PictureListActivity.REQUEST_CODE_PREVIEW && resultCode == RESULT_OK) {
            choose = data.getStringArrayListExtra("choose");
            updateBottom();
            pictureAdaper.setChoose(choose);
        } else if (requestCode == REQUEST_CODE_ALBUM && resultCode == AlbumListActivity.RESULT_CODE_ALBUM_CHOOSE) {
            pictureAdaper.setData(data.getStringArrayListExtra("pics"));
            txtTitle.setText(data.getStringExtra("name"));
        }
    }

    /**
     * 更新底部提示
     *
     * @file Framework:com.flueky.library.album.PictureListActivity.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年3月9日 上午9:07:32
     */
    public void updateBottom() {
        if (choose == null || choose.size() == 0) {
            txtPreview.setEnabled(false);
            txtDone.setText("完成(" + 0 + ")");
        } else {
            txtPreview.setEnabled(true);
            txtDone.setText("完成(" + choose.size() + ")");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionRefuse(String s) {

    }

    @Override
    public void onRequestPermissionFinish(boolean b) {
        if (!b)
            initData();
    }
}
