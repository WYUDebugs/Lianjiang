package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
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
import com.example.sig.lianjiang.view.SecurityCodeView;
import com.example.sig.lianjiang.widget.CountDownTimerUtils;
import com.hyphenate.chat.EMClient;
import com.lidong.photopicker.Image;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by sig on 2018/9/11.
 */

public class ChangePswActivity extends AppCompatActivity implements View.OnClickListener,
        SecurityCodeView.InputCompleteListener,View.OnTouchListener{

    private UserResultDto resultDto;
    private ImageView back;
    private TextView text;
    private SecurityCodeView editSecurity;
    private String phone;
    private Button bt_send;
    private ImageView showOldPsw;
    private ImageView showNewPaw;
    private EditText oldPswET;
    private EditText newPswET;
    private TextView title;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_change_psw);
        init();
    }

    private void init() {
        title=findViewById(R.id.profile_title);
        title.setText("修改密码 ");
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        bt_send=(Button)findViewById(R.id.bt_send);
        bt_send.setOnClickListener(this);
        showOldPsw=(ImageView)findViewById(R.id.iv_old_password);
        showOldPsw.setOnTouchListener(this);
        showNewPaw=(ImageView)findViewById(R.id.iv_new_password);
        showNewPaw.setOnTouchListener(this);
        oldPswET=(EditText)findViewById(R.id.oldpassword);
        newPswET=(EditText)findViewById(R.id.newpassword);
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
                String oldPassword=oldPswET.getText().toString();
                newPassword=newPswET.getText().toString();
                if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    boolean flag=judgNewPsw(newPassword);
                    if (flag == false) {
                        Toast.makeText(this, "请按照要求填写新密码", Toast.LENGTH_SHORT).show();
                    } else {
                        getPhoneNum(oldPassword);
                    }
                }
                break;

            default:
                break;
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.iv_old_password:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldPswET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP:
                        oldPswET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    default:
                        break;
                }
                break;
            case R.id.iv_new_password:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        newPswET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP:
                        newPswET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void getPhoneNum(final String password) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param oldPswParam = new OkHttpUtils.Param("password", password);
        String id= EMClient.getInstance().getCurrentUser();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",id);
        list.add(oldPswParam);
        list.add(idParam);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.ckPsw, new OkHttpUtils.ResultCallback() {
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
                            Toast.makeText(ChangePswActivity.this,"服务器出错",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            phone=resultDto.getData().getPhone();
                            sendMsg(phone);
                            showCodeDialog();
                        } else {
                            Toast.makeText(ChangePswActivity.this, "旧密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(ChangePswActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
    public void changePsw(final String password) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param newPswParam = new OkHttpUtils.Param("password", password);
        String id= EMClient.getInstance().getCurrentUser();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",id);
        list.add(newPswParam);
        list.add(idParam);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.changeUserById, new OkHttpUtils.ResultCallback() {
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
                            Toast.makeText(ChangePswActivity.this,"服务器出错",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            Toast.makeText(ChangePswActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangePswActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(ChangePswActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
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
                       changePsw(newPassword);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(4000); //延时4秒关闭修改页面
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }).start();


                }else {
                    text.setText("验证码输入错误");
                    text.setTextColor(Color.RED);
                }
            }
        });

    }
    private boolean judgNewPsw(String newPsw) {
        String telRegex = "^(?![^a-zA-Z]+$)(?!\\D+$).{8,16}$";
        if (newPsw==null) {
            return false;
        }
        else{
            return newPsw.matches(telRegex);
        }
    }

    @Override
    public void deleteContent(boolean isDelete) {
        if (isDelete){
            text.setText("输入验证码验证身份");
            text.setTextColor(Color.BLACK);
        }
    }

}
