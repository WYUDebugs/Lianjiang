package com.example.sig.lianjiang.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.MemoryCoverListAdapter;
import com.example.sig.lianjiang.bean.CoverPictureBean;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.example.sig.lianjiang.view.ObservableListView;

import java.util.ArrayList;
import java.util.List;

public class MemoryCoverUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgTopLeft;
    private TextView tvTopTitle;
    private ImageView imgTopRight;
    private TextView tvTopRight;
    private ListView recyclerView;
    private MemoryCoverListAdapter mAdapter;
    private List<CoverPictureBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_cover_update);

        initData();
        initView();

    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            CoverPictureBean coverPictureBean = new CoverPictureBean();
            if (i == 0) {
                coverPictureBean.setUrl1("coverAdd");
            } else {
                coverPictureBean.setUrl1("cover1" + i);
            }
            coverPictureBean.setUrl2("cover2" + i);
            mDataList.add(coverPictureBean);
        }
    }

    private void initView() {
        imgTopLeft = (ImageView) findViewById(R.id.img_top_left);
        tvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        imgTopRight = (ImageView) findViewById(R.id.img_top_right);
        tvTopRight = (TextView) findViewById(R.id.tv_top_right);
        recyclerView = (ListView) findViewById(R.id.recyclerview);
        mAdapter = new MemoryCoverListAdapter(this,mDataList);
        recyclerView.setAdapter(mAdapter);

        imgTopLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top_left:
                finish();
                break;

        }
    }

}
