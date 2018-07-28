package com.example.sig.lianjiang.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.sig.lianjiang.activity.R;
import com.example.sig.lianjiang.adapter.FriendAdapter;
import com.example.sig.lianjiang.bean.ContactPerson;
import com.example.sig.lianjiang.utils.Utils;
import com.example.sig.lianjiang.view.MyExpandableListView;
import com.example.sig.lianjiang.view.QuickIndexBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by sig on 2018/7/9.
 */

public class FriendFragment extends Fragment implements MyExpandableListView.OnMeiTuanRefreshListener{
    private MyExpandableListView mExpandableListView;
    ArrayList<ArrayList<ContactPerson>> contactPersonsList;
    private QuickIndexBar mQuickIndexBar;
    private TextView mTextDialog;
    private FriendAdapter friendAdapter;
    private TextView tv_pull_to_refresh;
    private final static int REFRESH_COMPLETE = 0;
    private static final int UPDATE_TEXT_DONE=1;
    private static final int UPDATE_TEXT_STAR=2;

    private FriendFragment.InterHandler mInterHandler = new FriendFragment.InterHandler(this);

    private  class InterHandler extends Handler {
        private WeakReference<FriendFragment> mActivity;
        public InterHandler(FriendFragment activity){
            mActivity = new WeakReference<FriendFragment>(activity);
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            FriendFragment activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        activity.mExpandableListView.setOnRefreshComplete();
                        activity.friendAdapter.notifyDataSetChanged();
                        activity.mExpandableListView.setSelection(0);
                        break;
                    case UPDATE_TEXT_DONE:
                        tv_pull_to_refresh.setText("刷新完成");
                        mExpandableListView.fin();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_friend, container, false);
        mExpandableListView = (MyExpandableListView) view.findViewById(R.id.address_expandable_listview);
        mExpandableListView.setOnMeiTuanRefreshListener(this);
        tv_pull_to_refresh=view.findViewById(R.id.tv_pull_to_refresh);
        mTextDialog = (TextView) view.findViewById(R.id.text_dialog);
        mQuickIndexBar = (QuickIndexBar) view.findViewById(R.id.quick_index_bar);
        initData();
        setListView();
        setIndexBar();
        return view;
    }



    /**
     * 初始化数据
     */
    private void initData() {
        contactPersonsList = Utils.getSortDataList(getActivity(),"person_name.txt");
        friendAdapter=new FriendAdapter(getActivity(), contactPersonsList);
    }

    /**
     * 设置ListView
     */
    private void setListView() {
        /**设置适配器*/
        mExpandableListView.setAdapter(friendAdapter);

        /**设置group不可点击*/
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//注意：一定要返回true，返回true这个方法才有效。
            }
        });

        /**展开所有条目*/
        for (int i = 0; i < contactPersonsList.size(); i++){
            mExpandableListView.expandGroup(i);
        }
    }

    /**
     * 设置setIndexBar
     */
    private void setIndexBar() {
        mQuickIndexBar.setTextView(mTextDialog);//设置textDialog
        mQuickIndexBar.setPaddingTop(15);//设置顶部padding
        mQuickIndexBar.setPaddingBottom(15);//设置底部padding

        /**设置监听器*/
        mQuickIndexBar.setOnLetterChangedListener(new QuickIndexBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                for( int i = 0; i< contactPersonsList.size();i++){
                    if(letter.equals(contactPersonsList.get(i).get(0).firstLetter)){
                        mExpandableListView.setSelectedGroup(i);
                    }
                }
            }
            @Override
            public void onLetterGone() {}
        });
    }
}
