package com.example.sig.lianjiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.MemoryBookStarModel;
import com.example.sig.lianjiang.model.MemoryListModel;
import com.moxun.tagcloudlib.view.TagsAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sig on 2018/7/10.
 */

public class MemoryBookStarAdapter extends TagsAdapter {


    private List<MemoryBookStarModel> mList;

    public MemoryBookStarAdapter(List<MemoryBookStarModel> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        View view=View.inflate(context, R.layout.item_memory_star, null);
        LinearLayout memoryBook=(LinearLayout)view.findViewById(R.id.ll_memory);
        TextView title=view.findViewById(R.id.tv_title);
        ImageView cover=view.findViewById(R.id.iv_cover);
        title.setText(mList.get(position).title);
        Picasso.with(context).load(APPConfig.test_image_url + mList.get(position).cover)
                .placeholder(R.mipmap.memory1).error(R.mipmap.memory1).into(cover);
        memoryBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MemoryBookActivity.class);
                intent.putExtra("memoryBookId",mList.get(position).memoryBookId);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public MemoryBookStarModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 1;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
