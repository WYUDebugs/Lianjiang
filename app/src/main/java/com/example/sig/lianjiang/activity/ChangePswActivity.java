package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.view.SecurityCodeView;
import com.example.sig.lianjiang.widget.CountDownTimerUtils;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by sig on 2018/9/11.
 */

public class ChangePswActivity extends AppCompatActivity implements View.OnClickListener,SecurityCodeView.InputCompleteListener{
    private ImageView back;
    private TextView text;
    private SecurityCodeView editSecurity;
    private String phone;  //需要后台获取当前用户电话
    private Button bt_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_change_psw);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        bt_send=(Button)findViewById(R.id.bt_send);
        bt_send.setOnClickListener(this);
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
            case R.id.bt_send:
//                showCodeDialog();
                break;
        }
    }

    private void sendMsg(String phone){
        BmobSMS.requestSMSCode(this, phone,"验证码", new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if(ex == null){
                    Toast.makeText(ChangePswActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                    showCodeDialog();
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(bt_send, 60000, 1000,R.drawable.shape_line_green,R.drawable.shape_line_grey);
                    mCountDownTimerUtils.start();
                }else {
                    Toast.makeText(ChangePswActivity.this,"验证码发送失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    /**
     * 验证码输入框
     */
    private void showCodeDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(ChangePswActivity.this);
        builder.setTitle("请输入验证码");

        View view=View.inflate(ChangePswActivity.this,R.layout.security_dialog,null);

        editSecurity = (SecurityCodeView) view.findViewById(R.id.scv_edittext);
        text = (TextView) view.findViewById(R.id.tv_text);
        editSecurity.setInputCompleteListener(this);

        builder.setView(view);
        builder.show();
    }

    @Override
    public void inputComplete() {

        BmobSMS.verifySmsCode(ChangePswActivity.this, phone, editSecurity.getEditContent(), new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {

                if(ex == null){
                    Toast.makeText(ChangePswActivity.this,"验证码输入成功",Toast.LENGTH_SHORT).show();
                    bt_send.setText("提交");
                }else {
                    text.setText("验证码输入错误");
                    text.setTextColor(Color.RED);
                }
            }
        });

    }

    @Override
    public void deleteContent(boolean isDelete) {
        if (isDelete){
            text.setText("输入验证码验证身份");
            text.setTextColor(Color.BLACK);
        }
    }
}
