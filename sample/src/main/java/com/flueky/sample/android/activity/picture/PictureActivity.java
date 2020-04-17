package com.flueky.sample.android.activity.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.flueky.android.basic.activity.FFBasicActivity;
import com.flueky.android.basic.tool.BitmapUtil;
import com.flueky.android.basic.tool.GetPicture;
import com.flueky.sample.R;

import org.json.JSONArray;


public class PictureActivity extends FFBasicActivity {
    private static final String TAG = "PictureActivity";


    private ImageView imageView;
    private Uri imageUri;
    private GetPicture getPicture;

    @Override
    protected int contentLayout() {
        return R.layout.activity_picture;
    }

    @Override
    protected void initView() {
        super.initView();

        imageView = findViewById(R.id.activity_picture_iv);


        getPicture = new GetPicture();
        getPicture.setGetResult(new GetPicture.GetPictureResult() {
            @Override
            public void onResult(Object data) {
                if (data instanceof Bitmap)
                    imageView.setImageBitmap((Bitmap) data);
                else if (data instanceof JSONArray) {
                    Toast.makeText(PictureActivity.this, ((JSONArray) data).toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(data.toString());
                    Log.d(TAG, "onResult: 压缩前 " + bitmap.getWidth() + " x " + bitmap.getHeight());
                    Log.d(TAG, "onResult: 压缩前 " + bitmap.getByteCount());
                    bitmap = BitmapUtil.compressScale(bitmap, 400, -1);
                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(PictureActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResult: 压缩后 " + bitmap.getWidth() + " x " + bitmap.getHeight());
                    Log.d(TAG, "onResult: 压缩后 " + bitmap.getByteCount());
                }
            }
        });

    }

    public void tpAlbum(View v) {
        getPicture.readAlbum(this,true);
    }
    public void album(View v) {
        getPicture.setCrop(false);
        getPicture.setMaxCount(10);
        getPicture.readAlbum(this);
    }

    public void takePhoto1(View v) {
        getPicture.setCrop(true);
        getPicture.takePhoto(this);

    }

    public void takePhoto2(View v) {
        getPicture.setCrop(false);
        getPicture.takePhoto(this);
    }

    public void readAlbum1(View v) {
        getPicture.setCrop(true);
        getPicture.readAlbum(this);
    }

    public void readAlbum2(View v) {
        getPicture.setCrop(false);
        getPicture.readAlbum(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        boolean flag = getPicture.checkResult(this, requestCode, resultCode, data);
        if (!flag)
            super.onActivityResult(requestCode, resultCode, data);

    }


}
