package com.lib_choosepic.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib_choosepic.R;
import com.lib_choosepic.adapter.GalleryAdapter;
import com.lib_choosepic.entity.GalleryBucketEntity;
import com.lib_choosepic.entity.GalleryEntity;
import com.lib_choosepic.enums.SelectType;
import com.lib_choosepic.utils.ChoosePic;
import com.lib_choosepic.utils.ChoosePicLog;
import com.lib_choosepic.utils.Constant;
import com.lib_choosepic.utils.Consumer;
import com.lib_choosepic.view.SelectBucketPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author lisg
 * @since 18-07-21 17:52
 */
public class CustGalleryActivity extends FragmentActivity {
    private RelativeLayout titleBar;
    private TextView tvTopTitle;
    private TextView tvBack;
    String TAG = "CustGalleryActivity";
    private HashMap<String, GalleryBucketEntity> mBuckets = new LinkedHashMap<>();
    SelectBucketPopupWindow selectBucketPopupWindow;
    private String allName = "全部";
    private GridView gridGallery;
    private GalleryAdapter galleryAdapter;
    private SelectType selectType;
    private int cropSize[];
    private File cropDistTempFile;
    private File takePhotoTempFile;
    /**
     * 单张图片crop时候 requestcode
     */
    private final static int REQUSE_SINGLE_CROP = 1;
    private final static int REQUSE_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_gallery);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        tvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        tvBack = (TextView) findViewById(R.id.tvBack);
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        galleryAdapter = new GalleryAdapter(this);
        gridGallery.setAdapter(galleryAdapter);
    }

    private void initData() {
        selectType = (SelectType) getIntent().getExtras().getSerializable(Constant.SELECT_TYPE);
        //默认
        if (selectType == null) {
            selectType = SelectType.SELECT_PIC_FOR_CROP;
        }
        cropSize = getIntent().getExtras().getIntArray(Constant.CROP_SIZE);
        //默认
        if (cropSize == null || cropSize.length < 2) {
            cropSize = new int[]{200, 200};
        }
        getGalleryInfo();
        tvTopTitle.setText(allName);
        selectBucketPopupWindow = new SelectBucketPopupWindow(this, new ArrayList<>(mBuckets.values()));
        if (selectType == SelectType.TAKE_PHOTO_FOR_CROP) {
            takePhoto();
        }
    }

    private void initEvent() {
        tvTopTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBucketPopupWindow.showAtLocation(titleBar, Gravity.NO_GRAVITY, 0, 0);
            }
        });
        selectBucketPopupWindow.setSelectBucketConsumer(new Consumer<GalleryBucketEntity>() {
            @Override
            public void accept(GalleryBucketEntity galleryBucketEntity) {
                tvTopTitle.setText(galleryBucketEntity.getName());
                galleryAdapter.addAll(galleryBucketEntity.getGalleries());
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = galleryAdapter.getGalleryEntitys().get(position).getSdcardPath();
                ChoosePicLog.i(path);
                if (selectType == SelectType.SELECT_PIC_FOR_CROP) {
                    cropImage(ChoosePic.getUriFromFile(CustGalleryActivity.this, new File(path)));
                }
//
            }
        });

    }

    private void cropImage(Uri srcUri) {
        if (srcUri == null) {
            throw new RuntimeException("图片的路径为空");
        }
        cropDistTempFile = getTempFile();
        ChoosePic.startPhotoCrop(this, srcUri, Uri.fromFile(cropDistTempFile), cropSize[0], cropSize[1], REQUSE_SINGLE_CROP);
    }


    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        takePhotoTempFile = getTempFile();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, ChoosePic.getUriFromFile(this, takePhotoTempFile));
        startActivityForResult(takePictureIntent, REQUSE_TAKE_PHOTO);
    }

    private File getTempFile() {
        return new File(getExternalCacheDir().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUSE_SINGLE_CROP:
                if (!cropDistTempFile.exists()) {
                    ChoosePicLog.e("??eeeeeeeee" + requestCode);
                    return;
                }
                Intent i = new Intent().putExtra(Constant.SINGLE_CROP_DEST_PATH, Uri.fromFile(cropDistTempFile).toString());
                setResult(Activity.RESULT_OK, i);
                finish();
                break;
            case REQUSE_TAKE_PHOTO:
                if (!takePhotoTempFile.exists()) {
                    ChoosePicLog.e("**获取拍照图片失败" + requestCode);
                    finish();
                    return;
                }
                cropImage(ChoosePic.getUriFromFile(this, takePhotoTempFile));
                break;
            default:
                break;
        }
    }

    /**
     * 获取相册分类及相册分类下面的图片
     */
    private void getGalleryInfo() {
        GalleryBucketEntity allGalleryBucket = new GalleryBucketEntity();
        allGalleryBucket.setName(allName);
        mBuckets.put(allName, allGalleryBucket);
        Cursor imagecursor = null;
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        imagecursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, MediaStore.Images.ImageColumns.DATE_TAKEN + " desc");
        try {
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    GalleryEntity galleryEntity = new GalleryEntity();
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    int sizeColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.SIZE);
                    int bucketNameColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    String bucketName = imagecursor.getString(bucketNameColumnIndex);
                    galleryEntity.setSdcardPath(imagecursor.getString(dataColumnIndex));
                    String size = imagecursor.getString(sizeColumnIndex);
                    if (!TextUtils.isEmpty(size) && Long.parseLong(size) > 0) {
                        if (!mBuckets.containsKey(bucketName)) {
                            GalleryBucketEntity bucket = new GalleryBucketEntity();
                            bucket.setName(bucketName);
                            mBuckets.put(bucketName, bucket);
                        }
                        mBuckets.get(bucketName).addGallery(galleryEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imagecursor != null) {
                imagecursor.close();
            }
        }
        ArrayList<GalleryEntity> galleryList = new ArrayList<>();
        Iterator<String> it =
                mBuckets.keySet().iterator();
        while (it.hasNext()) {
            ArrayList<GalleryEntity> customGalleries = mBuckets.get(it.next()).getGalleries();
            if (customGalleries != null) {
                galleryList.addAll(customGalleries);
            }
        }
        mBuckets.get(allName).setGalleries(galleryList);
        galleryAdapter.addAll(galleryList);
    }
}
