package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.widget.CountDownTimerUtils;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class ChangePhoneActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private TextView tvGetYzm;
    private EditText mobile_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_change_phone);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        tvGetYzm=(TextView)findViewById(R.id.tvGetYzm);
        tvGetYzm.setOnClickListener(this);
        mobile_edit=(EditText)findViewById(R.id.mobile_edit);
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.top_left:
                finish();
                break;
            case R.id.tvGetYzm:
                sendMsg(mobile_edit.getText().toString().trim());
                break;
        }
    }

    private void sendMsg(String phone){
        BmobSMS.requestSMSCode(this, phone,"验证码", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if(ex == null){
                    Toast.makeText(ChangePhoneActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetYzm, 60000, 1000,R.drawable.shape_line_green,R.drawable.shape_line_grey);
                    mCountDownTimerUtils.start();
                }else {
                    Toast.makeText(ChangePhoneActivity.this,"验证码发送失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    /*
    *验证验证码是否正确
    * request 手机号，用户输入的验证码
     */
    private void judgMsg(String phone,String Number){
        BmobSMS.verifySmsCode(ChangePhoneActivity.this, phone, Number, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {

                if(ex == null){
                    Toast.makeText(ChangePhoneActivity.this,"验证码输入成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ChangePhoneActivity.this,"验证码输入错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
