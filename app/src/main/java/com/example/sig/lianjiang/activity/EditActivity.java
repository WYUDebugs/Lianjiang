package com.example.sig.lianjiang.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sig.lianjiang.R;
/**
 * Created by sig on 2018/8/9.
 */

public class EditActivity extends BaseActivity{
    private EditText editText;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_edit);

        editText = (EditText) findViewById(R.id.edittext);
        String title = getIntent().getStringExtra("title");
        String data = getIntent().getStringExtra("data");
        Boolean editable = getIntent().getBooleanExtra("editable", false);
        if(title != null)
            ((TextView)findViewById(R.id.tv_title)).setText(title);
        if(data != null)
            editText.setText(data);

        editText.setEnabled(editable);
        editText.setSelection(editText.length());

        findViewById(R.id.btn_save).setEnabled(editable);
    }

    public void save(View view){
        setResult(RESULT_OK, new Intent().putExtra("data", editText.getText().toString()));
        finish();
    }

    public void back(View view) {
        finish();
    }
}
