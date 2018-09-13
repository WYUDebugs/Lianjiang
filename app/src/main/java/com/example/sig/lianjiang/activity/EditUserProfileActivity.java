package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.bean.CardBean;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.CustomDatePicker;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.TimeUtil;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private OptionsPickerView pvCustomOptions;
    private final static int REQUEST_CODE = 0x123;
    private LinearLayout mytopbar_back;
    private ImageView back;
    private LinearLayout changePhone;
    private LinearLayout changeNick;
    private LinearLayout changeSig; //个性签名
    private LinearLayout changePsw;
    private LinearLayout changeBirth;
    private LinearLayout changeAddress;
    private LinearLayout changeSex;
    private TextView tvlocation;
    private TextView tvBirth;
    private UserResultDto resultDto;
    private TextView textView;

    public final static String LEVEL_NICK="nick";
    public final static String  LEVEL_SIGNA="signa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_edit_user_profile);
        initView();
        getCardData();   //加载男女选择器内容
        initCustomOptionPicker();
    }

    private void initView(){
        mytopbar_back=(LinearLayout)findViewById(R.id.mytopbar);
        mytopbar_back.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
        back = (ImageView) findViewById(R.id.top_left);
        back.setOnClickListener(this);
        changePhone=(LinearLayout)findViewById(R.id.change_phone);
        changePhone.setOnClickListener(this);
        changeNick=(LinearLayout)findViewById(R.id.llNickname);
        changeNick.setOnClickListener(this);
        changeSex=(LinearLayout)findViewById(R.id.set_sex);
        changeSex.setOnClickListener(this);
        changeSig=(LinearLayout)findViewById(R.id.llMotto);
        changeSig.setOnClickListener(this);
        changePsw=(LinearLayout)findViewById(R.id.llChangePwd);
        changePsw.setOnClickListener(this);
        changeBirth=(LinearLayout)findViewById(R.id.llDirth);
        changeBirth.setOnClickListener(this);
        changeAddress=(LinearLayout)findViewById(R.id.llAdress);
        changeAddress.setOnClickListener(this);
        tvlocation=(TextView)findViewById(R.id.tvlocation);
        tvBirth=(TextView)findViewById(R.id.tvBirth);
        tvBirth.setOnClickListener(this);
        textView=(TextView)findViewById(R.id.tvCard);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.change_phone:
                Intent intent=new Intent(EditUserProfileActivity.this,ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.llNickname:
                Intent intent1=new Intent(EditUserProfileActivity.this,ChangeInfoActivity.class);
                intent1.putExtra("level",LEVEL_NICK);
                startActivity(intent1);
                break;
            case R.id.set_sex:
                pvCustomOptions.show(); //弹出性别选择器
                break;
            case R.id.llMotto:
                Intent intent2=new Intent(EditUserProfileActivity.this,ChangeInfoActivity.class);
                intent2.putExtra("level",LEVEL_SIGNA);
                startActivity(intent2);
                break;
            case R.id.llChangePwd:
                Intent intent3=new Intent(EditUserProfileActivity.this,ChangePswActivity.class);
                startActivity(intent3);
                break;
            case R.id.llDirth:
                alterDatePicker();
                break;
            case R.id.llAdress:
                getLocation();
                break;

        }
    }
    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();
                if (tx.equals("女")) {
                    String genderId = "1";
                    submitChangeGender(genderId);
                } else {
                    String genderId="0";
                    submitChangeGender(genderId);
                }
//                btn_CustomOptions.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })
                .isDialog(true)
                .build();

        pvCustomOptions.setPicker(cardItem);//添加数据


    }
    private void getCardData() {

        cardItem.add(new CardBean(0, "男" ));
        cardItem.add(new CardBean(1, "女" ));
    }
    public void getLocation(){
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 返回成功
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            String position = data.getStringExtra("position");
            tvlocation.setText(position);
        }
    }

    //自定义弹框3
    public void alterDatePicker(){
        String now = TimeUtil.dateToStringNoS(new Date());
        String startTime = "1950-01-01 00:00";
        String endTime = now;
        String seletTime="1997-01-01 00:00";
        CustomDatePicker customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvBirth.setText(time.substring(0,10));
            }
        }, startTime, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        customDatePicker.show(seletTime);
    }
    public void submitChangeGender(final String gender) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param genderParam = new OkHttpUtils.Param("name", gender);
        String id= EMClient.getInstance().getCurrentUser();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id",id);
        list.add(genderParam);
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
                            Toast.makeText(EditUserProfileActivity.this,"服务器出错,修改失败",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if (resultDto.getMsg().equals("success")) {
                            if (gender.equals("1")) {
                                textView.setText("女");
                            } else {
                                textView.setText("男");
                            }
                            Toast.makeText(EditUserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(EditUserProfileActivity.this, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(EditUserProfileActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
}
