package com.example.sig.lianjiang.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.utils.StatusBarUtil;


public class LoginActivitysteup1 extends AppCompatActivity implements View.OnClickListener{
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

}
