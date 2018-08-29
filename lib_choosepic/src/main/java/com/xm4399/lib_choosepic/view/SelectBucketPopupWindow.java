package com.xm4399.lib_choosepic.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xm4399.lib_choosepic.R;
import com.xm4399.lib_choosepic.entity.GalleryBucketEntity;
import com.xm4399.lib_choosepic.utils.Consumer;
import com.xm4399.lib_choosepic.utils.PicassoUtil;

import java.util.ArrayList;

/**
 * 选择相册名称分类
 *
 * @author lisg
 * @since 18-07-26 16:22
 */
public class SelectBucketPopupWindow extends PopupWindow {
    private Context mContext;
    private ArrayList<GalleryBucketEntity> allBuckets;
    private LayoutInflater infalter;
    private View vContanier;
    private ListView lvBuckets;
    private Consumer<GalleryBucketEntity> selectBucketConsumer;

    public void setSelectBucketConsumer(Consumer<GalleryBucketEntity> selectBucketConsumer) {
        this.selectBucketConsumer = selectBucketConsumer;
    }

    public SelectBucketPopupWindow(Context mContext, ArrayList<GalleryBucketEntity> allBuckets) {
        super(mContext);
        this.mContext = mContext;
        this.allBuckets = allBuckets;
        infalter = LayoutInflater.from(mContext);
        initView();
        initEvent();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.popupwindow_select_bucket_layout, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
//        setAnimationStyle(R.style.popAnimationPreview);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        update();
        vContanier = view.findViewById(R.id.ll_container);
        lvBuckets = (ListView) view.findViewById(R.id.lv_buckets);
        BucketAdapter adapter = new BucketAdapter();
        lvBuckets.setAdapter(adapter);
    }

    private void initEvent() {
        lvBuckets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectBucketConsumer != null) {
                    selectBucketConsumer.accept(allBuckets.get(position));
                    dismiss();
                }
            }
        });
        vContanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    class BucketAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return allBuckets == null ? 0 : allBuckets.size();
        }

        @Override
        public Object getItem(int position) {
            return allBuckets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GalleryBucketEntity bucket = allBuckets.get(position);
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = infalter.inflate(R.layout.item_bucket, null);
                holder.ivBucket = (ImageView) convertView.findViewById(R.id.iv_bucket);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_bucket_name);
                holder.tvNum = (TextView) convertView.findViewById(R.id.tv_bucket_num);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(bucket.getName());
            holder.tvNum.setText("(" + bucket.getCount() + "张)");
            PicassoUtil.showImage(mContext,bucket.getLastPath(), holder.ivBucket, 160, 160);
            if (bucket.isSelected) {
                holder.tvName.setSelected(true);
                holder.tvNum.setSelected(true);
            } else {
                holder.tvName.setSelected(false);
                holder.tvNum.setSelected(false);
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvNum;
            ImageView ivBucket;
        }
    }


}
