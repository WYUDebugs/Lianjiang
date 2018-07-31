package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sig.lianjiang.fragment.MemoryBookListFragment;
import com.example.sig.lianjiang.fragment.MemoryBookListStarFragment;


public class MemoryBookListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private LinearLayout llSearchMemory;
    private ImageView imgToStar;
    private FloatingActionButton fabAddMBook;

    //Fragment Object
    private Fragment fg1;
    private Fragment fg2;
    private FrameLayout lyContent;
    private FragmentManager fManager;
    private boolean showWhichFrag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_book_list);
        fManager=getSupportFragmentManager();
        initView();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        if(fg1 == null){
            fg1 = new MemoryBookListFragment();
            fTransaction.add(R.id.ly_content,fg1);
        }else{
            fTransaction.show(fg1);
        }
        showWhichFrag = false;
        fTransaction.commit();
    }

    private void initView() {
        lyContent = (FrameLayout) findViewById(R.id.ly_content);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        fabAddMBook = (FloatingActionButton) findViewById(R.id.fab_add_memory);
        fabAddMBook.setOnClickListener(this);
        imgToStar = (ImageView) findViewById(R.id.img_to_star);
        imgToStar.setOnClickListener(this);

        llSearchMemory = (LinearLayout) findViewById(R.id.ll_search_memory);
        llSearchMemory.setOnClickListener(this);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.ll_search_memory:
                Intent intentSearch = new Intent(MemoryBookListActivity.this, MemoryBookSearchListActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.img_to_star:
                FragmentTransaction fTransaction = fManager.beginTransaction();
                hideAllFragment(fTransaction);
                if (showWhichFrag) {
                    if (fg1 == null) {
                        fg1 = new MemoryBookListFragment();
                        fTransaction.add(R.id.ly_content, fg1);
                    } else {
                        fTransaction.show(fg1);
                    }
                    showWhichFrag = false;
                } else {
                    if(fg2 == null){
                        fg2 = new MemoryBookListStarFragment();
                        fTransaction.add(R.id.ly_content,fg2);
                    }else{
                        fTransaction.show(fg2);
                    }
                    showWhichFrag = true;
                }
                fTransaction.commit();
                break;
            case R.id.fab_add_memory:
                //Toast.makeText(MemoryBookListActivity.this, "点击添加", Toast.LENGTH_SHORT).show();
                final Dialog dialog1;
                View diaView = View.inflate(MemoryBookListActivity.this, R.layout.dialogui_center_add, null);
                dialog1 = new Dialog(MemoryBookListActivity.this, R.style.dialog);
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
                        Intent intent = new Intent(MemoryBookListActivity.this, MemoryBookActivity.class);
                        startActivity(intent);
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.cancel();
                    }
                });
                break;

        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}