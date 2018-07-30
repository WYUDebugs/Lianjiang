package com.example.sig.lianjiang.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.activity.R;

import java.util.List;

/**
 * Created by sig on 2018/7/26.
 */

public class MemoryBookListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    protected LayoutInflater inflater;

    public MemoryBookListAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        return mList.size();
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
        if (position == 0) {
            convertView = inflater.inflate(R.layout.layout_memory_book_tv, parent, false);
            convertView.findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
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
                    final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
                    tvSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.cancel();
                            Intent intent = new Intent(mContext, MemoryBookActivity.class);
                            mContext.startActivity(intent);
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

        } else {
            if (convertView == null || convertView.getTag() == null) {
                convertView = inflater.inflate(R.layout.item_memory_book, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //图片圆角
//            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.memory1);
//            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(),mBitmap);
//            circularBitmapDrawable.setCornerRadius(20); //设置圆角弧度
//            holder.imgCover.setImageDrawable(circularBitmapDrawable);
            holder.tvMemoryTitle.setText(mList.get(position));
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imgCover;
        TextView tvMemoryTitle;
        TextView tvMemoryPersionNum;
        TextView tvMemoryMomentNum;

        public ViewHolder(View view) {
            imgCover = view.findViewById(R.id.img_cover);
            tvMemoryTitle = (TextView) view.findViewById(R.id.tv_memory_title);
            tvMemoryPersionNum = (TextView) view.findViewById(R.id.tv_memory_persion_num);
            tvMemoryMomentNum = (TextView) view.findViewById(R.id.tv_memory_moment_num);
        }
    }
}
