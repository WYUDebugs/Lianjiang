package com.example.sig.lianjiang.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.utils.CommentFun;
import com.example.sig.lianjiang.utils.CustomTagHandler;
import com.example.sig.lianjiang.utils.GoodFun;
import com.example.sig.lianjiang.view.NineGridTestLayout;
import com.example.sig.lianjiang.view.PraiseTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import czh.library.LikeView;

import static com.example.sig.lianjiang.utils.TimeUtil.beforeToday;
import static com.example.sig.lianjiang.utils.TimeUtil.stampToDate;

/**
 * Created by sig on 2018/9/15.
 */

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.ViewHolder> {
    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    private CustomTagHandler mTagHandler;
    public static final int KEY_NAMEID = -2018924;

    public SquareAdapter(Context context,List<NineGridTestModel> list,CustomTagHandler tagHandler) {
        mContext = context;
        mList = list;
        mTagHandler = tagHandler;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<NineGridTestModel> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.square_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.layout.setIsShowAll(mList.get(position).isShowAll);
        holder.layout.setUrlList(mList.get(position).urlList);
        holder.mContent.setText(mList.get(position).mContent);
        CommentFun.parseCommentList(mContext, mList.get(position).mComment,mList.get(position).publishId,
                holder.mCommentList,holder.mBtnInput,  mTagHandler);
        GoodFun.parseGoodList(mContext,mList.get(position).mPraiseInfos,mList.get(position).publishId,holder.praiseTextView,holder.lv_good,holder.like);
        holder.name.setText(mList.get(position).name);
//        holder.time.setText(mList.get(position).time);
//        timestampToString
        if(beforeToday(mList.get(position).time)){
            holder.time.setText(stampToDate(mList.get(position).time,"yyyy-MM-dd HH:mm"));
            Log.d("shijian",stampToDate(mList.get(position).time,"yyyy-MM-dd HH:mm:ss"));
        }else{
            holder.time.setText(stampToDate(mList.get(position).time,"HH:mm"));
            Log.d("shijian",stampToDate(mList.get(position).time,"yyyy-MM-dd HH:mm:ss"));
        }
//        holder.time.setText(TimeUtil.stampToDate(mList.get(position).time));
        if(mList.get(position).address.length()>20){
            holder.address.setTextSize(10);
        }
        holder.address.setText(mList.get(position).address);
        if(mList.get(position).likeFlag){
            holder.lv_good.selectLike(true);
        }else {
            holder.lv_good.selectLike(false);
        }
        holder.head.setTag(KEY_NAMEID,mList.get(position).userId);
        holder.name.setTag(KEY_NAMEID,mList.get(position).userId);
        Picasso.with(mContext).load(APPConfig.img_url + mList.get(position).head)
                .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(holder.head);

    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;
        LinearLayout mCommentList;
        TextView mContent;
        View mBtnInput;
        LikeView lv_good;
        PraiseTextView praiseTextView;
        ImageView head;
        TextView name;
        TextView time;
        TextView address;
        ImageView like;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            mCommentList = (LinearLayout) itemView.findViewById(R.id.comment_list);
            mContent=(TextView)itemView.findViewById(R.id.square_content);
            mBtnInput=(ImageView)itemView.findViewById(R.id.img_input_comment);
            praiseTextView=(PraiseTextView)itemView.findViewById(R.id.praisetextview);
            like=(ImageView)itemView.findViewById(R.id.iv_like) ;
            lv_good=(LikeView)itemView.findViewById(R.id.lv_good);
            lv_good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lv_good.isChecked()) {
                        lv_good.unLike();
                        GoodFun.addGood(mContext,view,praiseTextView,false,like);
                    } else {
                        lv_good.like();
                        GoodFun.addGood(mContext,view,praiseTextView,true,like);
                    };
                }
            });
            head=(ImageView) itemView.findViewById(R.id.iv_head);
            head.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("username",(String)head.getTag(KEY_NAMEID));
                    mContext.startActivity(intent);
                }
            });
            name=(TextView)itemView.findViewById(R.id.tv_name);
            name.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("username",(String)name.getTag(KEY_NAMEID));
                    mContext.startActivity(intent);
                }
            });
            time=(TextView)itemView.findViewById(R.id.tv_time);
            address=(TextView)itemView.findViewById(R.id.tv_address);
        }
    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

}
