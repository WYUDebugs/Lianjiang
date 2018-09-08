package com.example.sig.lianjiang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.hyphenate.chat.EMClient;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.R;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends BaseActivity{
    private EditText editText;
    private RelativeLayout searchedUserLayout;
    private TextView nameText;
    private Button searchBtn;
    private String toAddUserPhone;
    private ProgressDialog progressDialog;
    private UserResultDto resultDto;
    private UserResultDto resultDto1;
    private Button indicator;
    private ImageView avatar;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_add_contact);
        TextView mTextView = (TextView) findViewById(R.id.add_list_friends);

        editText = (EditText) findViewById(R.id.edit_note);
        String strAdd = getResources().getString(R.string.add_friend);
        mTextView.setText(strAdd);
//        String strUserName = getResources().getString(R.string.user_name);
//        editText.setHint(strUserName);
        searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);
        nameText = (TextView) findViewById(R.id.name);
        searchBtn = (Button) findViewById(R.id.search);
        indicator=(Button)findViewById(R.id.indicator);
        avatar=(ImageView)findViewById(R.id.avatar);
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
    /**
     * search contact
     * @param v
     */
    public void searchContact(View v) {
        final String phone = editText.getText().toString().toLowerCase();
        String saveText = searchBtn.getText().toString();

        if (getString(R.string.button_search).equals(saveText)) {
            toAddUserPhone = phone;
            if(TextUtils.isEmpty(phone)) {
                new EaseAlertDialog(this, "请输入对方电话号码").show();
                return;
            }

            // TODO you can search the user from your app server here.
            getUserPost(phone);
            //show the userame and add button if user exist
//            searchedUserLayout.setVisibility(View.VISIBLE);
//            nameText.setText(toAddUserPhone);

        }
    }

    /**
     *  add contact
     * @param view
     */
    public void addContact(View view){
        if(EMClient.getInstance().getCurrentUser().equals(Integer.toString(id))){
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if(StarryHelper.getInstance().getContactList().containsKey(Integer.toString(id))){
            //let the user know the contact already in your contact list
            if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())){
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(Integer.toString(id), s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
    public void getUserPost(final String phone) {
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
                            Toast.makeText(AddContactActivity.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            id=resultDto.getData().getId();
                            searchedUserLayout.setVisibility(View.VISIBLE);
                            indicator.setVisibility(View.VISIBLE);
                            avatar.setVisibility(View.VISIBLE);
                            nameText.setText(resultDto.getData().getName());

                        }else {
                            searchedUserLayout.setVisibility(View.VISIBLE);
                            nameText.setText("找不到该用户");
                            indicator.setVisibility(View.INVISIBLE);
                            avatar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(AddContactActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();
    }



    public void back(View v) {
        finish();
    }
}
