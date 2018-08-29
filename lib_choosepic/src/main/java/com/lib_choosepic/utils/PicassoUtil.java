package com.lib_choosepic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.lib_choosepic.R;

import java.io.File;

public class PicassoUtil {

    /**
     * 展示本地图片略缩图
     *
     * @param path
     * @param imageView
     * @param bestWidth  理想宽度,pix,0表示自适应
     * @param bestHeight 理想高度,pix,0表示自适应
     */
    public static void showImage(Context context, String path, ImageView imageView, int bestWidth, int bestHeight) {
        if (TextUtils.isEmpty(path)) {
            path = "";
        }
        Picasso.with(context).setLoggingEnabled(true);
        RequestCreator creator = Picasso.with(context).load(new File(path));
        reSize(creator, bestWidth, bestHeight);
        creator.centerCrop()
                .config(Bitmap.Config.RGB_565)
                .priority(Picasso.Priority.LOW).skipMemoryCache().placeholder(R.drawable.icon_default)
                .into(imageView);
    }

    /**
     * 调整图片显示大小
     *
     * @param creator
     * @param bestWidth
     * @param bestHeight
     */
    private static void reSize(RequestCreator creator, int bestWidth, int bestHeight) {
        if (bestWidth < 0) {
            bestWidth = 0;
        }
        if (bestHeight < 0) {
            bestHeight = 0;
        }
        if (bestWidth > 0 || bestHeight > 0) {
            creator.resize(bestWidth, bestHeight);
        }
    }
}
