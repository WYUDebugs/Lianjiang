package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.bean.UserListResultDto;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


public class LoginActivitysteup1 extends AppCompatActivity implements View.OnClickListener{

    private UserResultDto resultDto;

    private ImageView ivSubmit;
    private Button test;
    private EditText etPhone;
    private String phone;
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
        etPhone=(EditText)findViewById(R.id.etPhone);
        etPhone.addTextChangedListener(mTextWatcher);
        test=(Button)findViewById(R.id.test);
        test.setOnClickListener(this);
        ivSubmit=(ImageView)findViewById(R.id.ivSubmit);
        ivSubmit.setOnClickListener(this);
        if (StarryHelper.getInstance().getCurrentUsernName() != null) {
            etPhone.setText(StarryHelper.getInstance().getCurrentUsernName());
        }

        postDemo();
        getDemo();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ivSubmit:
                phone=etPhone.getText().toString();
                Intent intent=new Intent(this,LoginActivitysteup2.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                break;
            case R.id.test:
                phone=etPhone.getText().toString();
                Intent intent1=new Intent(this,RegisterActivitysteup1.class);
                intent1.putExtra("phone",phone);
                startActivity(intent1);
                break;
        }

    }
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            //          mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = etPhone.getSelectionStart();
            editEnd = etPhone.getSelectionEnd();
            if (temp.length() > 10) {
                ivSubmit.setImageResource(R.mipmap.ic_confirm);
            }else {
                ivSubmit.setImageResource(R.mipmap.ic_confirm_disable);
            }
        }
    };

    public void postDemo() {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param phoneParam = new OkHttpUtils.Param("phone", "2");
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
                        try {
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        //UserListResultDto resultDto=OkHttpUtils.getObjectFromJson(response.toString(),UserListResultDto.class);
                        Log.d("wnf", "*********************************************************************");
                        Log.d("wnf", "********************resultDto:" + resultDto);
                        Toast.makeText(LoginActivitysteup1.this, "resultDto:" + resultDto, Toast.LENGTH_SHORT).show();
                        if (resultDto.getData() != null) {

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(LoginActivitysteup1.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }

    public void getDemo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //get方式连接 url，get方式请求链接不能传参数
                //参数方式：OkHttpUtils.get(url,OkHttpUtils.ResultCallback())
                OkHttpUtils.get(APPConfig.findAllUser, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        UserListResultDto userListResultDto;
                        try {
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            userListResultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserListResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            userListResultDto =  UserListResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        Log.d("wnf", "*********************************************************************");
                        Log.d("wnf", "********************userListResultDto:" + userListResultDto);

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(LoginActivitysteup1.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
