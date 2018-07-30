package com.example.sig.lianjiang.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sig.lianjiang.activity.MemoryBookListActivity;
import com.example.sig.lianjiang.activity.R;
import com.example.sig.lianjiang.activity.SquareActivity;

public class DynamicFragment extends Fragment implements View.OnClickListener{
    private LinearLayout toSquare;
    private LinearLayout llMemoryBook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dynamic, container, false);
        toSquare=view.findViewById(R.id.toSquare);
        llMemoryBook=view.findViewById(R.id.ll_memory_book);
        toSquare.setOnClickListener(this);
        llMemoryBook.setOnClickListener(this);
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
        }
    }

}
