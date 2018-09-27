package com.example.sig.lianjiang.fragment;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.activity.MemoryCoverUpdateActivity;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.MemoryBookListAdapter;
import com.example.sig.lianjiang.view.ObservableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MemoryBookListFragment extends Fragment implements ObservableListView.OnMeiTuanRefreshListener,View.OnClickListener,ObservableListView.ScrollViewListener{

    private TextView tv_pull_to_refresh;
    private ObservableListView mListView;
    private MemoryBookListAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    private final static int REFRESH_COMPLETE = 0;
    private static final int UPDATE_TEXT_DONE=1;
    private static final int UPDATE_TEXT_STAR=2;
    private InterHandler mInterHandler = new InterHandler(this);
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_memory_book_list, container, false);

        tv_pull_to_refresh=(TextView) view.findViewById(R.id.tv_pull_to_refresh);

//        initImageLoader();
//        initListData();
        initView();

        mListView.setOnMeiTuanRefreshListener(this);
        return view;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);
    }

    private void initListData() {

    }

    public void initView() {

        mListView = (ObservableListView) view.findViewById(R.id.lv_bbs);
        mList.add("header");
        for (int i = 0; i < 5; i++) {
            mList.add("福的朋友群纪念册" + i);
        }
        mAdapter = new MemoryBookListAdapter(getActivity(),mList);
        mListView.setAdapter(mAdapter);
        mListView.setScrollViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wnf", "position=========================="+position);
                if (position == 0) {

                } else {
                    Intent intent = new Intent(getActivity(), MemoryBookActivity.class);
                    startActivity(intent);
                    //Toast.makeText(MemoryBookListActivity.this, "点击" + mList.get(position-1), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("wnf", "position=========================="+position);
                if (position == 1) {

                } else {
                    //Toast.makeText(getActivity(), "长按" + mList.get(position-1), Toast.LENGTH_SHORT).show();
                    longClickItem(position-1);
                }
                return true;
            }
        });
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mInterHandler.sendEmptyMessage(UPDATE_TEXT_DONE);
                    Thread.sleep(1000);
                    mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
                    mInterHandler.sendEmptyMessage(UPDATE_TEXT_STAR);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void updateMemoryName() {
        final Dialog dialog1;
        View diaView = View.inflate(getActivity(), R.layout.dialogui_center_add, null);
        dialog1 = new Dialog(getActivity(), R.style.dialog);
        dialog1.setContentView(diaView);
        dialog1.show();
        TextView tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
        TextView tvSure = (TextView) diaView.findViewById(R.id.tv_sure);
        TextView tvCancel = (TextView) diaView.findViewById(R.id.tv_cancel);
        tvInfo.setText("请输入更改后的纪念册名称");
        final EditText etInput = (EditText) diaView.findViewById(R.id.et_input);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }

    public void longClickItem(int position) {
        View diaView= View.inflate(getActivity(), R.layout.dialogui_footer_setting, null);
        final Dialog dialog=new Dialog(getActivity(),R.style.dialogfooter);
        diaView.setMinimumWidth(R.dimen.width);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(diaView);
        dialog.show();
        TextView tvInfo;
        ImageView imgCancel;
        LinearLayout llMemorySettingPeople;
        LinearLayout llMemorySettingName;
        LinearLayout llMemorySettingCover;
        LinearLayout llMemorySettingDel;

        tvInfo = (TextView) diaView.findViewById(R.id.tv_info);
        tvInfo.setText("设置\""+mList.get(position)+"\"的信息");
        imgCancel = (ImageView) diaView.findViewById(R.id.img_cancel);
        llMemorySettingPeople = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_people);
        llMemorySettingName = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_name);
        llMemorySettingCover = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_cover);
        llMemorySettingDel = (LinearLayout) diaView.findViewById(R.id.ll_memory_setting_del);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        llMemorySettingPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        llMemorySettingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                updateMemoryName();
            }
        });
        llMemorySettingCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(getActivity(), MemoryCoverUpdateActivity.class);
                startActivity(intent);
            }
        });
        llMemorySettingDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                clickDelMemory();
            }
        });
    }

    public void clickDelMemory() {
        View diaView1= View.inflate(getActivity(), R.layout.dialogui_center_sure_or_cancel, null);
        final Dialog dialog1=new Dialog(getActivity(),R.style.dialog);
        dialog1.setContentView(diaView1);
        dialog1.show();
        TextView textInfo1 = (TextView) diaView1.findViewById(R.id.tv_info);
        TextView btSure1 = (TextView) diaView1.findViewById(R.id.tv_sure);
        TextView btCancel1 = (TextView) diaView1.findViewById(R.id.tv_cancel);
        textInfo1.setText("是否解散该纪念册？");
        btSure1.setTextColor(this.getResources().getColor(R.color.grey));
        btCancel1.setTextColor(this.getResources().getColor(R.color.red));
        btSure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "退出", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onScroll(int h){
//        if (h <= 0) {
//            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));//AGB由相关工具获得，或者美工提供
//        } else if (h > 0 && h <= imageHeight-textView.getHeight()) {
//            // 只是layout背景透明(仿知乎滑动效果)
//            textView.setBackgroundColor(Color.argb((int) 0, 57, 58, 62));
//            title.setText("");
//        } else {
//            textView.setBackgroundColor(Color.argb((int) 255, 57, 58, 62));
//            title.setText("广场");
//        }
    }

    private  class InterHandler extends Handler {
        private WeakReference<MemoryBookListFragment> mActivity;
        public InterHandler(MemoryBookListFragment activity){
            mActivity = new WeakReference<MemoryBookListFragment>(activity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            MemoryBookListFragment activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        activity.mListView.setOnRefreshComplete();
                        activity.mAdapter.notifyDataSetChanged();
                        activity.mListView.setSelection(0);
                        break;
                    case UPDATE_TEXT_DONE:
                        tv_pull_to_refresh.setText("刷新完成");
                        mListView.fin();
                        break;
                    case UPDATE_TEXT_STAR:
                        tv_pull_to_refresh.setText("下拉刷新");
                        break;
                }
            }else{
                super.handleMessage(msg);
            }
        }
    }



}
