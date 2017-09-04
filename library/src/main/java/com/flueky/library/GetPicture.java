package com.flueky.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.flueky.library.album.PictureListActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 通过读相册和拍照的方式获取手机中的照片
 *
 * @author flueky zuokefei0217@163.com
 * @file Framework:com.flueky.android.util.GetPhoto.java
 * @time 2017年2月3日 下午2:29:48
 */
public class GetPicture {

    public static final int TAKE_PICTURE_CODE = 0x111;// 拍照
    public static final int CHOOSE_PICTUER_CODE = 0x112;// 从图库中取照片
    public static final int CROP_PICTURE_CODE = 0x113;// 裁剪照片
    public static final int CHOOSE_PICTUER_MULTI_CODE = 0x114;// 裁剪照片

    private Activity activity;
    //    private Bitmap picture;
    private ArrayList<String> chooseFile;
    private String takePhotoSavePath = Environment.getExternalStorageDirectory() + File.separator + "image.jpg";
    private boolean isCalled = false;
    private boolean isCrop = false;// 标记是否需要裁剪，默认裁剪大小300x300
    private Bitmap cropBit;
    private int cropWidth = 300;

    /**
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public GetPicture(Activity activity) {
        super();
        this.activity = activity;
//        Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        display.getRealMetrics(metrics);
//        cropWidth = (int) (metrics.widthPixels * 0.8f);
    }

    /**
     * 拍照
     *
     * @file Framework:com.flueky.android.util.GetPicture.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月3日 下午3:32:48
     */
    public void takePhoto() {

        // 跳转到拍照页面
        // if (isCameraCanUse()) {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(new File(takePhotoSavePath));
        // 设置拍照保存的图片路径
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(openCamera, TAKE_PICTURE_CODE);
        isCalled = true;
        // } else {
        // Toast.makeText(activity, "请您先开启相机权限", Toast.LENGTH_SHORT).show();
        // }
    }

    /**
     * 读相册
     *
     * @file Framework:com.flueky.android.util.GetPicture.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月3日 下午3:32:42
     */
    public void readAlbum() {
        readAlbum(1);
    }

    public void readAlbum(int count) {
        isCalled = true;
        if (count <= 1) {
            Intent openAlbum = new Intent(Intent.ACTION_PICK, null);
            openAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(openAlbum, CHOOSE_PICTUER_CODE);
        } else {
            Intent intent = new Intent(activity, PictureListActivity.class);
            intent.putExtra("max", count);
            activity.startActivityForResult(intent, CHOOSE_PICTUER_MULTI_CODE);
        }
    }

    public boolean isCrop() {
        return isCrop;
    }

    /**
     * 判断获取结果。可能取消操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     * @file Framework:com.flueky.android.util.GetPicture.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月3日 下午3:12:49
     */
    public boolean checkResult(int requestCode, int resultCode, Intent data) {
        if (!isCalled)
            return isCalled;
        if (resultCode == Activity.RESULT_CANCELED) {
//            picture = null;
            chooseFile = null;
            return false;
        }
        if (requestCode == TAKE_PICTURE_CODE) {
            Uri uri = Uri.fromFile(new File(takePhotoSavePath));
            chooseFile = new ArrayList<String>();
            chooseFile.add(takePhotoSavePath);
            if (uri == null) {
                return false;
            }
            if (isCrop) {
                cropImage(uri, cropWidth, cropWidth, CROP_PICTURE_CODE);
                return false;
            } else {
//                picture = compressImage(takePhotoSavePath);
                return true;
            }

        } else if (requestCode == CHOOSE_PICTUER_CODE) {

            if (data == null)
                return false;
            Uri uri = data.getData();
            if (uri == null) {
                return false;
            }
            String filepath = uri.toString();
            if (filepath.startsWith("file://")) {
                filepath = filepath.replace("file://", "");
            } else {
                filepath = getRealFilePath(activity, uri);
            }
            if (filepath != null) {
                chooseFile = new ArrayList<String>();
                chooseFile.add(filepath);
                uri = Uri.fromFile(new File(filepath));
                if (isCrop) {
                    cropImage(uri, cropWidth, cropWidth, CROP_PICTURE_CODE);
                    return false;
                } else {
//                    picture = compressImage(filepath);
                    return true;
                }
            }
            return false;
        } else if (requestCode == CROP_PICTURE_CODE) {
            if (data == null)
                return false;
            cropBit = (Bitmap) data.getExtras().get("data");
            return true;
        } else if (requestCode == CHOOSE_PICTUER_MULTI_CODE) {
            chooseFile = data.getStringArrayListExtra("pictures");
            return true;
        } else {
//            picture = null;
            chooseFile = null;
            return false;
        }
    }

