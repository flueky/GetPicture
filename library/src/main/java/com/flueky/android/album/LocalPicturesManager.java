package com.flueky.android.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalPicturesManager {

    private static LocalPicturesManager mgr;
    private List<String> pics;
    private List<String> recentPics;
    private boolean hasLoadPic = false;

    public LocalPicturesManager() {
        pics = new ArrayList<>();
    }

    public static LocalPicturesManager getInstance() {
        if (mgr == null)
            mgr = new LocalPicturesManager();
        return mgr;
    }

    public boolean isHasLoadPic() {
        return hasLoadPic;
    }

    public synchronized void loadPictures(Context context) {
        if (hasLoadPic)
            return;
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mResolver = context.getContentResolver();
        Cursor cursor = mResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File file = new File(path);
            // 文件不存在
            if (!file.exists())
                continue;
            if (path.endsWith(".png") || path.endsWith(".jpg"))
//                albumResult.add(path);
                pics.add(path);
//            Log.d("TAG", "initData: " + path);
        }
        cursor.close();
        hasLoadPic = true;
    }

    public List<String> getLocalPictures() {
        return pics;
    }


    public List<String> getRecentPics() {
        if(recentPics==null)
            recentPics = new ArrayList<>();
        return recentPics;
    }

    public void setRecentPics(List<String> recentPics) {
        this.recentPics = recentPics;
    }
}
