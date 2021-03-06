package com.example.sig.lianjiang.activity;

import android.annotation.TargetApi;
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
import com.example.sig.lianjiang.runtimepermissions.PermissionsManager;
import com.example.sig.lianjiang.runtimepermissions.PermissionsResultAction;
import com.example.sig.lianjiang.utils.ConfigUtil;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


public class LoginActivitysteup1 extends AppCompatActivity implements View.OnClickListener{

    private UserResultDto resultDto;

    private ImageView ivSubmit;
    private EditText etPhone;
    private String phone;
    private int id;
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
        setContentView(R.layout.activity_login_activitysteup1);
        // 如果没有显示过引导图，则显示之（为了方便查看效果，此处把判断条件注释掉了）
         if (ConfigUtil.needShowGuide(this)) {
            startActivity(new Intent(this, WelcomeGuideActivity.class));
        }
        etPhone=(EditText)findViewById(R.id.etPhone);
        etPhone.addTextChangedListener(mTextWatcher);
        ivSubmit=(ImageView)findViewById(R.id.ivSubmit);
        ivSubmit.setOnClickListener(this);
        if (StarryHelper.getInstance().getCurrentUsernName() != null) {
//            etPhone.setText(StarryHelper.getInstance().getCurrentUsernName());
            getUserByIdpostDemo(StarryHelper.getInstance().getCurrentUsernName());
        }
        requestPermissions();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ivSubmit:
                phone=etPhone.getText().toString();
                if(phone.equals("")){
                    return;
                }
                postDemo(phone);
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
                ivSubmit.setClickable(true);
                ivSubmit.setImageResource(R.mipmap.ic_confirm);
            }else {
                ivSubmit.setImageResource(R.mipmap.ic_confirm_disable);
                ivSubmit.setClickable(false);
            }
        }
    };

    public void postDemo(final String phone) {
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
                            Toast.makeText(LoginActivitysteup1.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            Intent intent=new Intent(LoginActivitysteup1.this,LoginActivitysteup2.class);
                            id=resultDto.getData().getId();
                            name=resultDto.getData().getName();
                            intent.putExtra("phone",phone);
                            intent.putExtra("id",id);
                            intent.putExtra("name",name);
                            startActivity(intent);
                        }else {
                            Intent intent1=new Intent(LoginActivitysteup1.this,RegisterActivitysteup1.class);
                            intent1.putExtra("phone",phone);
                            startActivity(intent1);
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

    public void getUserByIdpostDemo(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findUserById, new OkHttpUtils.ResultCallback() {
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
                            Toast.makeText(LoginActivitysteup1.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getMsg().equals("success")){
                            String phone=resultDto.getData().getPhone();
                            etPhone.setText(phone);
                        }else {

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
    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(LoginActivitysteup1.this, "权限： " + permission + "未被授权", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