    //
//    public Bitmap getBitmap() {
//        Bitmap temp = picture;
//        picture = null;
//        return temp;
//    }
//
    public String getBitmapBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bitmap = null;
            baos.flush();
            baos.close();
            return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getCropBit() {
        return cropBit;
    }

    public ArrayList<String> getChooseFile() {
        return chooseFile;
    }


    /**
     * 设置裁剪标记
     *
     * @param isCrop the isCrop to set
     */
    public void setCrop(boolean isCrop) {
        this.isCrop = isCrop;
    }

    /**
     * 裁剪照片，获得圆角
     *
     * @param bitmap
     * @param radius 圆角半径比例，0~1之间的浮点数，0表示四边形，1表示圆形。
     * @return
     * @file Framework:com.flueky.android.util.GetPicture.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月3日 下午3:29:52
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 判断相机是否可用
     *
     * @return
     * @file Framework:com.flueky.android.util.GetPicture.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月3日 下午3:31:32
     */
    @SuppressWarnings("deprecation")
    private boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera == null) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    /**
     * 裁剪照片
     *
     * @param uri
     * @param outputX     裁剪完的长度
     * @param outputY     裁剪完的宽度
     * @param requestCode
     * @file Framework:com.flueky.android.util.GetPicture.java
     * @author flueky zuokefei0217@163.com
     * @time 2017年2月3日 下午3:19:59
     */
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 宽高比
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);// 宽高
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("scale", true);// 黑边
        intent.putExtra("scaleUpIfNeeded", true);// 黑边
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    public String getRealFilePath(Context context, Uri uri) {
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, pojo, null, null, null);
        String picPath = null;
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        return picPath;
    }

    /**
     * @param srcPath 需要被压缩的图片的地址
     * @return Bitmap 返回被压缩后的图片
     * @Description 比例压缩方式
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午4:39:14
     */
    public Bitmap compressImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();// 创建位图属性
        newOpts.inJustDecodeBounds = true;// 读内容，如果为false，那么就是只读边(为了获取尺寸)不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 从接收的文件全路径和设定的属性来解析出位图
        // options.inSampleSize是以2的指数的倒数被进行放缩。这样，我们可以依靠inSampleSize的值的设定将图片放缩载入，
        // 这样一般情况也就不会出现上述的OOM问题了。现在问题是怎么确定inSampleSize的值？每张图片的放缩大小的比例应该是不一样的！
        // 这样的话就要运行时动态确定。在BitmapFactory.Options中提供了另一个成员inJustDecodeBounds。
        // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，但可计算出原始图片的长度和宽度，即opts.width和opts.height。
        // 有了这两个参数，再通过一定的算法，即可得到一个恰当的inSampleSize。
        newOpts.inSampleSize = computeSampleSize(newOpts, -1, 720 * 480);
        // 这里一定要将其设置回false，因为之前我们将其设置成了true
        newOpts.inJustDecodeBounds = false;

        // int w = newOpts.outWidth;// 获取属性的宽
        // int h = newOpts.outHeight;// 获取属性的高
        // // float hh = 720f;// 自定义高
        // float hh = 480f;// 自定义高
        // float ww = 320f;// 自定义宽
        // int be = 1;// 定义默认采样率
        // if (w > h && w > ww) {
        // be = (int) (newOpts.outWidth / ww);
        // } else if (w < h && h > hh) {
        // be = (int) (newOpts.outHeight / hh);
        // }
        // if (be <= 0)
        // be = 1;
        // newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }


    public int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}