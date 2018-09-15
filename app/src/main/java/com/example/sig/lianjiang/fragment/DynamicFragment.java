package com.example.sig.lianjiang.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dalong.zwlviewpager.ViewPager;
import com.example.sig.lianjiang.activity.LoginActivitysteup1;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.SquareActivity;
import com.example.sig.lianjiang.activity.WelcomeGuideActivity;
import com.example.sig.lianjiang.adapter.CardPagerAdapter;
import com.example.sig.lianjiang.bean.ShowItem;
import com.example.sig.lianjiang.view.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

public class DynamicFragment extends Fragment implements View.OnClickListener{
    private LinearLayout toSquare;
    private LinearLayout llMemoryBook;
    private LinearLayout llMemoryBook1;
    private List<ShowItem> mlist=new ArrayList<>();
    private int[] mImgs ={R.mipmap.show1,R.mipmap.show2,R.mipmap.show4};
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dynamic, container, false);

        initData();
        initView(view);
        initLintener();
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toSquare:
                Intent intent=new Intent(getActivity(), SquareActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_memory_book:
                Intent intentM=new Intent(getActivity(), MemoryBookListActivity.class);
                startActivity(intentM);
                break;
            case R.id.ll_memory_book1:
//                Intent intentM1=new Intent(getActivity(), MemoryBookList1Activity.class);
//                startActivity(intentM1);
//                Intent intentM1=new Intent(getActivity(), LoginActivitysteup1.class);
//                startActivity(intentM1);
                startActivity(new Intent(getContext(), WelcomeGuideActivity.class));
                break;
        }
    }
    /**
     * 初始化数据
     */
    private void initData() {
        for (int i=0;i<mImgs.length;i++){
            ShowItem item=new ShowItem();
            item.setImg(mImgs[i]);
            item.setTip("");
            mlist.add(item);
        }

    }

    private void initView(View view){
        toSquare=view.findViewById(R.id.toSquare);
        llMemoryBook=view.findViewById(R.id.ll_memory_book);
        llMemoryBook1=view.findViewById(R.id.ll_memory_book1);
        toSquare.setOnClickListener(this);
        llMemoryBook.setOnClickListener(this);
        llMemoryBook1.setOnClickListener(this);
        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter(getContext(),mlist);
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(4);
    }
    private void initLintener(){
        mCardShadowTransformer.setAlpha(0.5f,true);
        mCardShadowTransformer.setScale(0.1f,true);
    }
}
