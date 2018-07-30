package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.adapter.MemoryBookListAdapter;
import com.example.sig.lianjiang.view.ObservableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MemoryBookListActivity extends AppCompatActivity implements ObservableListView.OnMeiTuanRefreshListener,View.OnClickListener,ObservableListView.ScrollViewListener{

    private ImageView back;
    private LinearLayout llSearchMemory;
    private ImageView imgToStar;

    private TextView tv_pull_to_refresh;
    private int imageHeight=800;
    private FloatingActionButton fabAddMBook;
    private ObservableListView mListView;
    private MemoryBookListAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    private final static int REFRESH_COMPLETE = 0;
    private static final int UPDATE_TEXT_DONE=1;
    private static final int UPDATE_TEXT_STAR=2;
    private InterHandler mInterHandler = new InterHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_book_list);

        tv_pull_to_refresh=(TextView) findViewById(R.id.tv_pull_to_refresh);

//        initImageLoader();
//        initListData();
        initView();

        mListView.setOnMeiTuanRefreshListener(this);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    private void initListData() {

    }

    public void initView() {
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);

        imgToStar = (ImageView) findViewById(R.id.img_to_star);
        imgToStar.setOnClickListener(this);

        llSearchMemory = (LinearLayout) findViewById(R.id.ll_search_memory);
        llSearchMemory.setOnClickListener(this);
        fabAddMBook = (FloatingActionButton) findViewById(R.id.fab_add_memory);
        fabAddMBook.setOnClickListener(this);

        mListView = (ObservableListView) findViewById(R.id.lv_bbs);
        mList.add("header");
        for (int i = 0; i < 5; i++) {
            mList.add("福的朋友群纪念册" + i);
        }
        mAdapter = new MemoryBookListAdapter(this,mList);
        mListView.setAdapter(mAdapter);
        mListView.setScrollViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wnf", "position=========================="+position);
                if (position == 1) {

                } else {
                    Intent intent = new Intent(MemoryBookListActivity.this, MemoryBookActivity.class);
                    startActivity(intent);
                    //Toast.makeText(MemoryBookListActivity.this, "点击" + mList.get(position-1), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wnf", "position=========================="+position);
                if (position == 1) {

                } else {
                    Toast.makeText(MemoryBookListActivity.this, "长按" + mList.get(position-1), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mInterHandler.sendEmptyMessage(UPDATE_TEXT_DONE);
                    Thread.sleep(1000);
                    mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
                    mInterHandler.sendEmptyMessage(UPDATE_TEXT_STAR);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_left:
                finish();
                break;
            case R.id.ll_search_memory:
                Intent intentSearch = new Intent(MemoryBookListActivity.this, MemoryBookSearchListActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.img_to_star:
                Intent intentStar = new Intent(MemoryBookListActivity.this, MemoryBookStarListActivity.class);
                startActivity(intentStar);
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
    public void onScroll(int h){
//        if (h <= 0) {
//            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
//        } else if (h > 0 && h <= imageHeight-textView.getHeight()) {
//            // 只是layout背景透明(仿知乎滑动效果)
//            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
//            title.setText("");
//        } else {
//            textView.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
//            title.setText("广场");
//        }
    }

    private  class InterHandler extends Handler {
        private WeakReference<MemoryBookListActivity> mActivity;
        public InterHandler(MemoryBookListActivity activity){
            mActivity = new WeakReference<MemoryBookListActivity>(activity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            MemoryBookListActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        activity.mListView.setOnRefreshComplete();
                        activity.mAdapter.notifyDataSetChanged();
                        activity.mListView.setSelection(0);
                        break;
                    case UPDATE_TEXT_DONE:
                        tv_pull_to_refresh.setText("刷新完成");
                        mListView.fin();
                        break;
                    case UPDATE_TEXT_STAR:
                        tv_pull_to_refresh.setText("下拉刷新");
                        break;
                }
            }else{
                super.handleMessage(msg);
            }
        }
    }
}
