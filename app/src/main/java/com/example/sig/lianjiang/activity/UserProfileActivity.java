package com.example.sig.lianjiang.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.TimeLineAdapter;
import com.example.sig.lianjiang.bean.PublicListResultDto;
import com.example.sig.lianjiang.bean.PublishDto;
import com.example.sig.lianjiang.bean.ResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.TimeLineModel;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.RecycleViewUtils;
import com.example.sig.lianjiang.utils.StatusBar1Util;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    private  String userId;
    private UserResultDto resultDto;
    private int mOffset = 0;
    private int mScrollY = 0;
    private RecyclerView mRecycler;
    private CircleImageView head;
    private TextView name;
    private RecycleViewUtils recycleViewUtils;
    private int mDistance = 0;
    private Toolbar toolbar;
    private View buttonBar;
    private View parallax;
    private PublicListResultDto publicListResultDto;
    private TimeLineAdapter mAdapter;
    private List<TimeLineModel> mList = new ArrayList<>();
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int color = Color.argb((int) 255, 51,170,255);
        private int h = DensityUtil.dp2px(170);
        private int lastScrollY = 0;
        //dy:每一次竖直滑动增量 向下为正 向上为负
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mDistance=recycleViewUtils.getScrollY();
            Log.d("zxd",Integer.toString(mDistance));
//            if(lastScrollY<h){
//                mDistance = Math.min(h, mDistance);
//                mScrollY = mDistance > h ? h : mDistance;
//                buttonBar.setAlpha(1f * mScrollY / h);
//                toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
//                parallax.setTranslationY(mOffset - mScrollY);
//            }
//            lastScrollY = mDistance;
            buttonBar.setAlpha(1f * mDistance / h);
//            toolbar.setBackgroundColor(((255 * mDistance / h) << 24) | color);
            if(mDistance<h){
                toolbar.setBackgroundColor(Color.argb((int) (255 * mDistance / h), 51,170,255));
            }else{
                toolbar.setBackgroundColor(Color.argb((int) 255, 51,170,255));
            }
            parallax.setTranslationY(mOffset - mDistance);
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        theme();
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        userId=intent.getStringExtra("username");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //状态栏透明和间距处理
        StatusBar1Util.immersive(this);
        StatusBar1Util.setPaddingSmart(this, toolbar);
        buttonBar = findViewById(R.id.buttonBarLayout);
        parallax = findViewById(R.id.parallax);
        head=(CircleImageView)findViewById(R.id.toolbar_avatar);
        name=(TextView)findViewById(R.id.name);
        getHead();
        final RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);


        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(3000);
//            }
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getpublishListPost(userId);
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }


            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });

        buttonBar.setAlpha(0);
        toolbar.setBackgroundColor(0);
        initRecycler();
        initImageLoader();
        refreshLayout.autoRefresh();
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {



        }
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    private void theme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initRecycler() {
        mRecycler=(RecyclerView)findViewById(R.id.time_line_recycler) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new TimeLineAdapter(mList,this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_memory_book_list_header,null);//假的头部
        mAdapter.setHeaderView(headerView);

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);
        recycleViewUtils = new RecycleViewUtils().with(mRecycler);
        mRecycler.addOnScrollListener(mOnScrollListener);
    }

    private void initListData(final List<PublishDto> data) {
        mList.clear();
        for(int i=0;i<data.size();i++){
            PublishDto temp=data.get(i);
            List<String> urlList = new ArrayList<>();
            if(temp.getImageList()!=null){
                for (int j = 0; j < temp.getImageList().size()&&j<1; j++) {
                    String publishImage=temp.getImageList().get(j).getPath();
                    urlList.add(APPConfig.img_url+publishImage);
                }
            }
            String content=temp.getContent();
            String publishId=Integer.toString(temp.getId());
            String address=temp.getAddress();
            String time=temp.getTime();
            String userId=Integer.toString(temp.getPublisher());
            boolean flag;
            if(userId.equals(EMClient.getInstance().getCurrentUser())){
                flag=true;
            }else {
                flag=false;
            }
            TimeLineModel model = new TimeLineModel(content,urlList,publishId,time,flag);
            mList.add(model);

        }
    }





    public void getHead() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", userId);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findUserById, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(UserProfileActivity.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Picasso.with(UserProfileActivity.this).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            name.setText(resultDto.getData().getName());

                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(UserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }

    public void getpublishListPost(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Integer a=null;
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("publisher", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.showSomeonePost, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            publicListResultDto = OkHttpUtils.getObjectFromJson(response.toString(), PublicListResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            publicListResultDto = PublicListResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(publicListResultDto.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
                            initListData(publicListResultDto.getData());
                            mAdapter.notifyDataSetChanged();
                        }else {
                            Log.e("zxd","动态为空");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                    }
                }, list);
            }

        }).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        getHead();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getHead();
    }
}
