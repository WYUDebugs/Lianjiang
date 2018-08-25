package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.utils.CustomDatePicker;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.example.sig.lianjiang.utils.TimeUtil;

import java.util.Date;

public class SquareAddActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgTopLeft;
    private TextView tvTopTitle;
    private ImageView imgTopRight;
    private TextView tvTopRight;
    private EditText etMomentContent;
    private LinearLayout llLocationTime;
    private Button btSendMoment;
    private TextView tvLocationTime;
    private final static int REQUEST_CODE = 0x123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_square_add);
        initData();
        initView();
    }
    private void initData() {

    }

    private void initView() {
        imgTopLeft = (ImageView) findViewById(R.id.img_top_left);
        tvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        imgTopRight = (ImageView) findViewById(R.id.img_top_right);
        tvTopRight = (TextView) findViewById(R.id.tv_top_right);
        etMomentContent = (EditText) findViewById(R.id.et_moment_content);
        llLocationTime = (LinearLayout) findViewById(R.id.ll_location_time);
        btSendMoment = (Button) findViewById(R.id.bt_send_moment);
        tvLocationTime = (TextView) findViewById(R.id.tv_location_time);
        tvTopTitle.setText("发布动态");

        imgTopLeft.setOnClickListener(this);
        llLocationTime.setOnClickListener(this);
        btSendMoment.setOnClickListener(this);
    }

    //自定义弹框3
    public void alterDatePicker(){
        String now = TimeUtil.dateToStringNoS(new Date());
        String startTime = "2000-01-01 00:00";
        String endTime = "2030-01-01 00:00";
        CustomDatePicker customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvLocationTime.setText(time);
            }
        }, startTime, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        customDatePicker.show(now);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_top_left:
                finish();
                break;
            case R.id.ll_location_time:
                getLocation();
                break;
            case R.id.bt_send_moment:
                finish();
                break;

        }
    }
    private void theme(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.StatusBarLightMode(this);  //把标题栏字体变黑色
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
            tvLocationTime.setText(position);
        }
    }
}
