package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.MemoryNineGridAdapter;
import com.example.sig.lianjiang.adapter.NineGridAdapter;
import com.example.sig.lianjiang.bean.MemoryBook;
import com.example.sig.lianjiang.bean.MemoryBookListResult;
import com.example.sig.lianjiang.bean.MemoryBookResult;
import com.example.sig.lianjiang.bean.Moment;
import com.example.sig.lianjiang.bean.MomentListResult;
import com.example.sig.lianjiang.bean.PublicListResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.MemoryNineGridModel;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.RecycleViewUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;
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

public class MemoryBookActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView bgTop;
    private ImageView imgAddFriend;
    private ImageView imgAddSetting;

    private TextView tv_pull_to_refresh;
    private TextView topTitle;
    private int imageHeight=800;
    private RelativeLayout llTop;
    private FloatingActionButton fabAddMoment;
    private MemoryNineGridAdapter mAdapter;
    private List<MemoryNineGridModel> mList = new ArrayList<>();
    private AnimRFRecyclerView mRecyclerView;

    private View headerView;
    private View footerView;
    private Handler mHandler = new Handler();
    int mDistance = 0;
    private RecycleViewUtils recycleViewUtils;
    private MomentListResult momentListResult;
    private MemoryBookResult memoryBookResult;
    private String momtent;
    private ImageView head;
    private ImageView cover;
    private TextView time;
    private TextView friendNum;
    private TextView momentNum;
    private TextView title;
    private TextView name;
    private MemoryBookListResult memoryBookListResult;


    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        //dy:每一次竖直滑动增量 向下为正 向上为负
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mDistance=recycleViewUtils.getScrollY();
            Log.d("zxd",Integer.toString(mDistance));
            if (mDistance <= 0) {
                llTop.setBackgroundColor(getResources().getColor(R.color.transparent));//AGB由相关工具获得，或者美工提供
                topTitle.setText("");
            } else if (mDistance > 0 && mDistance <= 500 -llTop.getHeight()
                    ) {
                // 只是layout背景透明(仿知乎滑动效果)
            llTop.setBackgroundColor(getResources().getColor(R.color.transparent));
            topTitle.setText("");
            } else {
            llTop.setBackgroundColor(getResources().getColor(R.color.white));
            topTitle.setText("福的群纪念册");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_menory_book);
        momtent=getIntent().getStringExtra("memoryBookId");
        tv_pull_to_refresh=(TextView) findViewById(R.id.tv_pull_to_refresh);
        topTitle=(TextView) findViewById(R.id.top_title);
//        bgTop=(ImageView)findViewById(R.id.img_memory_cover_update);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        imgAddFriend = (ImageView) findViewById(R.id.img_add_friend);
        imgAddFriend.setOnClickListener(this);
        imgAddSetting = (ImageView) findViewById(R.id.img_add_setting);
        imgAddSetting.setOnClickListener(this);

        llTop = (RelativeLayout) findViewById(R.id.mytopbar_square);
        llTop.setOnClickListener(this);
        fabAddMoment = (FloatingActionButton) findViewById(R.id.fab_add_moment);
        fabAddMoment.setOnClickListener(this);
        initImageLoader();
        initView();

    }

    private void theme(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.StatusBarLightMode(this);  //把标题栏字体变黑色
    }
    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    private void initListData(List<Moment> data) {
        mList.clear();
        for(int i=0;i<data.size();i++){
            Moment moment=data.get(i);
            String content=moment.getContent();
            List<String> urlList = new ArrayList<>();
            for (int j = 0; j < moment.getImageList().size(); j++) {
                String publishImage=moment.getImageList().get(j).getPath();
                if(publishImage!=null){
                    urlList.add(APPConfig.test_image_url+publishImage);
                }
            }
            String momentId=Integer.toString(moment.getId());
            String head=moment.getUser().getHeadimage();
            String name=moment.getUser().getName();
            String time=moment.getLocation_time();
            String userId=Integer.toString(moment.getSender());
            MemoryNineGridModel memoryNineGridModel=new MemoryNineGridModel(content,urlList,false,momentId,head,name,time,userId);
            mList.add(memoryNineGridModel);
        }
    }

    public void initView() {
        mRecyclerView = (AnimRFRecyclerView) findViewById(R.id.lv_bbs);
        // 头部
        headerView = LayoutInflater.from(MemoryBookActivity.this).inflate(R.layout.layout_moment_header, null);
        head=(ImageView) headerView.findViewById(R.id.iv_head);
        cover=(ImageView) headerView.findViewById(R.id.img_memory_cover_update);
        time=(TextView) headerView.findViewById(R.id.tv_memory_addtime);
        friendNum=(TextView) headerView.findViewById(R.id.tv_memory_persion_num);
        momentNum=(TextView) headerView.findViewById(R.id.tv_memory_moment_num);
        title=(TextView) headerView.findViewById(R.id.tv_title);
        name=(TextView) headerView.findViewById(R.id.tv_name);
        getMomentBookPost(momtent,cover,title,head,name,time,friendNum,momentNum);
        // 脚部
        footerView = LayoutInflater.from(MemoryBookActivity.this).inflate(R.layout.footer_view, null);
        // 使用重写后的线性布局管理器
        AnimRFLinearLayoutManager manager = new AnimRFLinearLayoutManager(MemoryBookActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MemoryBookActivity.this, manager.getOrientation(), true));
