package com.example.sig.lianjiang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.MemoryNineGridAdapter;
import com.example.sig.lianjiang.adapter.Message;
import com.example.sig.lianjiang.adapter.MessageAdapter;
import com.example.sig.lianjiang.fragment.MessageFragment;
import com.example.sig.lianjiang.model.NineGridTestModel;
import com.example.sig.lianjiang.utils.StatusBarUtil;
import com.example.sig.lianjiang.view.CircleImageView;
import com.example.sig.lianjiang.view.MyListView;
import com.example.sig.lianjiang.view.ObservableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MomentInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView topTitle;
    private ImageView imgInfoSetting;
    private EditText etMomentComment;
    private Button btSendComment;

    private ViewFlipper viewFlipper;
    private List<String> mListData = new ArrayList<>();
    private ImageView imgPictureOne;
    private ImageView imgPictureTwo;

    //private View convertView;
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
        setContentView(R.layout.activity_moment_info);
        initData();
        initView();
        MyThread mt = new MyThread() ;	// 实例化Runnable子类对象
        new Thread(mt).start() ;
    }

    public void initData() {
        for (int i = 0; i < 1; i++) {
            mListData.add("你真是个小机灵鬼" + i);
        }
    }

    public void initView() {

        imgPictureOne = (ImageView) findViewById(R.id.img_picture_one);
        imgPictureOne.setOnClickListener(this);
        imgPictureTwo = (ImageView) findViewById(R.id.img_picture_two);
        imgPictureTwo.setOnClickListener(this);
        topTitle = (TextView) findViewById(R.id.top_title);
        imgInfoSetting = (ImageView) findViewById(R.id.img_info_setting);

        etMomentComment = (EditText) findViewById(R.id.et_moment_comment);
        btSendComment = (Button) findViewById(R.id.bt_send_comment);
        btSendComment.setOnClickListener(this);
        back = findViewById(R.id.top_left);
        back.setOnClickListener(this);
        imgInfoSetting.setOnClickListener(this);
        viewFlipper = (ViewFlipper) findViewById(R.id.filpper);
        updateView();

    }

    public void updateView() {
        int listSize = mListData.size();
        for (int i = 0; i < listSize; i++) {
            View convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_moment_comment, null);
            LinearLayout llOne = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ImageView imgHeadicon1 = (ImageView) convertView.findViewById(R.id.img_headicon1);
            TextView tvComment1 = (TextView) convertView.findViewById(R.id.tv_comment1);

            LinearLayout llTwo = (LinearLayout) convertView.findViewById(R.id.ll_two);
            ImageView imgHeadicon2 = (ImageView) convertView.findViewById(R.id.img_headicon2);
            TextView tvComment2 = (TextView) convertView.findViewById(R.id.tv_comment2);

            LinearLayout llThree = (LinearLayout) convertView.findViewById(R.id.ll_three);
            ImageView imgHeadicon3 = (ImageView) convertView.findViewById(R.id.img_headicon3);
            TextView tvComment3 = (TextView) convertView.findViewById(R.id.tv_comment3);

            tvComment1.setText(mListData.get(i));
            i++;
            if (i < listSize) {
                tvComment2.setText(mListData.get(i));
                i++;
                if (i < listSize) {
                    tvComment3.setText(mListData.get(i));
                } else {
                    llThree.setVisibility(View.GONE);
                }
            } else {
                llTwo.setVisibility(View.GONE);
                llThree.setVisibility(View.GONE);
            }

            viewFlipper.addView(convertView);
        }

    }

    public void updateConvertView(int listSize) {
        //增加一条数据，计算以前数据的数目是否是3的倍数，如果是3的倍数则新增一个view，若不是移除最后一个view，更新最后一个view
        if (listSize % 3 == 0) {
            View convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_moment_comment, null);
            LinearLayout llOne = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ImageView imgHeadicon1 = (ImageView) convertView.findViewById(R.id.img_headicon1);
            TextView tvComment1 = (TextView) convertView.findViewById(R.id.tv_comment1);
            LinearLayout llTwo = (LinearLayout) convertView.findViewById(R.id.ll_two);
            ImageView imgHeadicon2 = (ImageView) convertView.findViewById(R.id.img_headicon2);
            TextView tvComment2 = (TextView) convertView.findViewById(R.id.tv_comment2);
            LinearLayout llThree = (LinearLayout) convertView.findViewById(R.id.ll_three);
            ImageView imgHeadicon3 = (ImageView) convertView.findViewById(R.id.img_headicon3);
            TextView tvComment3 = (TextView) convertView.findViewById(R.id.tv_comment3);

            tvComment1.setText(mListData.get(listSize));
            llTwo.setVisibility(View.GONE);
            llThree.setVisibility(View.GONE);
            viewFlipper.addView(convertView);
        } else {
            int lastNum = listSize % 3;
            View convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_moment_comment, null);
            LinearLayout llOne = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ImageView imgHeadicon1 = (ImageView) convertView.findViewById(R.id.img_headicon1);
            TextView tvComment1 = (TextView) convertView.findViewById(R.id.tv_comment1);

            LinearLayout llTwo = (LinearLayout) convertView.findViewById(R.id.ll_two);
            ImageView imgHeadicon2 = (ImageView) convertView.findViewById(R.id.img_headicon2);
            TextView tvComment2 = (TextView) convertView.findViewById(R.id.tv_comment2);

            LinearLayout llThree = (LinearLayout) convertView.findViewById(R.id.ll_three);
            ImageView imgHeadicon3 = (ImageView) convertView.findViewById(R.id.img_headicon3);
            TextView tvComment3 = (TextView) convertView.findViewById(R.id.tv_comment3);
            if (lastNum > 0) {
                tvComment1.setText(mListData.get(listSize - lastNum));
                if (lastNum > 1) {
                    tvComment2.setText(mListData.get(listSize - lastNum + 1));
                    tvComment3.setText(mListData.get(listSize));
                } else {
                    tvComment2.setText(mListData.get(listSize));
                    llThree.setVisibility(View.GONE);
                }
            }
            viewFlipper.addView(convertView);
            viewFlipper.removeViewAt(listSize / 3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left:
                finish();
                break;
            case R.id.img_picture_one:
            case R.id.img_picture_two:
                Intent intent = new Intent(MomentInfoActivity.this, PictureDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_send_comment:
                //Toast.makeText(MomentInfoActivity.this, "添加编辑好友", Toast.LENGTH_SHORT).show();
                String str = etMomentComment.getText().toString().trim();
                if ("".equals(str)) {
                    Toast.makeText(MomentInfoActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    int listSize = mListData.size();
                    mListData.add(str);
                    updateConvertView(listSize);
                    etMomentComment.setText("");
                }
                break;
            case R.id.img_info_setting:
                //Toast.makeText(MemoryBookActivity.this, "设置", Toast.LENGTH_SHORT).show();
                clickSetting();
                break;
//            case R.id.fab_add_moment:
//                Toast.makeText(MomentInfoActivity.this, "添加片段", Toast.LENGTH_SHORT).show();
//                break;

        }
    }

    public void clickSetting() {
        View diaView = View.inflate(MomentInfoActivity.this, R.layout.dialogui_footer_moment_setting, null);
        final Dialog dialog = new Dialog(MomentInfoActivity.this, R.style.dialogfooter);
        diaView.setMinimumWidth(R.dimen.width);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(diaView);
        dialog.show();
        ImageView imgCancel;
        TextView tvInfo;
        LinearLayout llMomentSettingUpdate;
        LinearLayout llMomentSettingDel;

        imgCancel = (ImageView) diaView.findViewById(R.id.img_cancel);
        tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
        llMomentSettingUpdate = (LinearLayout) diaView.findViewById(R.id.ll_moment_setting_update);
        llMomentSettingDel = (LinearLayout) diaView.findViewById(R.id.ll_moment_setting_del);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        llMomentSettingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        llMomentSettingDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                clickDelMoment();
            }
        });
    }

    public void clickDelMoment() {
        View diaView1 = View.inflate(MomentInfoActivity.this, R.layout.dialogui_center_sure_or_cancel, null);
        final Dialog dialog1 = new Dialog(MomentInfoActivity.this, R.style.dialog);
        dialog1.setContentView(diaView1);
        dialog1.show();
        TextView textInfo1 = (TextView) diaView1.findViewById(R.id.tv_info);
        TextView btSure1 = (TextView) diaView1.findViewById(R.id.tv_sure);
        TextView btCancel1 = (TextView) diaView1.findViewById(R.id.tv_cancel);
        textInfo1.setText("是否删除该纪念册片段？");
        btSure1.setTextColor(this.getResources().getColor(R.color.grey));
        btCancel1.setTextColor(this.getResources().getColor(R.color.red));
        btSure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MemoryBookActivity.this, "退出", Toast.LENGTH_SHORT).show();
                dialog1.cancel();
            }
        });
        btCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }

    class MyThread extends Thread {

        public synchronized void updateConvertView(int listSize) {
            android.os.Message message = new android.os.Message();
            message.what = 0;
            message.obj = listSize;
            handler.sendMessage(message);
        }

        public void run() {
            int j = 0;
            while (true) {
                int listSize = mListData.size();
                mListData.add("你真是个小机灵鬼。线程" + j);
                j++;
                updateConvertView(listSize);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    int listSize = (int) msg.obj;
                    updateConvertView(listSize);
                    break;
            }
        }

    };
}
