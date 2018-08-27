package com.example.sig.lianjiang.activity;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.sig.lianjiang.adapter.UserProfile;
import com.example.sig.lianjiang.adapter.UserProfileAdapter;
import com.example.sig.lianjiang.utils.ImageUtils;
import com.example.sig.lianjiang.view.ObservableListView;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.view.ObservableScrollView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class UserProfileActivity extends BaseActivity implements OnClickListener,ObservableScrollView.ScrollViewListener{

    private ObservableScrollView mScrollView;
    private LinearLayout topbar;
    private TextView topbarText;
    private int imageHeight;
    private ImageView back;
    private TextView userName;
    private TextView userSex;
    private TextView userDay;
    private ImageView head_background;
    private ImageView head;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        theme();
        setContentView(R.layout.activity_user_profile);
        initView();
        initListeners();
    }
    public void initView(){
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        topbarText=(TextView)findViewById(R.id.top_center);
        topbar = (LinearLayout) findViewById(R.id.mytopbar_square);
        mScrollView=findViewById(R.id.lv_bbs);
//        mScrollView.setScrollViewListener(this);
        userName=findViewById(R.id.user_name);
        userSex=findViewById(R.id.user_sex);
        userDay=findViewById(R.id.user_day);
        head=findViewById(R.id.head);
        head_background=findViewById(R.id.head_background);
        head.setOnClickListener(this);
        head_background.setOnClickListener(this);
    }

    /*
    *  获取顶部图片(也可以任意一控件)高度后，设置滚动监听
     */
    private void initListeners() {
        ViewTreeObserver vto = head_background.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                head_background.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = head_background.getHeight();

                mScrollView.setScrollViewListener(UserProfileActivity.this);
            }
        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.head_background:
                ImageUtils.showImagePickDialog(this,"修改背景图片");
                break;
            case R.id.head:
                ImageUtils.showImagePickDialog(this,"修改头像");
                break;
            case R.id.top_left:
                finish();
                break;


        }
    }
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy)
    {
        if (y <= 0) {
            topbar.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
        } else if (y > 0 && y <= imageHeight-topbar.getHeight()) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                if(resultCode == RESULT_CANCELED) {
                    return;
                }
                Uri imageUri = data.getData();
                Toast.makeText(this,imageUri.toString(),Toast.LENGTH_SHORT).show();
                //这里得到图片后做相应操作
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if(resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                    Toast.makeText(this,imageUriCamera.toString(),Toast.LENGTH_SHORT).show();

                    //这里得到图片后做相应操作
                }
                break;

            default:
                break;
        }
    }
}
