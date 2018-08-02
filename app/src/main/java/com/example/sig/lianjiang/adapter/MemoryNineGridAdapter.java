package com.example.sig.lianjiang.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.activity.MemoryCoverUpdateActivity;
import com.example.sig.lianjiang.activity.R;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.view.NineGridTestLayout;

import java.util.List;

/**
 * Created by sig on 2018/7/26.
 */

public class MemoryNineGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    public MemoryNineGridAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.layout_moment_header, parent, false);
            convertView.findViewById(R.id.img_memory_cover_update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MemoryCoverUpdateActivity.class);
                    mContext.startActivity(intent);
                    //Toast.makeText(mContext, "更新封面", Toast.LENGTH_SHORT).show();
                }
            });
            convertView.findViewById(R.id.img_memory_name_update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog1;
                    View diaView = View.inflate(mContext, R.layout.dialogui_center_add, null);
                    dialog1 = new Dialog(mContext, R.style.dialog);
                    dialog1.setContentView(diaView);
                    dialog1.show();
                    TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
                    TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
                    TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
                    tvInfo.setText("请输入更改后的纪念册名称");
                    final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
                    tvSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.cancel();
                        }
                    });
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.cancel();
                        }
                    });
                }
            });
        }else {
            if (convertView == null || convertView.getTag() == null) {
                convertView = inflater.inflate(R.layout.item_moment, parent, false);
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
