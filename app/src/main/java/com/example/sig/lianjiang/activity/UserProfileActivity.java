package com.example.sig.lianjiang.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.sig.lianjiang.adapter.UserProfile;
import com.example.sig.lianjiang.adapter.UserProfileAdapter;
import com.example.sig.lianjiang.view.ObservableListView;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.R;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class UserProfileActivity extends BaseActivity implements OnClickListener,ObservableListView.ScrollViewListener{

    private ObservableListView mListView;
    private UserProfileAdapter mAdapter;
    private List<UserProfile> mList = new ArrayList<>();
    private LinearLayout topbar;
    private TextView topbarText;
    private int imageHeight=800;
    private ImageView back;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        theme();
        setContentView(R.layout.activity_user_profile);
        getData();
        initView();
    }
    public void initView(){
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        topbarText=(TextView)findViewById(R.id.top_center);
        topbar = (LinearLayout) findViewById(R.id.mytopbar_square);
        mListView=findViewById(R.id.lv_bbs);
        mAdapter = new UserProfileAdapter(this);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
        mListView.setScrollViewListener(this);
    }
    //注入灵魂
    public void getData(){
        mList.add(new UserProfile(R.mipmap.ic_phone,"SIG",false));
        mList.add(new UserProfile(R.mipmap.ic_sex,"男",false));
        mList.add(new UserProfile(R.mipmap.ic_age,"18",false));
        mList.add(new UserProfile(R.mipmap.ic_adress,"广东",true));
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.top_left:
                finish();
                break;

        }
    }
    @Override
    public void onScroll(int h){
        if (h <= 0) {
            topbar.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
        } else if (h > 0 && h <= imageHeight-topbar.getHeight()) {
            // 只是layout背景透明(仿知乎滑动效果)
            topbar.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
            topbarText.setText("");
        } else {
            topbar.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
            topbarText.setText("个人信息");
        }
    }
    private void theme(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
