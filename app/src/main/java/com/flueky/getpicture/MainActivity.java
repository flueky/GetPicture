package com.flueky.getpicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flueky.library.GetPicture;

import java.util.List;

public class MainActivity extends Activity {

    private GetPicture getPicture;
    private ImageView ivPicture;
    private TextView tvPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivPicture = (ImageView) findViewById(R.id.main_iv_picture);
        tvPicture = (TextView) findViewById(R.id.main_tv_picture);
        getPicture = new GetPicture(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_take_picture_crop:
                getPicture.setCrop(true);
                getPicture.takePhoto();
                break;
            case R.id.main_btn_read_album_crop:
                getPicture.setCrop(true);
                getPicture.readAlbum();
                break;
            case R.id.main_btn_take_picture:
                getPicture.setCrop(false);
                getPicture.takePhoto();
                break;
            case R.id.main_btn_read_album_single:
                getPicture.setCrop(false);
                getPicture.readAlbum();
                break;
            case R.id.main_btn_read_album_more:
                getPicture.setCrop(false);
                getPicture.readAlbum(9);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getPicture.checkResult(requestCode, resultCode, data)) {
            if (getPicture.isCrop())
                ivPicture.setImageBitmap(getPicture.getCropBit());
            else {
                List<String> files = getPicture.getChooseFile();
                if (files == null)
                    return;
                StringBuffer buffer = new StringBuffer();
                for (String path : files) {
                    buffer.append(path + "\n");
                }
                tvPicture.setText(buffer.toString());
            }
        }
    }
}
