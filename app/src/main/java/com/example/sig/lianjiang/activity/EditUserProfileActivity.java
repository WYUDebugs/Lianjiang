package com.example.sig.lianjiang.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sig.lianjiang.R;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout mytopbar_back;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_edit_user_profile);
        initView();
    }

    private void initView(){
        mytopbar_back=(LinearLayout)findViewById(R.id.mytopbar);
        mytopbar_back.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
        back = (ImageView) findViewById(R.id.top_left);
        back.setOnClickListener(this);
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


        }
    }
}
