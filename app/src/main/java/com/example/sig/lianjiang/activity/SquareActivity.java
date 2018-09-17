package com.example.sig.lianjiang.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.NineGridAdapter;
import com.example.sig.lianjiang.adapter.SquareAdapter;
import com.example.sig.lianjiang.bean.PublicListResultDto;
import com.example.sig.lianjiang.bean.Publish;
import com.example.sig.lianjiang.bean.PublishDto;
import com.example.sig.lianjiang.bean.PublishImage;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.Comment;
import com.example.sig.lianjiang.model.CommentUser;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.utils.CommentFun;
import com.example.sig.lianjiang.utils.CustomTagHandler;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.RecycleViewUtils;
import com.example.sig.lianjiang.view.CircleImageView;
import com.example.sig.lianjiang.view.ObservableListView;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.decoration.DividerItemDecoration;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SquareActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView titleText;
    private ImageView back;
    private TextView tv_pull_to_refresh;
    private int imageHeight=800;
    private LinearLayout title;
    public static CommentUser sUser = new CommentUser(Integer.parseInt(EMClient.getInstance().getCurrentUser()), "未知用户"); // 当前登录用户
    private AnimRFRecyclerView mRecyclerView;
    private SquareAdapter mAdapter;
    private List<NineGridTestModel> mList = new ArrayList<>();
    private UserResultDto resultDto;
    private PublicListResultDto publicListResultDto;
    private View headerView;
    private View footerView;
    private Handler mHandler = new Handler();
    int mDistance = 0;
    private RecycleViewUtils recycleViewUtils;
    private CircleImageView head;
    private boolean progressShow;
    //滑动监听事件
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        //dy:每一次竖直滑动增量 向下为正 向上为负
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
//            mDistance += dy;
//            float percent = mDistance * 1f / maxDistance;//百分比
//            int alpha = (int) (percent * 255);
////            int argb = Color.argb(alpha, 57, 174, 255);
            mDistance=recycleViewUtils.getScrollY();
            Log.d("zxd",Integer.toString(mDistance));
            if (mDistance <= 0) {
                title.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
            } else if (mDistance > 0 && mDistance <= 800 -title.getHeight()
                    ) {
                // 只是layout背景透明(仿知乎滑动效果)
                title.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
                titleText.setText("");
            } else {
                title.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
                titleText.setText("动态");
            }
//            setSystemBarAlpha(alpha);
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_left:
                finish();
                break;

        }
    }

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
        setContentView(R.layout.activity_square);
        tv_pull_to_refresh=(TextView) findViewById(R.id.tv_pull_to_refresh);
        titleText=(TextView)findViewById(R.id.top_center);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        title = (LinearLayout) findViewById(R.id.mytopbar_square);
//        setSystemBarAlpha(0);
        initImageLoader();
//        getpublishListPost(EMClient.getInstance().getCurrentUser());
        initView();
        getUserPost(EMClient.getInstance().getCurrentUser());


    }




    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    private void initListData(final List<PublishDto> data) {
        mList.clear();
        for(int i=0;i<data.size();i++){
//            ArrayList<Comment> comments = new ArrayList<Comment>();
//            List<String> urlList = new ArrayList<>();
//            comments.add(new Comment(new CommentUser(i + 2, "用户" + i), "评论" + i, null));
//            comments.add(new Comment(new CommentUser(i + 100, "用户" + (i + 100)), "评论" + i, new CommentUser(i + 200, "用户" + (i + 200))));
//            comments.add(new Comment(new CommentUser(i + 200, "用户" + (i + 200)), "评论" + i, null));
//            comments.add(new Comment(new CommentUser(i + 300, "用户" + (i + 300)), "评论" + i, null));
//            for (int j = 0; j < mUrls.length; j++) {
//                urlList.add(mUrls[j]);
//            }
//            NineGridTestModel model = new NineGridTestModel("假装有内容。。。。",urlList,false,comments);
//            mList.add(model);
            PublishDto temp=data.get(i);
            ArrayList<Comment> comments = new ArrayList<Comment>();
            List<String> urlList = new ArrayList<>();
//            comments.add(new Comment(new CommentUser(i + 2, "用户" + i), "评论" + i, null));
//            comments.add(new Comment(new CommentUser(i + 100, "用户" + (i + 100)), "评论" + i, new CommentUser(i + 200, "用户" + (i + 200))));
//            comments.add(new Comment(new CommentUser(i + 200, "用户" + (i + 200)), "评论" + i, null));
//            comments.add(new Comment(new CommentUser(i + 300, "用户" + (i + 300)), "评论" + i, null));
            for (int j = 0; j < temp.getImageList().size(); j++) {
                String publishImage=temp.getImageList().get(j).getPath();
                urlList.add(APPConfig.test_image_url+publishImage);
            }
            String content=temp.getContent();
            NineGridTestModel model = new NineGridTestModel(content,urlList,false,comments);
            mList.add(model);

        }
    }

    public void initView() {
        mRecyclerView = (AnimRFRecyclerView) findViewById(R.id.lv_bbs);
        // 头部
        headerView = LayoutInflater.from(SquareActivity.this).inflate(R.layout.header_view, null);
        head=(CircleImageView) headerView.findViewById(R.id.head);
        // 脚部
        footerView = LayoutInflater.from(SquareActivity.this).inflate(R.layout.footer_view, null);
        // 使用重写后的线性布局管理器
        AnimRFLinearLayoutManager manager = new AnimRFLinearLayoutManager(SquareActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SquareActivity.this, manager.getOrientation(), true));
