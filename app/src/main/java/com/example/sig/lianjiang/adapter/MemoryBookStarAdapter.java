package com.example.sig.lianjiang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sig.lianjiang.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

/**
 * Created by sig on 2018/7/10.
 */

public class MemoryBookStarAdapter extends TagsAdapter {


    private List<String> mList;

    public MemoryBookStarAdapter(List<String> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        View view=View.inflate(context, R.layout.item_memory_star, null);
        TextView tv=view.findViewById(R.id.tcv_tags);
        tv.setText(getItem(position));
        return view;
    }

    @Override
    public String getItem(int position) {
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
