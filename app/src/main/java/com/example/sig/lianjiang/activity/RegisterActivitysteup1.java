package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.utils.StatusBarUtil;

public class RegisterActivitysteup1 extends AppCompatActivity implements View.OnClickListener{
    private Button test;
    private String phone;
    private ImageView top_back;
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
        setContentView(R.layout.activity_register_activitysteup1);
        test=(Button)findViewById(R.id.test);
        test.setOnClickListener(this);
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        top_back=(ImageView)findViewById(R.id.top_back);
        top_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.test:
                Intent intent=new Intent(this,RegisterActivitysteup2.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                break;
            case R.id.top_back:
                finish();
                break;
        }

    }
}