//            // 添加头部和脚部，如果不添加就使用默认的头部和脚部
            mRecyclerView.addHeaderView(headerView);
//            // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
            mRecyclerView.setScaleRatio(1.7f);
//            // 设置下拉时拉伸的图片，不设置就使用默认的
            mRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
            mRecyclerView.addFootView(footerView);
        // 设置刷新动画的颜色
        mRecyclerView.setColor(Color.argb((int) 255, 56,207,176), Color.argb((int) 255, 57, 58, 62));
        // 设置头部恢复动画的执行时间，默认500毫秒
        mRecyclerView.setHeaderImageDurationMillis(300);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        mRecyclerView.setHeaderImageMinAlpha(1.0f);
        mAdapter = new SquareAdapter(this,mList,new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
            @Override
            public void onCommentatorClick(View view, CommentUser commentator) {
                Toast.makeText(getApplicationContext(), commentator.mName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiverClick(View view, CommentUser receiver) {
                Toast.makeText(getApplicationContext(), receiver.mName, Toast.LENGTH_SHORT).show();
            }

            // 点击评论内容，弹出输入框回复评论
            @Override
            public void onContentClick(View view, CommentUser commentator, CommentUser receiver) {
                if (commentator != null && commentator.mId == sUser.mId) { // 不能回复自己的评论
                    return;
                }
                inputComment(view, commentator);
            }
        }));
        mRecyclerView.setAdapter(mAdapter);
        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        mRecyclerView.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                new Thread(new MyRunnable(true)).start();
            }

            @Override
            public void onLoadMore() {
                new Thread(new MyRunnable(false)).start();
            }
        });

        // 刷新
        mRecyclerView.setRefresh(true);
        recycleViewUtils = new RecycleViewUtils().with(mRecyclerView);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
//        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"click "+position,Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    class MyRunnable implements Runnable {

        boolean isRefresh;

        public MyRunnable(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        public void run() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRefresh) {
                        getpublishListPost(EMClient.getInstance().getCurrentUser());
                        refreshComplate();
                        // 刷新完成后调用，必须在UI线程中
                        mRecyclerView.refreshComplate();
                    } else {
//                        addData();
                        loadMoreComplate();
                        // 加载更多完成后调用，必须在UI线程中
                        mRecyclerView.loadMoreComplate();
                    }
                }
            }, 2000);
        }
    }
    public void refreshComplate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void loadMoreComplate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
//    @Override
//    public void onScroll(int h){
//        if (h <= 0) {
//            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
//        } else if (h > 0 && h <= imageHeight -textView.getHeight()
//                ) {
//            // 只是layout背景透明(仿知乎滑动效果)
//            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
//            title.setText("");
//        } else {
//            textView.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
//            title.setText("动态");
//        }
//    }
    public void getUserPost(final String id) {
//        progressShow = true;
//        final ProgressDialog pd= new ProgressDialog(SquareActivity.this);
//        pd.setCanceledOnTouchOutside(false);
//        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                progressShow = false;
//            }
//        });
//        pd.setMessage("正在获取用户信息......");
//        pd.show();
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
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
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            sUser.setmName(resultDto.getData().getName());
                            Picasso.with(SquareActivity.this).load(APPConfig.img_url + resultDto.getData().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
//                            if (!SquareActivity.this.isFinishing() && pd.isShowing()) {
//                                pd.dismiss();
//                            }
                        }else {
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    pd.dismiss();
//                                    Toast.makeText(SquareActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
//                                }
//                            });
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
    //点击点赞
    public void clickGood(View view){

    }
    //点击评论
    public void inputComment(final View v) {
        inputComment(v, null);
    }

    /**
     * 弹出评论对话框
     * @param v
     * @param receiver
     */
    public void inputComment(final View v, CommentUser receiver) {

        CommentFun.inputComment(SquareActivity.this, mRecyclerView, v, receiver, new CommentFun.InputCommentListener() {
            @Override
            public void onCommitComment() {
//                mAdapter.notifyDataSetChanged();
                Log.d("对话框","12345");
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void getpublishListPost(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("userId", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.publishList, new OkHttpUtils.ResultCallback() {
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
}
