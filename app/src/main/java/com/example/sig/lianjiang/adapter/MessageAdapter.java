package com.example.sig.lianjiang.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.view.DragDeleteTextView;

import java.util.List;

/**
 * Created by sig on 2018/7/11.
 */

public class MessageAdapter extends ArrayAdapter<Message>{
    private int resourceId;
    public static int flag=0;
    public MessageAdapter(Context context, int textViewResourceId, List<Message> object){
        super(context,textViewResourceId,object);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Message message=getItem(position);
        View view;
        final ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.userHead=(ImageView)view.findViewById(R.id.user_head);
            viewHolder.userName=(TextView)view.findViewById(R.id.user_name);
            viewHolder.mainMean=(TextView)view.findViewById(R.id.main_mean);
            viewHolder.time=(TextView)view.findViewById(R.id.message_time);
            viewHolder.messageNum=(DragDeleteTextView)view.findViewById(R.id.message_num);


            viewHolder.messageNum.setOnDeleteTextListener(new DragDeleteTextView.OnDeleteTextListener() {     //监听函数
                @Override
                public void onDelete(View view) {
                    //viewHolder.messageNum.setVisibility(View.INVISIBLE);
                }
            });
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.userHead.setImageResource(message.getImageId());
        viewHolder.userName.setText(message.getName());
        viewHolder.mainMean.setText(message.getMainMean());
        viewHolder.time.setText(message.getTime());
        viewHolder.messageNum.setText(Integer.toString(message.getMessageNum()));
        if (message.getMessageNum()>0){
            viewHolder.messageNum.setText(Integer.toString(message.getMessageNum()));
        }else {
            viewHolder.messageNum.setVisibility(View.INVISIBLE);
        }
        view.setTag(viewHolder);
        return view;
    }

    class ViewHolder{
        ImageView userHead;
        TextView userName;
        TextView mainMean;
        TextView time;
        DragDeleteTextView messageNum;
    }
}
