package com.example.sig.lianjiang.fragment;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.sig.lianjiang.bean.MemoryBook;
import com.example.sig.lianjiang.bean.MemoryBookListResult;
import com.example.sig.lianjiang.bean.PublicListResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.MemoryListModel;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.example.sig.lianjiang.view.ObservableListView;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class MemoryBookListFragment extends Fragment implements View.OnClickListener{

    private TextView tv_pull_to_refresh;
    private ObservableListView mListView;
    private MemoryBookListAdapter mAdapter;
//    private List<String> mList = new ArrayList<>();
    private View view;
    private RefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private static boolean isFirstEnter = true;
    private MemoryBookListResult memoryBookListResult;
    private List<MemoryListModel> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_memory_book_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

//        initImageLoader();
//        initListData();
        initView();

//        mListView.setOnMeiTuanRefreshListener(this);
        mRefreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        initListData();
                        getMemoryBookListPost(EMClient.getInstance().getCurrentUser());
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
//        if (isFirstEnter) {
//            isFirstEnter = false;
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
//        }
        return view;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);
    }

    private void initListData(List<MemoryBook> data) {
        mList.clear();
//        for (int i = 0; i < 5; i++) {
//            mList.add("福的朋友群纪念册" + i);
//        }
        for(int i=0;i<data.size();i++){
            MemoryBook memoryBook=data.get(i);
            int memoryBookId=memoryBook.getId();
            String title=memoryBook.getTitle();
            String cover=memoryBook.getCover();
            int friendCount=memoryBook.getFriendCount();
            int momentCount=memoryBook.getMomentCount();
            MemoryListModel memoryListModel=new MemoryListModel(Integer.toString(memoryBookId),cover,title,friendCount,momentCount);
            mList.add(memoryListModel);
        }

    }

    public void initView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        for (int i = 0; i < 5; i++) {
//            mList.add("福的朋友群纪念册" + i);
//        }
        mAdapter = new MemoryBookListAdapter(mList,getActivity());
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_memory_book_list_header,null);//假的头部
        mAdapter.setHeaderView(headerView);
        recyclerView.setAdapter(mAdapter);


//        mListView = (ObservableListView) view.findViewById(R.id.lv_bbs);
//        mList.add("header");
//        for (int i = 0; i < 5; i++) {
//            mList.add("福的朋友群纪念册" + i);
//        }
//        mAdapter = new MemoryBookListAdapter(getActivity(),mList);
//        mListView.setAdapter(mAdapter);
//        mListView.setScrollViewListener(this);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("wnf", "position=========================="+position);
//                if (position == 0) {
//
//                } else {
//                    Intent intent = new Intent(getActivity(), MemoryBookActivity.class);
//                    startActivity(intent);
//                    //Toast.makeText(MemoryBookListActivity.this, "点击" + mList.get(position-1), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("wnf", "position=========================="+position);
//                if (position == 1) {
//
//                } else {
//                    //Toast.makeText(getActivity(), "长按" + mList.get(position-1), Toast.LENGTH_SHORT).show();
//                    longClickItem(position-1);
//                }
//                return true;
//            }
//        });
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


    public void getMemoryBookListPost(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        Integer a=null;
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("uId", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findMemoryListByUserId, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryBookListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryBookListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryBookListResult = MemoryBookListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryBookListResult.getMsg().equals("success")){
                            //sUser.setmName(resultDto.getData().getName());
                            initListData(memoryBookListResult.getData());
                            mAdapter.notifyDataSetChanged();
                            Log.e("zxd","获取纪念册成功");
                        }else {
                            Log.e("zxd","获取纪念册失败");
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
