package com.example.sig.lianjiang.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.runtimepermissions.PermissionsManager;
import com.example.sig.lianjiang.runtimepermissions.PermissionsResultAction;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.example.sig.lianjiang.view.SecurityCodeView;
import com.example.sig.lianjiang.widget.CountDownTimerUtils;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterActivitysteup1 extends AppCompatActivity implements View.OnClickListener,SecurityCodeView.InputCompleteListener{
    private String phone;
    private ImageView top_back;
    private TextView tvPhoneTip;
    private SecurityCodeView editText;
    private TextView tvConfirm;
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
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        top_back=(ImageView)findViewById(R.id.top_back);
        top_back.setOnClickListener(this);
        editText = (SecurityCodeView) findViewById(R.id.scv_edittext);
        tvPhoneTip=(TextView)findViewById(R.id.tvPhoneTip);
        tvConfirm=findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(this);
        setListener();
        sendMsg();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.top_back:
                finish();
                break;
            case R.id.tvConfirm:
                sendMsg();
                break;
        }

    }
    private void sendMsg(){
        BmobSMS.requestSMSCode(this, phone,"验证码", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if(ex == null){
                    tvPhoneTip.setVisibility(View.VISIBLE);
                    tvPhoneTip.setText("已发送验证码至"+phone);
                    Toast.makeText(RegisterActivitysteup1.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvConfirm, 60000, 1000);
                    mCountDownTimerUtils.start();
                }else {
                    tvPhoneTip.setVisibility(View.VISIBLE);
                    tvPhoneTip.setText("验证码发送失败");
                    Toast.makeText(RegisterActivitysteup1.this,"验证码发送失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    private void setListener() {
        editText.setInputCompleteListener(this);
    }
    public void inputComplete() {

        BmobSMS.verifySmsCode(RegisterActivitysteup1.this, phone, editText.getEditContent(), new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {

                if(ex == null){
                    Toast.makeText(RegisterActivitysteup1.this,"验证码输入成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivitysteup1.this,RegisterActivitysteup2.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterActivitysteup1.this,"验证码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void deleteContent(boolean isDelete) {

    }
}
