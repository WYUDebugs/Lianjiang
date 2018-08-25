package com.example.sig.lianjiang.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sig.lianjiang.R;

import java.util.List;

/**
 * Created by sig on 2018/8/22.
 */

public class UserProfileAdapter extends BaseAdapter {
    private Context mContext;
    protected LayoutInflater inflater;
    private List<UserProfile> mList;
    public UserProfileAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }
    public void setList(List<UserProfile> list) {
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
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        View view;
        if(position==0){
            view = inflater.inflate(R.layout.user_profile_0, parent, false);
        }else{
            if (convertView == null|| convertView.getTag() == null) {
                view = inflater.inflate(R.layout.user_profile_1, parent, false);
                holder=new ViewHolder();
                holder.imageView=view.findViewById(R.id.user_image);
                holder.textView=view.findViewById(R.id.text);
                holder.next=view.findViewById(R.id.next);

                holder.imageView.setImageResource(mList.get(position).getImageView());
                holder.textView.setText(mList.get(position).getTextView());
                if(mList.get(position).isFlag()){
                    holder.next.setVisibility(View.VISIBLE);
                }else {
                    holder.next.setVisibility(View.INVISIBLE);
                }
                view.setTag(holder);
            } else {
                view=convertView;
//                holder=(ViewHolder)view.getTag();
            }

        }
        return view;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
        ImageView next;
    }
    private int getListSize(List<UserProfile> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
