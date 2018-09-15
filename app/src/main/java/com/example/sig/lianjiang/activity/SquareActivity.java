package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.sig.lianjiang.view.ObservableListView;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SquareActivity extends AppCompatActivity implements ObservableListView.OnMeiTuanRefreshListener,View.OnClickListener,ObservableListView.ScrollViewListener{
    private TextView title;
    private ImageView back;
    private TextView tv_pull_to_refresh;
    private int imageHeight=800;
    private LinearLayout textView;
    public static CommentUser sUser = new CommentUser(Integer.parseInt(EMClient.getInstance().getCurrentUser()), "未知用户"); // 当前登录用户
    private ObservableListView mListView;
    private NineGridAdapter mAdapter;
    private List<NineGridTestModel> mList = new ArrayList<>();
    private UserResultDto resultDto;
    private PublicListResultDto publicListResultDto;
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

    private  class InterHandler extends Handler {
        private WeakReference<SquareActivity> mActivity;
        public InterHandler(SquareActivity activity){
            mActivity = new WeakReference<SquareActivity>(activity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            SquareActivity activity = mActivity.get();
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_left:
                finish();
                break;

        }
    }
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    getpublishListPost(EMClient.getInstance().getCurrentUser());
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getUserPost(EMClient.getInstance().getCurrentUser());
        setContentView(R.layout.activity_square);
        tv_pull_to_refresh=(TextView) findViewById(R.id.tv_pull_to_refresh);
        title=(TextView)findViewById(R.id.top_center);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        textView = (LinearLayout) findViewById(R.id.mytopbar_square);

        initImageLoader();
        getpublishListPost(EMClient.getInstance().getCurrentUser());
        initView();

        mListView.setOnMeiTuanRefreshListener(this);


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
        mListView = (ObservableListView) findViewById(R.id.lv_bbs);
        mAdapter = new NineGridAdapter(this,mList,new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
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
        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"click "+position,Toast.LENGTH_SHORT).show();
//            }
//        });
        mListView.setScrollViewListener(this);
    }
    @Override
    public void onScroll(int h){
        if (h <= 0) {
            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
        } else if (h > 0 && h <= imageHeight -textView.getHeight()
                ) {
            // 只是layout背景透明(仿知乎滑动效果)
            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
            title.setText("");
        } else {
            textView.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
            title.setText("动态");
        }
    }
    public void getUserPost(final String id) {
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
    public void inputComment(final View v) {
        inputComment(v, null);
    }

    /**
     * 弹出评论对话框
     * @param v
     * @param receiver
     */
    public void inputComment(final View v, CommentUser receiver) {

        CommentFun.inputComment(SquareActivity.this, mListView, v, receiver, new CommentFun.InputCommentListener() {
            @Override
            public void onCommitComment() {
                mAdapter.notifyDataSetChanged();
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
