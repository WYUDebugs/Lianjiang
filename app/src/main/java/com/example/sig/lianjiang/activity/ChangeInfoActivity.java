package com.example.sig.lianjiang.activity;
import android.content.Intent;
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
import com.hyphenate.chat.EMClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.sig.lianjiang.activity.EditUserProfileActivity.LEVEL_NICK;
import static com.example.sig.lianjiang.activity.EditUserProfileActivity.LEVEL_SIGNA;

/**
 * Created by sig on 2018/9/10.
 */

public class ChangeInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private UserResultDto resultDto;
    private ImageView back;
    private TextView title;
    private EditText editText;
    private Button button;
    private ImageView imageView;
    private TextView nickName;
    private TextView singa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_change_info);
        init();
        getLevel();

    }
    private void init(){
        singa=(TextView)findViewById(R.id.tvMotto);
        nickName=(TextView)findViewById(R.id.tvNickname);
        title=findViewById(R.id.profile_title);
        editText=findViewById(R.id.edtContent);
        button=(Button)findViewById(R.id.bt_send_moment);
        button.setOnClickListener(this);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        imageView=findViewById(R.id.ivDel);
        imageView.setOnClickListener(this);
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
            case R.id.ivDel:
                editText.setText("");
                break;
            case R.id.bt_send_moment:
                if (getLevel().equals(LEVEL_NICK)) {
                    String textContent=editText.getText().toString();
                    if (textContent.isEmpty()) {
                        Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        submitChangeNick(textContent);
                    }
                    break;
                } else if (getLevel().equals(LEVEL_SIGNA)) {
                    String textContent2=editText.getText().toString();
                    if (textContent2.isEmpty()) {
                        Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        submitChangeSigna(textContent2);
                    }
                    break;
                }
            default:
                break;
        }
    }
    private String getLevel(){
        Intent intent=getIntent();
        String level=intent.getStringExtra("level");
        if (level.equals(LEVEL_NICK)) {
            title.setText("修改昵称");
            return LEVEL_NICK;
        } else if (level.equals(LEVEL_SIGNA)){
            title.setText("修改签名");
            return LEVEL_SIGNA;
        }
            return null;
    }

    public void submitChangeNick(final String name) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param nameParam = new OkHttpUtils.Param("name", name);
        String id=EMClient.getInstance().getCurrentUser();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",id);
        list.add(nameParam);
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
                            Toast.makeText(ChangeInfoActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            Toast.makeText(ChangeInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ChangeInfoActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(ChangeInfoActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }

    public void submitChangeSigna(final String signature) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param signaParam = new OkHttpUtils.Param("signature", signature);
        String id=EMClient.getInstance().getCurrentUser();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",id);
        list.add(signaParam);
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
                            Toast.makeText(ChangeInfoActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            Toast.makeText(ChangeInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ChangeInfoActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(ChangeInfoActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
}