//            // 添加头部和脚部，如果不添加就使用默认的头部和脚部
        mRecyclerView.addHeaderView(headerView);
//            // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
        mRecyclerView.setScaleRatio(1.7f);
//            // 设置下拉时拉伸的图片，不设置就使用默认的
        mRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.img_memory_cover_update));
        mRecyclerView.addFootView(footerView);
        // 设置刷新动画的颜色
        mRecyclerView.setColor(Color.argb((int) 255, 56,207,176), Color.argb((int) 255, 57, 58, 62));
        // 设置头部恢复动画的执行时间，默认500毫秒
        mRecyclerView.setHeaderImageDurationMillis(300);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        mRecyclerView.setHeaderImageMinAlpha(1.0f);
        mAdapter = new MemoryNineGridAdapter(this,mList);
        mRecyclerView.setAdapter(mAdapter);
        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        mRecyclerView.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                new Thread(new MemoryBookActivity.MyRunnable(true)).start();
            }

            @Override
            public void onLoadMore() {
                new Thread(new MemoryBookActivity.MyRunnable(false)).start();
            }
        });

        // 刷新
        mRecyclerView.setRefresh(true);
        recycleViewUtils = new RecycleViewUtils().with(mRecyclerView);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
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
//                        getpublishListPost(EMClient.getInstance().getCurrentUser());
                        getMomentListPost(momtent);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_left:
                finish();
                break;
            case R.id.img_add_friend:
                Intent intent = new Intent(MemoryBookActivity.this, MemoryPickContactsActivity.class);
                intent.putExtra("memoryBookId",momtent);
                startActivity(intent);
//                Toast.makeText(MemoryBookActivity.this, "添加编辑好友", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_add_setting:
                //Toast.makeText(MemoryBookActivity.this, "设置", Toast.LENGTH_SHORT).show();
                clickSetting();
                break;
            case R.id.fab_add_moment:
                //Toast.makeText(MemoryBookActivity.this, "添加片段", Toast.LENGTH_SHORT).show();
                Intent intentAddM = new Intent(MemoryBookActivity.this, MomentAddActivity.class);
                intentAddM.putExtra("memoryBookId",momtent);
                startActivity(intentAddM);
