package com.example.sig.lianjiang.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by sig on 2018/7/26.
 */

public class NineGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    private CustomTagHandler mTagHandler;
    public NineGridAdapter(Context context ,List<NineGridTestModel> list,CustomTagHandler tagHandler) {
        mContext = context;
        mList = list;
        mTagHandler = tagHandler;
        inflater = LayoutInflater.from(context);
    }

//    public void setList(List<NineGridTestModel> list) {
//        mList = list;
//    }

    @Override
    public int getCount() {
        return getListSize(mList)+1;
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
//            if(mList.size()!=0){
                convertView = inflater.inflate(R.layout.activity_square_list1, parent, false);
//            }else {
//                convertView = inflater.inflate(R.layout.square_none, parent, false);
//            }
        }else {
            if (convertView == null || convertView.getTag() == null) {
                convertView = inflater.inflate(R.layout.square_item_view, parent, false);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            int index = position - 1;
            holder.mContent.setText(mList.get(index).mContent);
//            CommentFun.parseCommentList(mContext, mList.get(index).mComment,
//                    holder.mCommentList,holder.mBtnInput,  mTagHandler);
            holder.layout.setIsShowAll(mList.get(index).isShowAll);
            holder.layout.setUrlList(mList.get(index).urlList);


        }


        //防止ListView的OnItemClick与item里面子view的点击发生冲突
//        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        return convertView;
    }

    private class ViewHolder {
        NineGridTestLayout layout;
        LinearLayout mCommentList;
        TextView mContent;
        View mBtnInput;
        public ViewHolder(View view) {
            layout = (NineGridTestLayout) view.findViewById(R.id.layout_nine_grid);
            mCommentList = (LinearLayout) view.findViewById(R.id.comment_list);
            mContent=(TextView)view.findViewById(R.id.square_content);
            mBtnInput=(ImageView)view.findViewById(R.id.img_input_comment);
        }
    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
