package com.example.sig.lianjiang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.MemoryBookActivity;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.adapter.MemoryBookStarAdapter;
import com.example.sig.lianjiang.bean.MemoryBook;
import com.example.sig.lianjiang.bean.MemoryBookListResult;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.model.MemoryBookStarModel;
import com.example.sig.lianjiang.model.MemoryListModel;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.hyphenate.chat.EMClient;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

public class MemoryBookListStarFragment extends Fragment implements TagCloudView.OnTagClickListener{

    private ImageView back;
    private TagCloudView tcvTags;//标签云对象
    private List<MemoryBookStarModel> list = new ArrayList<>();//标签云数据的集合
//    private List<String> listClick = new ArrayList<>();//点击过的标签云的数据的集合
    private View view;
    private MemoryBookStarAdapter adapter;
    private MemoryBookListResult memoryBookListResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_memory_book_liststar, container, false);
        initView();
        getMemoryBookListPost(EMClient.getInstance().getCurrentUser());
        return view;
    }
    public void initView() {
        //给集合添加数据
//        for (int i = 0; i < 40; i++) {
//            list.add("纪念册"+i);
//        }
        tcvTags = (TagCloudView) view.findViewById(R.id.tcv_tags);
        //设置标签云的点击事件
        tcvTags.setOnTagClickListener(this);
        //给标签云设置适配器
        adapter = new MemoryBookStarAdapter(list);
        tcvTags.setAdapter(adapter);
    }

    /**
     * 点击标签是回调的方法
     */
    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
//        view.setSelected(!view.isSelected());//设置标签的选择状态
//        if (view.isSelected()) {
//            //加入集合
//            listClick.add(list.get(position));
            Intent intent = new Intent(getActivity(), MemoryBookActivity.class);
            startActivity(intent);
//            Toast.makeText(getActivity(), "你点击的是：" + list.get(position), Toast.LENGTH_SHORT).show();
//        } else {
//            //移除集合
//            listClick.remove(list.get(position));
//        }
        //Toast.makeText(getActivity(), "点击过的标签：" + listClick.toString(), Toast.LENGTH_SHORT).show();
    }

    private void initListData(List<MemoryBook> data) {
        list.clear();
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
            MemoryBookStarModel memoryListModel=new MemoryBookStarModel(Integer.toString(memoryBookId),cover,title);
            list.add(memoryListModel);
        }

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
                            adapter.notifyDataSetChanged();
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
