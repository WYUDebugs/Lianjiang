package com.example.sig.lianjiang.adapter;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.activity.MemoryCoverUpdateActivity;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.MomentInfoActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.MemoryNineGridModel;
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
 * Created by sig on 2018/7/26.
 */

public class MemoryNineGridAdapter extends RecyclerView.Adapter<MemoryNineGridAdapter.ViewHolder> {

//    private Context mContext;
//    private List<MemoryNineGridModel> mList;
//    protected LayoutInflater inflater;
//    public MemoryNineGridAdapter(Context context) {
//        mContext = context;
//        inflater = LayoutInflater.from(context);
//    }
//
//    public void setList(List<MemoryNineGridModel> list) {
//        mList = list;
//    }
//
//    @Override
//    public int getCount() {
//        return getListSize(mList);
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder = null;
//        if(position==0){
//            convertView = inflater.inflate(R.layout.layout_moment_header, parent, false);
//            convertView.findViewById(R.id.img_memory_cover_update).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, MemoryCoverUpdateActivity.class);
//                    mContext.startActivity(intent);
//                    //Toast.makeText(mContext, "更新封面", Toast.LENGTH_SHORT).show();
//                }
//            });
//            convertView.findViewById(R.id.img_memory_name_update).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Dialog dialog1;
//                    View diaView = View.inflate(mContext, R.layout.dialogui_center_add, null);
//                    dialog1 = new Dialog(mContext, R.style.dialog);
//                    dialog1.setContentView(diaView);
//                    dialog1.show();
//                    TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
//                    TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
//                    TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
//                    tvInfo.setText("请输入更改后的纪念册名称");
//                    final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
//                    tvSure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog1.cancel();
//                        }
//                    });
//                    tvCancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog1.cancel();
//                        }
//                    });
//                }
//            });
//        }else {
//            if (convertView == null || convertView.getTag() == null) {
//                convertView = inflater.inflate(R.layout.item_moment, parent, false);
//                holder = new ViewHolder(convertView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            holder.layout.setIsShowAll(mList.get(position).isShowAll);
//            holder.layout.setUrlList(mList.get(position).urlList);
//        }
//
//
//        return convertView;
//    }
//
//    private class ViewHolder {
//        NineGridTestLayout layout;
//
//        public ViewHolder(View view) {
//            layout = (NineGridTestLayout) view.findViewById(R.id.layout_nine_grid);
//        }
//    }
//
//    private int getListSize(List<MemoryNineGridModel> list) {
//        if (list == null || list.size() == 0) {
//            return 0;
//        }
//        return list.size();
//    }
    private Context mContext;
    private List<MemoryNineGridModel> mList;
    protected LayoutInflater inflater;
    public static final int KEY_NAMEID = -2018101;
    public static final int KEY_MOMENTID = -2018102;

    public MemoryNineGridAdapter(Context context,List<MemoryNineGridModel> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<MemoryNineGridModel> list) {
        mList = list;
    }

    @Override
    public MemoryNineGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_moment, parent, false);
        MemoryNineGridAdapter.ViewHolder viewHolder = new MemoryNineGridAdapter.ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MemoryNineGridAdapter.ViewHolder holder, int position) {
        holder.layout.setIsShowAll(mList.get(position).isShowAll);
        holder.layout.setUrlList(mList.get(position).urlList);
        holder.mContent.setText(mList.get(position).mContent);
        holder.name.setText(mList.get(position).name);
        holder.time.setText(mList.get(position).time);
        holder.head.setTag(KEY_NAMEID,mList.get(position).userId);
        holder.name.setTag(KEY_NAMEID,mList.get(position).userId);
        holder.lv_moment.setTag(KEY_MOMENTID,mList.get(position).momentId);
        holder.lv_moment.setTag(KEY_NAMEID,mList.get(position).userId);
        Picasso.with(mContext).load(APPConfig.img_url + mList.get(position).head)
                .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(holder.head);

    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;
        TextView mContent;
        ImageView head;
        TextView name;
        TextView time;
        TextView delete;
        LinearLayout lv_moment;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            mContent=(TextView)itemView.findViewById(R.id.tv_content);

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
            delete=(TextView)itemView.findViewById(R.id.tv_delete);
            lv_moment=(LinearLayout)itemView.findViewById(R.id.lv_moment);
            lv_moment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, MomentInfoActivity.class);
                    intent.putExtra("momentId",view.getTag(KEY_MOMENTID).toString());
                    intent.putExtra("userId",view.getTag(KEY_NAMEID).toString());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private int getListSize(List<MemoryNineGridModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
