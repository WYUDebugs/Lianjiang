package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.adapter.MemoryNineGridAdapter;
import com.example.sig.lianjiang.adapter.NineGridAdapter;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.view.ObservableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MemoryBookActivity extends AppCompatActivity implements ObservableListView.OnMeiTuanRefreshListener,View.OnClickListener,ObservableListView.ScrollViewListener{
    private TextView title;
    private ImageView back;
    private ImageView bgTop;
    private ImageView imgAddFriend;
    private ImageView imgAddSetting;

    private TextView tv_pull_to_refresh;
    private TextView topTitle;
    private int imageHeight=800;
    private LinearLayout textView;
    private FloatingActionButton fabAddMoment;
    private ObservableListView mListView;
    private MemoryNineGridAdapter mAdapter;
    private List<NineGridTestModel> mList = new ArrayList<>();
    private String[] mUrls = new String[]{"http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://img3.fengniao.com/forum/attachpics/537/165/21472986.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};
    private final static int REFRESH_COMPLETE = 0;
    private static final int UPDATE_TEXT_DONE=1;
    private static final int UPDATE_TEXT_STAR=2;
    private InterHandler mInterHandler = new InterHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_menory_book);
        tv_pull_to_refresh=(TextView) findViewById(R.id.tv_pull_to_refresh);
        topTitle=(TextView) findViewById(R.id.top_title);
        title=(TextView)findViewById(R.id.top_center);
        bgTop=(ImageView)findViewById(R.id.img_memory_cover_update);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        imgAddFriend = (ImageView) findViewById(R.id.img_add_friend);
        imgAddFriend.setOnClickListener(this);
        imgAddSetting = (ImageView) findViewById(R.id.img_add_setting);
        imgAddSetting.setOnClickListener(this);

        textView = (LinearLayout) findViewById(R.id.mytopbar_square);
        fabAddMoment = (FloatingActionButton) findViewById(R.id.fab_add_moment);
        fabAddMoment.setOnClickListener(this);
        //获得头部背景图的高度
//        int w1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        int h1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        bgTop.measure(w1, h1);
//        imageHeight =bgTop.getMeasuredHeight();
        //获得头部背景图的高度
        ViewGroup.LayoutParams para = bgTop.getLayoutParams();
        imageHeight = para.height;

        Log.d("wnf", "imageHeigt=========" + imageHeight);
        initImageLoader();
        initListData();
        initView();

        mListView.setOnMeiTuanRefreshListener(this);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    private void initListData() {
        NineGridTestModel model1 = new NineGridTestModel();
        model1.urlList.add(mUrls[0]);
        mList.add(model1);

        NineGridTestModel model2 = new NineGridTestModel();
        model2.urlList.add(mUrls[4]);
        mList.add(model2);
//
//        NineGridTestModel model3 = new NineGridTestModel();
//        model3.urlList.add(mUrls[2]);
//        mList.add(model3);

        NineGridTestModel model4 = new NineGridTestModel();
        for (int i = 0; i < mUrls.length; i++) {
            model4.urlList.add(mUrls[i]);
        }
        model4.isShowAll = false;
        mList.add(model4);

        NineGridTestModel model5 = new NineGridTestModel();
        for (int i = 0; i < mUrls.length; i++) {
            model5.urlList.add(mUrls[i]);
        }
        model5.isShowAll = false;//显示全部图片
        mList.add(model5);

        NineGridTestModel model6 = new NineGridTestModel();
        for (int i = 0; i < 9; i++) {
            model6.urlList.add(mUrls[i]);
        }
        mList.add(model6);

        NineGridTestModel model7 = new NineGridTestModel();
        for (int i = 3; i < 7; i++) {
            model7.urlList.add(mUrls[i]);
        }
        mList.add(model7);

        NineGridTestModel model8 = new NineGridTestModel();
        for (int i = 3; i < 6; i++) {
            model8.urlList.add(mUrls[i]);
        }
        mList.add(model8);
    }

    public void initView() {
        mListView = (ObservableListView) findViewById(R.id.lv_bbs);
        mAdapter = new MemoryNineGridAdapter(this);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
        mListView.setScrollViewListener(this);
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            private SparseArray recordSp = new SparseArray(0);
//            private int mCurrentfirstVisibleItem = 0;
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                mCurrentfirstVisibleItem = firstVisibleItem;
//                View firstView = view.getChildAt(0);
//                if (null != firstView) {
//                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
//                    if (null == itemRecord) {
//                        itemRecord = new ItemRecod();
//                    }
//                    itemRecord.height = firstView.getHeight();
//                    itemRecord.top = firstView.getTop();
//                    recordSp.append(firstVisibleItem, itemRecord);
//                    int h = getScrollY();//滚动距离
//                    //在此进行你需要的操作//TODO
//                    Log.e("123",Integer.toString(h));
//                    if (h <= 0) {
//                        textView.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或者美工提供
//                    } else if (h > 0 && h <= imageHeight-textView.getHeight()) {
//                        // 只是layout背景透明(仿知乎滑动效果)
//                        textView.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));
//                        title.setText("");
//                    } else {
//                        textView.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
//                        title.setText("广场");
//                    }
//
//                }
//            }
//
//            private int getScrollY() {
//                int height = 0;
//                for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
//                    ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
//                    height += itemRecod.height;
//                }
//                ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
//                if (null == itemRecod) {
//                    itemRecod = new ItemRecod();
//                }
//                return height - itemRecod.top;
//            }
//
//            class ItemRecod {
//                int height = 0;
//                int top = 0;
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_left:
                finish();
                break;
            case R.id.img_add_friend:
                Toast.makeText(MemoryBookActivity.this, "添加编辑好友", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_add_setting:
                Toast.makeText(MemoryBookActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_add_moment:
                Toast.makeText(MemoryBookActivity.this, "添加片段", Toast.LENGTH_SHORT).show();
                break;

        }
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
    public void onScroll(int h){
        if (h <= 0) {
            textView.setBackgroundColor(getResources().getColor(R.color.transparent));//AGB由相关工具获得，或者美工提供
            topTitle.setText("");
        } else if (h > 0 && h <= imageHeight-textView.getHeight()) {
            // 只是layout背景透明(仿知乎滑动效果)
            //textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
            textView.setBackgroundColor(getResources().getColor(R.color.transparent));
            topTitle.setText("");
        } else {
            textView.setBackgroundColor(getResources().getColor(R.color.white));
            topTitle.setText("福群相册2");
        }
    }

    private  class InterHandler extends Handler {
        private WeakReference<MemoryBookActivity> mActivity;
        public InterHandler(MemoryBookActivity activity){
            mActivity = new WeakReference<MemoryBookActivity>(activity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            MemoryBookActivity activity = mActivity.get();
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
