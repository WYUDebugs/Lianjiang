package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.utils.StatusBarUtil;


public class LoginActivitysteup1 extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivSubmit;
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
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_login_activitysteup1);
        ivSubmit=(ImageView)findViewById(R.id.ivSubmit);
        ivSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ivSubmit:
                Intent intent=new Intent(this,LoginActivitysteup2.class);
                startActivity(intent);
                break;
        }

    }

}
