package com.xm4399.lib_choosepic.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.xm4399.lib_choosepic.activity.CustGalleryActivity;
import com.xm4399.lib_choosepic.enums.SelectType;

import java.io.File;

public class ChoosePic {
    /**
     * app 的 Application类中初始化的时候init传入 context
     */
    private static Context context;

    public static void init(Context context) {
        ChoosePic.context = context;
    }

    public static Context getContext() {
        if (context == null) {
            throw new RuntimeException("context 不能为空，请先调用ChoosePic.init(context)");
        }
        return context;
    }

    /**
     * @param activity
     * @param cropSize
     * @param selectType
     * @param requestCode
     */
    public static void toChoosePicActivity(Activity activity, int[] cropSize, SelectType selectType, int requestCode) {
        Intent intent = new Intent(activity, CustGalleryActivity.class);
        intent.putExtra(Constant.SELECT_TYPE, selectType);
        intent.putExtra(Constant.CROP_SIZE, cropSize);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri       输入路径
     * @param uriOutput 输出路径
     * @param width     宽
     * @param height    高
     * @return
     */
    public static void startPhotoCrop(Activity activity, Uri uri, Uri uriOutput, int width, int height, int requstCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        if (width > 0 & height > 0) {
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);
        activity.startActivityForResult(intent, requstCode);
    }

    public static Uri getUriFromFile(Context mContext, File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String applicationId = getContext().getApplicationInfo().processName;
            ChoosePicLog.i(applicationId);
            return FileProvider.getUriForFile(mContext, applicationId + ".fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
