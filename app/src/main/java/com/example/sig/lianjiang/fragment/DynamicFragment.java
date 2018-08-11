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

import com.example.sig.lianjiang.activity.LoginActivitysteup1;
import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.activity.SquareActivity;

public class DynamicFragment extends Fragment implements View.OnClickListener{
    private LinearLayout toSquare;
    private LinearLayout llMemoryBook;
    private LinearLayout llMemoryBook1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dynamic, container, false);
        toSquare=view.findViewById(R.id.toSquare);
        llMemoryBook=view.findViewById(R.id.ll_memory_book);
        llMemoryBook1=view.findViewById(R.id.ll_memory_book1);
        toSquare.setOnClickListener(this);
        llMemoryBook.setOnClickListener(this);
        llMemoryBook1.setOnClickListener(this);
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
                Intent intentM1=new Intent(getActivity(), LoginActivitysteup1.class);
                startActivity(intentM1);
                break;
        }
    }

}
