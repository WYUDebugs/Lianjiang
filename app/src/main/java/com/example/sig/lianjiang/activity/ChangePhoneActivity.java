package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.widget.CountDownTimerUtils;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class ChangePhoneActivity extends AppCompatActivity implements View.OnClickListener{

    private UserResultDto resultDto;
    private ImageView back;
    private TextView tvGetYzm;
    private EditText mobile_edit;
    private Button button;
    private EditText yzm_edit;
    private TextView title;
    private TextView phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_change_phone);
        init();
    }

    private void init(){
        phoneNum=(TextView)findViewById(R.id.phone_num);
        title=findViewById(R.id.profile_title);
        title.setText("更改手机号");
        back=(ImageView)findViewById(R.id.top_left);
        button=findViewById(R.id.bt_send_moment);
        yzm_edit=findViewById(R.id.edtYzm);
        button.setOnClickListener(this);
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
            case R.id.bt_send_moment:
                String phone=mobile_edit.getText().toString().trim();
                Log.d("phone",phone);
                boolean phoneFlag=judgPhone(phone);
                if (phoneFlag == false) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                String msg=yzm_edit.getText().toString().trim();
                if (msg.isEmpty()&&phoneFlag==true) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
                if (!msg.isEmpty()) {
                    judgMsg(phone,msg);
                }

                break;

            default:
                break;
        }
    }

    public void submitChangePhone(final String phone) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", phone);
        String id= EMClient.getInstance().getCurrentUser();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",id);
        list.add(phoneParam);
        list.add(idParam);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changePhoneById, new OkHttpUtils.ResultCallback() {
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
                            Toast.makeText(ChangePhoneActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            Toast.makeText(ChangePhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000); //延时3秒关闭修改页面
                                         finish();
                                    } catch (InterruptedException e) {
                                         e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(ChangePhoneActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(ChangePhoneActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

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
     private void judgMsg(final String phone, String Number){

        BmobSMS.verifySmsCode(ChangePhoneActivity.this, phone, Number, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                if(ex == null){
                    submitChangePhone(phone);
                }else {
                    Toast.makeText(ChangePhoneActivity.this, "验证码验证失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean judgPhone(String phone) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (phone==null) {
            return false;
        }
        else{
            return phone.matches(telRegex);
        }
    }
}
