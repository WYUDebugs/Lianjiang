package com.example.sig.lianjiang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivitysteup2 extends AppCompatActivity implements View.OnClickListener{
    private String phone;
    private EditText passwordEditText;
    private EditText nameEditText;
    private TextView ivSubmit;
    private ImageView top_back;
    private UserResultDto resultDto;
    private String passWord;
    private String name;
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
        setContentView(R.layout.activity_register_activitysteup2);
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        Toast.makeText(this,phone,Toast.LENGTH_SHORT).show();
        passwordEditText=(EditText)findViewById(R.id.etPwd);
        nameEditText=(EditText)findViewById(R.id.etName);
        ivSubmit=(TextView)findViewById(R.id.tvConfirm);
        ivSubmit.setOnClickListener(this);
        top_back=(ImageView)findViewById(R.id.top_back);
        top_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvConfirm:
                passWord = passwordEditText.getText().toString().trim();
                name= nameEditText.getText().toString().trim();
                registerPost(phone,passWord,name);
                break;
            case R.id.top_back:
                finish();
                break;
        }

    }

    public void register(final String id,final String passWord) {
        final String userId = id;
        final String pwd = passWord;
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return;
        }
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        // call method in SDK
                        EMClient.getInstance().createAccount(userId, pwd);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivitysteup2.this.isFinishing())
                                    pd.dismiss();
                                // save current user
                                StarryHelper.getInstance().setCurrentUserName(userId);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivitysteup2.this,LoginActivitysteup2.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("id",Integer.parseInt(id));
                                startActivity(intent);
//                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivitysteup2.this.isFinishing())
                                    pd.dismiss();
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.EXCEED_SERVICE_LIMIT){
                                    Toast.makeText(RegisterActivitysteup2.this, getResources().getString(R.string.register_exceed_service_limit), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }

    }
    public void registerPost(final String phone,final String passWord,final String name) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", phone);
        OkHttpUtils.Param passWordParam = new OkHttpUtils.Param("password", passWord);
        OkHttpUtils.Param nameParam = new OkHttpUtils.Param("name", name);
        list.add(phoneParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.register, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                            if(resultDto.getMsg().equals("register_success")){

//                                Toast.makeText(RegisterActivitysteup2.this,"注册成功",Toast.LENGTH_SHORT).show();
                                getIdPost(phone);
                            }else if(resultDto.getMsg().equals("phone_exist")){
                                Toast.makeText(RegisterActivitysteup2.this,"手机号码已经被注册过",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivitysteup2.this,"注册失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            //客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(RegisterActivitysteup2.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        //UserListResultDto resultDto=OkHttpUtils.getObjectFromJson(response.toString(),UserListResultDto.class);
                        Log.d("wnf", "*********************************************************************");
                        Log.d("wnf", "********************resultDto:" + resultDto);
                        Toast.makeText(RegisterActivitysteup2.this, "resultDto:" + resultDto, Toast.LENGTH_SHORT).show();
                        if (resultDto.getData() != null) {

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(RegisterActivitysteup2.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
    public void getIdPost(final String phone) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", phone);
        list.add(phoneParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findUserByPhone, new OkHttpUtils.ResultCallback() {
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
                            Toast.makeText(RegisterActivitysteup2.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success") && resultDto.getData() != null) {
                            String id = Integer.toString(resultDto.getData().getId());
                            register(id, passWord);
                        } else {
                            Toast.makeText(RegisterActivitysteup2.this, "注册失败", Toast.LENGTH_SHORT).show();
                            // 注册失败了是不是应该把我们自己后台的刚刚添加的用户删掉，
                            // 或者有其他的方法保证环信和我们自己后台的用户信息同时存在
                            deleteUserPost(phone);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(RegisterActivitysteup2.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }

    public void deleteUserPost(final String phone) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", phone);
        list.add(phoneParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.deleteUserByPhone, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                            if(resultDto.getMsg().equals("success")){
                                Log.e("RegisterActivitysteup2","删除成功");
                            }else {
                                Log.e("RegisterActivitysteup2","删除失败");
                            }
                        } catch (Exception e) {
                            //客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(RegisterActivitysteup2.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                            Log.e("RegisterActivitysteup2","删除失败");
                        }
                        //UserListResultDto resultDto=OkHttpUtils.getObjectFromJson(response.toString(),UserListResultDto.class);
                        Log.d("wnf", "*********************************************************************");
                        Log.d("wnf", "********************resultDto:" + resultDto);
                        Toast.makeText(RegisterActivitysteup2.this, "resultDto:" + resultDto, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(RegisterActivitysteup2.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }

}
