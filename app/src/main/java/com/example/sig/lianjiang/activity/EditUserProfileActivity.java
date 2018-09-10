package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sig.lianjiang.R;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout mytopbar_back;
    private ImageView back;
    private LinearLayout changePhone;
    private LinearLayout changeNick;
    private LinearLayout changeSig;
    private LinearLayout changePsw;
    private LinearLayout changeBirth;
    private LinearLayout changeAddress;
    private LinearLayout changeSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_edit_user_profile);
        initView();
    }

    private void initView(){
        mytopbar_back=(LinearLayout)findViewById(R.id.mytopbar);
        mytopbar_back.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
        back = (ImageView) findViewById(R.id.top_left);
        back.setOnClickListener(this);
        changePhone=(LinearLayout)findViewById(R.id.change_phone);
        changePhone.setOnClickListener(this);
        changeNick=(LinearLayout)findViewById(R.id.llNickname);
        changeNick.setOnClickListener(this);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.change_phone:
                Intent intent=new Intent(EditUserProfileActivity.this,ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.llNickname:
                Intent intent1=new Intent(EditUserProfileActivity.this,ChangeInfoActivity.class);
                startActivity(intent1);
                break;


        }
    }
}
