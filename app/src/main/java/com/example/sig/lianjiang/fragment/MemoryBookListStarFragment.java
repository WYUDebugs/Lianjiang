package com.example.sig.lianjiang.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.R;
import com.example.sig.lianjiang.adapter.MemoryBookStarAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

public class MemoryBookListStarFragment extends Fragment implements TagCloudView.OnTagClickListener{

    private ImageView back;
    TagCloudView tcvTags;//标签云对象
    List<String> list = new ArrayList<>();//标签云数据的集合
    List<String> listClick = new ArrayList<>();//点击过的标签云的数据的集合
    private View view;
    private MemoryBookStarAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_memory_book_liststar, container, false);
        initView();
        return view;
    }
    public void initView() {
        //给集合添加数据
        for (int i = 0; i < 40; i++) {
            list.add("纪念册"+i);
        }
        tcvTags = (TagCloudView) view.findViewById(R.id.tcv_tags);
        //设置标签云的点击事件
        tcvTags.setOnTagClickListener(this);
        //给标签云设置适配器
        adapter = new MemoryBookStarAdapter(list);
        tcvTags.setAdapter(adapter);
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
            Toast.makeText(getActivity(), "你点击的是：" + list.get(position), Toast.LENGTH_SHORT).show();
        } else {
            //移除集合
            listClick.remove(list.get(position));
        }
        //Toast.makeText(getActivity(), "点击过的标签：" + listClick.toString(), Toast.LENGTH_SHORT).show();
    }


}
