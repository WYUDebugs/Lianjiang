package com.example.sig.lianjiang.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.view.NineGridTestLayout;

import java.util.List;

/**
 * Created by sig on 2018/7/26.
 */

public class NineGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    public NineGridAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<NineGridTestModel> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return getListSize(mList);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(position==0){
            convertView = inflater.inflate(R.layout.activity_square_list1, parent, false);
        }else {
            if (convertView == null || convertView.getTag() == null) {
                convertView = inflater.inflate(R.layout.square_item_view, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.layout.setIsShowAll(mList.get(position).isShowAll);
            holder.layout.setUrlList(mList.get(position).urlList);
        }


        return convertView;
    }

    private class ViewHolder {
        NineGridTestLayout layout;

        public ViewHolder(View view) {
            layout = (NineGridTestLayout) view.findViewById(R.id.layout_nine_grid);
        }
    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