//                finish();
                break;

        }
    }

    public void clickSetting() {
        View diaView= View.inflate(MemoryBookActivity.this, R.layout.dialogui_footer_setting, null);
        final Dialog dialog=new Dialog(MemoryBookActivity.this,R.style.dialogfooter);
        diaView.setMinimumWidth(R.dimen.width);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(diaView);
        dialog.show();
        ImageView imgCancel;
        LinearLayout llMemorySettingPeople;
        LinearLayout llMemorySettingName;
        LinearLayout llMemorySettingCover;
        LinearLayout llMemorySettingDel;

        imgCancel = (ImageView) diaView.findViewById(R.id.img_cancel);
        llMemorySettingPeople = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_people);
        llMemorySettingName = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_name);
        llMemorySettingCover = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_cover);
        llMemorySettingDel = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_del);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        llMemorySettingPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryBookActivity.this, MemoryDeleteContactsActivity.class);
                intent.putExtra("memoryBookId",momtent);
                startActivity(intent);
                dialog.cancel();
            }
        });
        llMemorySettingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                updateMemoryName();
            }
        });
        llMemorySettingCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(MemoryBookActivity.this, MemoryCoverUpdateActivity.class);
                intent.putExtra("memoryBookId",momtent);
                startActivity(intent);
            }
        });
        llMemorySettingDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                clickDelMemory();
            }
        });
    }

    public void clickDelMemory() {
        View diaView1= View.inflate(MemoryBookActivity.this, R.layout.dialogui_center_sure_or_cancel, null);
        final Dialog dialog1=new Dialog(MemoryBookActivity.this,R.style.dialog);
        dialog1.setContentView(diaView1);
        dialog1.show();
        TextView textInfo1 = (TextView) diaView1.findViewById(R.id.tv_info);
        TextView btSure1 = (TextView) diaView1.findViewById(R.id.tv_sure);
        TextView btCancel1 = (TextView) diaView1.findViewById(R.id.tv_cancel);
        textInfo1.setText("是否解散该纪念册？");
        btSure1.setTextColor(this.getResources().getColor(R.color.grey));
        btCancel1.setTextColor(this.getResources().getColor(R.color.red));
        btSure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MemoryBookActivity.this, "退出", Toast.LENGTH_SHORT).show();
                dialog1.cancel();
            }
        });
        btCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }

    public void updateMemoryName() {
        final Dialog dialog1;
        View diaView = View.inflate(MemoryBookActivity.this, R.layout.dialogui_center_add, null);
        dialog1 = new Dialog(MemoryBookActivity.this, R.style.dialog);
        dialog1.setContentView(diaView);
        dialog1.show();
        TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
        TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
        TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
        tvInfo.setText("请输入更改后的纪念册名称");
        final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMemoryBookTitle(momtent,etInput.getText().toString().trim());
                dialog1.cancel();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }



//    @Override
//    public void onScroll(int h){
//        if (h <= 0) {
//            llTop.setBackgroundColor(getResources().getColor(R.color.transparent));//AGB由相关工具获得，或者美工提供
//            topTitle.setText("");
//        } else if (h > 0 && h <= imageHeight-llTop.getHeight()) {
//            // 只是layout背景透明(仿知乎滑动效果)
//            //textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
//            llTop.setBackgroundColor(getResources().getColor(R.color.transparent));
//            topTitle.setText("");
//        } else {
//            llTop.setBackgroundColor(getResources().getColor(R.color.white));
//            topTitle.setText("福的群纪念册");
//        }
//    }

    public void getMomentListPost(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Integer a=null;
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("bId", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.momentList, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            momentListResult = OkHttpUtils.getObjectFromJson(response.toString(), MomentListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            momentListResult = MomentListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(momentListResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
                            initListData(momentListResult.getData());
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

    public void getMomentBookPost(final String id,final ImageView cover,final TextView title,final ImageView head,final TextView name,final TextView time,final TextView friendNum,final TextView momentNum) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Integer a=null;
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("bId", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findBookDetailed, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookResult = MemoryBookResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
                            MemoryBook memoryBook=memoryBookResult.getData();
                            Picasso.with(MemoryBookActivity.this).load(APPConfig.test_image_url + memoryBook.getCover())
                                    .placeholder(R.mipmap.memory1).error(R.mipmap.memory1).into(cover);
                            Picasso.with(MemoryBookActivity.this).load(APPConfig.img_url + memoryBook.getUser().getHeadimage())
                                    .placeholder(R.mipmap.icon_head).error(R.mipmap.icon_head).into(head);
                            title.setText(memoryBook.getTitle());
                            name.setText(memoryBook.getUser().getName());
                            time.setText(memoryBook.getCreatTime());
                            friendNum.setText(Integer.toString(memoryBook.getFriendCount()));
                            momentNum.setText(Integer.toString(memoryBook.getMomentCount()));

                        }else {

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

    public void changeMemoryBookTitle(final String id,final String title) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        OkHttpUtils.Param titleParam = new OkHttpUtils.Param("title", title);
        list.add(idParam);
        list.add(titleParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changeMemoryBookTitle, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookListResult = MemoryBookListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookListResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(MemoryBookActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            changeTitle(title);
                        }else {
                            Toast.makeText(MemoryBookActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
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

    public void deleteMemoryBook(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.deleteMemoryBook, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookListResult = MemoryBookListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookListResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(MemoryBookActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(MemoryBookActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
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
    private void changeTitle(String title1){
        title.setText(title1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMomentBookPost(momtent,cover,title,head,name,time,friendNum,momentNum);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        getHead();
        getMomentBookPost(momtent,cover,title,head,name,time,friendNum,momentNum);
    }
}
