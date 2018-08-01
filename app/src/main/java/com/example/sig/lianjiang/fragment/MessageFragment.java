package com.example.sig.lianjiang.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.sig.lianjiang.activity.MainActivity;
import com.example.sig.lianjiang.activity.R;
import com.example.sig.lianjiang.adapter.Message;
import com.example.sig.lianjiang.adapter.MessageAdapter;
import com.example.sig.lianjiang.utils.ListviewUtils;
import com.example.sig.lianjiang.view.CircleImageView;
import com.example.sig.lianjiang.view.MainNavigateTabBar;
import com.example.sig.lianjiang.view.MyListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by sig on 2018/7/9.
 */

public class MessageFragment extends Fragment implements MyListView.OnMeiTuanRefreshListener,View.OnClickListener{
    private List<Message> lists = new ArrayList<>();
    private MyListView listView;
    private MessageAdapter messageAdapter;
    private TextView tv_pull_to_refresh;
    private CircleImageView topHead;
    private final static int REFRESH_COMPLETE = 0;
    private static final int UPDATE_TEXT_DONE=1;
    private static final int UPDATE_TEXT_STAR=2;
    /**
     * mInterHandler是一个私有静态内部类继承自Handler，内部持有MainActivity的弱引用，
     * 避免内存泄露
     */
    private InterHandler mInterHandler = new InterHandler(this);

    private  class InterHandler extends Handler {
        private WeakReference<MessageFragment> mActivity;
        public InterHandler(MessageFragment activity){
            mActivity = new WeakReference<MessageFragment>(activity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            MessageFragment activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        activity.listView.setOnRefreshComplete();
                        activity.messageAdapter.notifyDataSetChanged();
                        activity.listView.setSelection(0);
                        break;
                    case UPDATE_TEXT_DONE:
                        tv_pull_to_refresh.setText("刷新完成");
                        listView.fin();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_message, container, false);
        topHead=view.findViewById(R.id.top_head);
        topHead.setOnClickListener(this);
        init();
        tv_pull_to_refresh=view.findViewById(R.id.tv_pull_to_refresh);
        listView = (MyListView) view.findViewById(R.id.list_view);
        messageAdapter=new MessageAdapter(getActivity(),R.layout.message_item_view,lists);
        listView.setAdapter(messageAdapter);
        listView.setOnMeiTuanRefreshListener(this);
        MainActivity.mNavigateTabBar.setOnCleanMessageTip(new MainNavigateTabBar.OnCleanMessageTip() {
            @Override
            public void onClean() {
                for (int i=0;i<lists.size();i++){
                    lists.set(i,new Message(R.mipmap.android,"SIG","最近怎么样？","21:45",0));
                }
                messageAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    private void init(){
        for (int i=0;i<20;i++){
            Message message=new Message(R.mipmap.android,"SIG","最近怎么样？","21:45",1);
            lists.add(message);
        }
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
                    mInterHandler.sendEmptyMessage(UPDATE_TEXT_STAR);
                    mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
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
             case R.id.top_head:
                 MainActivity.leftDrawerLayout.openDrawer();
                 break;

        }
    }
}
