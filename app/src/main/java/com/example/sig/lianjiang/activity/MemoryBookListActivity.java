package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.bean.MemoryBook;
import com.example.sig.lianjiang.bean.MemoryBookListResult;
import com.example.sig.lianjiang.bean.MemoryBookResult;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.fragment.MemoryBookListFragment;
import com.example.sig.lianjiang.fragment.MemoryBookListStarFragment;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;


public class MemoryBookListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private LinearLayout llSearchMemory;
    private ImageView imgToStar;
    private FloatingActionButton fabAddMBook;

    //Fragment Object
    private Fragment fg1;
    private Fragment fg2;
    private LinearLayout lyContent;
    private FragmentManager fManager;
    private boolean showWhichFrag = false;
    private MemoryBookResult memoryBookResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //顶部渲染
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.StatusBarLightMode(this);  //把标题栏字体变黑色
        setContentView(R.layout.activity_memory_book_list);
        fManager=getSupportFragmentManager();
        initView();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        if(fg1 == null){
            fg1 = new MemoryBookListFragment();
            fTransaction.add(R.id.ly_content,fg1);
        }else{
            fTransaction.show(fg1);
        }
        showWhichFrag = false;
        fTransaction.commit();
    }

    private void initView() {
        lyContent = (LinearLayout) findViewById(R.id.ly_content);
        back=(ImageView)findViewById(R.id.top_left);
        back.setOnClickListener(this);
        fabAddMBook = (FloatingActionButton) findViewById(R.id.fab_add_memory);
        fabAddMBook.setOnClickListener(this);
        imgToStar = (ImageView) findViewById(R.id.img_to_star);
        imgToStar.setOnClickListener(this);

        llSearchMemory = (LinearLayout) findViewById(R.id.ll_search_memory);
        llSearchMemory.setOnClickListener(this);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.ll_search_memory:
//                Intent intentSearch = new Intent(MemoryBookListActivity.this, MemoryBookSearchListActivity.class);
//                startActivity(intentSearch);
                break;
            case R.id.img_to_star:
                FragmentTransaction fTransaction = fManager.beginTransaction();
                hideAllFragment(fTransaction);
                if (showWhichFrag) {
                    if (fg1 == null) {
                        fg1 = new MemoryBookListFragment();
                        fTransaction.add(R.id.ly_content, fg1);
                    } else {
                        fTransaction.show(fg1);
                    }
                    imgToStar.setImageResource(R.mipmap.star);
                    showWhichFrag = false;
                } else {
                    if(fg2 == null){
                        fg2 = new MemoryBookListStarFragment();
                        fTransaction.add(R.id.ly_content,fg2);
                    }else{
                        fTransaction.show(fg2);
                    }
                    imgToStar.setImageResource(R.drawable.rectangles);
                    showWhichFrag = true;
                }
                fTransaction.commit();
                break;
            case R.id.fab_add_memory:
                //Toast.makeText(MemoryBookListActivity.this, "点击添加", Toast.LENGTH_SHORT).show();
                final Dialog dialog1;
                View diaView = View.inflate(MemoryBookListActivity.this, R.layout.dialogui_center_add, null);
                dialog1 = new Dialog(MemoryBookListActivity.this, R.style.dialog);
                dialog1.setContentView(diaView);
                dialog1.show();
                TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
                TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
                TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
                final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
//                final String title=etInput.getText().toString();
//                Log.d("zxdm",title);
//                Log.d("zxdm",title.trim());
                tvSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String title=etInput.getText().toString().trim();
                        if(title.length()>14){
                            Toast.makeText(MemoryBookListActivity.this,"纪念册名称过长",Toast.LENGTH_SHORT).show();
                        }else if(title.equals("")){
                            Toast.makeText(MemoryBookListActivity.this,"纪念册名称不能为空",Toast.LENGTH_SHORT).show();
                        }else{
                            addMemoryBookPost(EMClient.getInstance().getCurrentUser(),title,dialog1);
                        }
//                        dialog1.cancel();
//                        Intent intent = new Intent(MemoryBookListActivity.this, MemoryBookActivity.class);
//                        startActivity(intent);
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.cancel();
                    }
                });
                break;

        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void addMemoryBookPost(final String owner,final String title,final Dialog dialog) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Integer a=null;
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("owner", owner);
        OkHttpUtils.Param titleParam = new OkHttpUtils.Param("title", title);
        list.add(idParam);
        list.add(titleParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.addBook, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookResult = MemoryBookResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookResult.getMsg().equals("add_success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Log.e("zxd","添加纪念册成功");
                            MemoryBook memoryBook=memoryBookResult.getData();
                            String memoryBookId=Integer.toString(memoryBook.getId());
                            Toast.makeText(MemoryBookListActivity.this,"添加纪念册成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MemoryBookListActivity.this, MemoryBookActivity.class);
                            intent.putExtra("memoryBookId",memoryBookId);
                            startActivity(intent);
                            dialog.cancel();
                        }else {
                            Log.e("zxd","添加纪念册失败");
                            Toast.makeText(MemoryBookListActivity.this,"添加纪念册失败",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                    }
                }, list);
            }

        }).start();
    }


}