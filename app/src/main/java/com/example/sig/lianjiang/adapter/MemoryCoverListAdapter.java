package com.example.sig.lianjiang.adapter;

import android.app.Activity;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.Toast;

import com.example.sig.lianjiang.R;

import com.example.sig.lianjiang.bean.CoverPictureBean;

import com.example.sig.lianjiang.utils.ImageUtils;



import java.util.List;

/**
 * Created by sig on 2018/7/26.
 */

public class MemoryCoverListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CoverPictureBean> mList;
    protected LayoutInflater inflater;

    public MemoryCoverListAdapter(Context context, List<CoverPictureBean> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.item_memory_cover_update, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.imgCoverOne.setImageResource(R.drawable.addpic);
            holder.imgCoverOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAddCoverImage();
                }
            });
        } else {
            holder.imgCoverOne.setImageResource(R.mipmap.memory1);
            holder.imgCoverOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectUpateCover(mList.get(position).getUrl1());
                }
            });
        }
        holder.imgCoverTwo.setImageResource(R.mipmap.memory1);
        holder.imgCoverTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUpateCover(mList.get(position).getUrl2());
            }
        });
        return convertView;
    }

    public void clickAddCoverImage() {
        ImageUtils.showImagePickDialog((Activity)mContext);
    }

    public void selectUpateCover(String url) {
        Toast.makeText(mContext, "选择默认的封面" + url, Toast.LENGTH_SHORT).show();
    }

    private class ViewHolder {
        ImageView imgCoverOne;
        ImageView imgCoverTwo;


        public ViewHolder(View convertView) {
            imgCoverOne = (ImageView) convertView.findViewById(R.id.img_cover_one);
            imgCoverTwo = (ImageView) convertView.findViewById(R.id.img_cover_two);

        }
    }


}
