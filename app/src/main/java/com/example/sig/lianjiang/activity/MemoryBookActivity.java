package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.example.sig.lianjiang.model.MemoryNineGridModel;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.utils.StatusBarUtil;
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
    private RelativeLayout llTop;
    private FloatingActionButton fabAddMoment;
    private ObservableListView mListView;
    private MemoryNineGridAdapter mAdapter;
    private List<MemoryNineGridModel> mList = new ArrayList<>();
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
        StatusBarUtil.StatusBarLightMode(this);  //把标题栏字体变黑色
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

        llTop = (RelativeLayout) findViewById(R.id.mytopbar_square);
        llTop.setOnClickListener(this);
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
        MemoryNineGridModel model1 = new MemoryNineGridModel();
        model1.urlList.add(mUrls[0]);
        mList.add(model1);

        MemoryNineGridModel model2 = new MemoryNineGridModel();
        model2.urlList.add(mUrls[4]);
        mList.add(model2);
//
//        NineGridTestModel model3 = new NineGridTestModel();
//        model3.urlList.add(mUrls[2]);
//        mList.add(model3);

        MemoryNineGridModel model4 = new MemoryNineGridModel();
        for (int i = 0; i < mUrls.length; i++) {
            model4.urlList.add(mUrls[i]);
        }
        model4.isShowAll = false;
        mList.add(model4);

        MemoryNineGridModel model5 = new MemoryNineGridModel();
        for (int i = 0; i < mUrls.length; i++) {
            model5.urlList.add(mUrls[i]);
        }
        model5.isShowAll = false;//显示全部图片
        mList.add(model5);

        MemoryNineGridModel model6 = new MemoryNineGridModel();
        for (int i = 0; i < 9; i++) {
            model6.urlList.add(mUrls[i]);
        }
        mList.add(model6);

        MemoryNineGridModel model7 = new MemoryNineGridModel();
        for (int i = 3; i < 7; i++) {
            model7.urlList.add(mUrls[i]);
        }
        mList.add(model7);

        MemoryNineGridModel model8 = new MemoryNineGridModel();
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wnf", "position=========================="+position);
                if (position == 1) {

                } else {
                    Intent intent = new Intent(MemoryBookActivity.this, MomentInfoActivity.class);
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
                    Toast.makeText(MemoryBookActivity.this, "长按" + position, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
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
                //Toast.makeText(MemoryBookActivity.this, "设置", Toast.LENGTH_SHORT).show();
                clickSetting();
                break;
            case R.id.fab_add_moment:
                //Toast.makeText(MemoryBookActivity.this, "添加片段", Toast.LENGTH_SHORT).show();
                Intent intentAddM = new Intent(MemoryBookActivity.this, MomentAddActivity.class);
                startActivity(intentAddM);
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
            llTop.setBackgroundColor(getResources().getColor(R.color.transparent));//AGB由相关工具获得，或者美工提供
            topTitle.setText("");
        } else if (h > 0 && h <= imageHeight-llTop.getHeight()) {
            // 只是layout背景透明(仿知乎滑动效果)
            //textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
            llTop.setBackgroundColor(getResources().getColor(R.color.transparent));
            topTitle.setText("");
        } else {
            llTop.setBackgroundColor(getResources().getColor(R.color.white));
            topTitle.setText("福的群纪念册");
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
