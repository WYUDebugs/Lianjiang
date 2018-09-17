package com.example.sig.lianjiang.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.utils.CommentFun;
import com.example.sig.lianjiang.utils.CustomTagHandler;
import com.example.sig.lianjiang.view.NineGridTestLayout;

import java.util.List;
/**
 * Created by sig on 2018/9/15.
 */

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.ViewHolder>{
    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    private CustomTagHandler mTagHandler;

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
        CommentFun.parseCommentList(mContext, mList.get(position).mComment,
                holder.mCommentList,holder.mBtnInput,  mTagHandler);
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
        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            mCommentList = (LinearLayout) itemView.findViewById(R.id.comment_list);
            mContent=(TextView)itemView.findViewById(R.id.square_content);
            mBtnInput=(ImageView)itemView.findViewById(R.id.img_input_comment);
        }
    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
