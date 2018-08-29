package com.lib_choosepic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lib_choosepic.R;
import com.lib_choosepic.entity.GalleryEntity;
import com.lib_choosepic.utils.PicassoUtil;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<GalleryEntity> data = new ArrayList<>();

    public GalleryAdapter(Context mContext) {
        this.mContext = mContext;
        infalter = LayoutInflater.from(mContext);
    }

    public ArrayList<GalleryEntity> getGalleryEntitys() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GalleryEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = infalter.inflate(R.layout.item_gallery, null);
            holder.dvGalleryImg = (ImageView) convertView.findViewById(R.id.dvGalleryImg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GalleryEntity galleryEntity=getItem(position);
        PicassoUtil.showImage(mContext,galleryEntity.getSdcardPath(),holder.dvGalleryImg,160,160);
        return convertView;
    }

    public void addAll(ArrayList<GalleryEntity> files) {
        data = files;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        ImageView dvGalleryImg;
    }
}
