package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.adapter.CursorTagsAdapter;
import com.example.sig.lianjiang.adapter.MemoryBookListAdapter;
import com.example.sig.lianjiang.adapter.MemoryBookStarAdapter;
import com.example.sig.lianjiang.view.ObservableListView;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MemoryBookStarListActivity extends AppCompatActivity implements TagCloudView.OnTagClickListener{

    private ImageView back;
    TagCloudView tcvTags;//标签云对象
    List<String> list = new ArrayList<>();//标签云数据的集合
    List<String> listClick = new ArrayList<>();//点击过的标签云的数据的集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorybook_starlist);
        initView();
        //给集合添加数据
        for (int i = 0; i < 40; i++) {
            list.add("纪念册"+i);
        }

        tcvTags = (TagCloudView) findViewById(R.id.tcv_tags);
        //设置标签云的点击事件
        tcvTags.setOnTagClickListener(this);
        //给标签云设置适配器
        MemoryBookStarAdapter adapter = new MemoryBookStarAdapter(list);
        tcvTags.setAdapter(adapter);
    }
    public void initView() {
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    /**
     * 点击标签是回调的方法
     */
    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
        view.setSelected(!view.isSelected());//设置标签的选择状态
        if (view.isSelected()) {
            //加入集合
            listClick.add(list.get(position));
            Toast.makeText(MemoryBookStarListActivity.this, "你点击的是：" + list.get(position), Toast.LENGTH_SHORT).show();
        } else {
            //移除集合
            listClick.remove(list.get(position));
        }
        //Toast.makeText(getActivity(), "点击过的标签：" + listClick.toString(), Toast.LENGTH_SHORT).show();
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.top_left:
//                finish();
//                break;
//
//
//        }
//    }


}
