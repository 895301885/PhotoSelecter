package com.example.rxjavademo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.lib_choosepic.enums.SelectType;
import com.lib_choosepic.utils.ChoosePic;
import com.lib_choosepic.utils.Constant;
import com.lib_choosepic.utils.PicassoUtil;

import rx.functions.Action1;

public class MainActivity extends FragmentActivity {
    String TAG = "MainActivity";
    ImageView ivResut;
    TextView takePhoto, tvSelectBucket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivResut = (ImageView) findViewById(R.id.ivResut);
        takePhoto = (TextView) findViewById(R.id.takePhoto);
        tvSelectBucket = (TextView) findViewById(R.id.tvSelectBucket);


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions.getInstance(MainActivity.this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            ChoosePic.toChoosePicActivity(MainActivity.this, new int[]{600, 600}, SelectType.TAKE_PHOTO_FOR_CROP, 5);
                        }
                    }
                });
            }
        });
        tvSelectBucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions.getInstance(MainActivity.this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            ChoosePic.toChoosePicActivity(MainActivity.this, new int[]{600, 600}, SelectType.SELECT_PIC_FOR_CROP, 5);
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }                switch (requestCode) {

                case 5:
                if (data == null || TextUtils.isEmpty(data.getStringExtra(Constant.SINGLE_CROP_DEST_PATH))) {
                    Toast.makeText(this, "获取图片失败！", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d(TAG, "onActivityResult: " + data.getStringExtra(Constant.SINGLE_CROP_DEST_PATH));
                PicassoUtil.showImage(this,data.getStringExtra(Constant.SINGLE_CROP_DEST_PATH).replace("file://", ""), ivResut, 600, 600);
                break;
            default:
                break;
        }
    }
}
