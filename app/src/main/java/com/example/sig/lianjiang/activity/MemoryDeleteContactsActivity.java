package com.example.sig.lianjiang.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.Constant;
import com.example.sig.lianjiang.R;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.bean.MemoryFriend;
import com.example.sig.lianjiang.bean.MemoryFriendListResult;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sig on 2018/10/5.
 */

public class MemoryDeleteContactsActivity extends BaseActivity{
    /** if this is a new group */
    protected boolean isCreatingNewGroup;
    private MemoryDeleteContactsActivity.PickContactAdapter contactAdapter;
    /** members already in the group */
    private List<String> existMembers= new ArrayList<String>();
    private MemoryFriendListResult memoryFriendListResult;
    private String memoryBookId;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme();
        setContentView(R.layout.activity_group_pick_contacts);

        memoryBookId = getIntent().getStringExtra("memoryBookId");
        Log.e("zxd","活动"+memoryBookId);
        getMemoryBookFriencPost(memoryBookId);

    }

    private void initData(List<MemoryFriend> data){
        if(existMembers == null)
            existMembers = new ArrayList<String>();
        // get contact list
        final List<EaseUser> alluserList = new ArrayList<EaseUser>();
        for (EaseUser user : StarryHelper.getInstance().getContactList().values()) {
            if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(Constant.GROUP_USERNAME) & !user.getUsername().equals(Constant.CHAT_ROOM) & !user.getUsername().equals(Constant.CHAT_ROBOT))
//                alluserList.add(user);
                if(data!=null){
                    for(int i=0;i<data.size();i++){
                        MemoryFriend memoryFriend=data.get(i);
                        if(user.getUsername().equals(memoryFriend.getFriendId())){
                            alluserList.add(user);
                        }
                    }
                }
        }
        // sort the list
        Collections.sort(alluserList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        contactAdapter = new MemoryDeleteContactsActivity.PickContactAdapter(this, R.layout.em_row_contact_with_checkbox, alluserList);
        listView.setAdapter(contactAdapter);
        ((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.toggle();

            }
        });
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
     * save selected members
     *
     * @param v
     */
    public void save(View v) {
        List<String> var = getToBeAddMembers();
        flag=false;
        for (int i=0;i<var.size();i++){
            Log.e("zxd111","选择的人"+var.get(i));
            deleteMemoryBookFriencPost(var.get(i));
        }
        if(flag==true){
            Toast.makeText(MemoryDeleteContactsActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MemoryDeleteContactsActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
        }
//        setResult(RESULT_OK, new Intent().putExtra("newmembers", var.toArray(new String[var.size()])));
        finish();
    }

    /**
     * get selected members
     *
     * @return
     */
    private List<String> getToBeAddMembers() {
        List<String> members = new ArrayList<String>();
        int length = contactAdapter.isCheckedArray.length;
        for (int i = 0; i < length; i++) {
            String username = contactAdapter.getItem(i).getUsername();
            if (contactAdapter.isCheckedArray[i] && !existMembers.contains(username)) {
                members.add(username);
            }
        }

        return members;
    }

    /**
     * adapter
     */
    private class PickContactAdapter extends EaseContactAdapter {

        private boolean[] isCheckedArray;

        public PickContactAdapter(Context context, int resource, List<EaseUser> users) {
            super(context, resource, users);
            isCheckedArray = new boolean[users.size()];
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            final String username = getItem(position).getUsername();

            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
            TextView nameView = (TextView) view.findViewById(R.id.name);

            if (checkBox != null) {
                if(existMembers != null && existMembers.contains(username)){
                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
                }else{
                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // check the exist members
                        if (existMembers.contains(username)) {
                            isChecked = true;
                            checkBox.setChecked(true);
                        }
                        isCheckedArray[position] = isChecked;

                    }
                });
                // keep exist members checked
                if (existMembers.contains(username)) {
                    checkBox.setChecked(true);
                    isCheckedArray[position] = true;
                } else {
                    checkBox.setChecked(isCheckedArray[position]);
                }
            }

            return view;
        }
    }

    public void back(View view){
        finish();
    }

    public void getMemoryBookFriencPost(final String memoryBookId) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("memoryBookId", memoryBookId);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findMemoryFriend, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryFriendListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryFriendListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryFriendListResult = MemoryFriendListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryFriendListResult.getMsg().equals("find_success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Log.e("zxd","获取成员成功");
                            Log.d("zxd",memoryFriendListResult.getData().toString());
                            Log.d("zxd","111   "+memoryFriendListResult.getData().get(0).getFriendId());
                            initData(memoryFriendListResult.getData());
//                            Toast.makeText(MemoryPickContactsActivity.this,"获取成员成功",Toast.LENGTH_SHORT).show();
                        }else {
//                            Log.e("zxd","获取成员失败");
//                            Toast.makeText(MemoryPickContactsActivity.this,"获取成员失败",Toast.LENGTH_SHORT).show();
                            initData(null);
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

    public void deleteMemoryBookFriencPost(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.deleteMemoryFriend, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            memoryFriendListResult = OkHttpUtils.getObjectFromJson(response.toString(), MemoryFriendListResult.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            memoryFriendListResult = MemoryFriendListResult.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(memoryFriendListResult.getMsg().equals("deleteMemoryFriend_success")){
                            //sUser.setmName(resultDto.getData().getName());
//                            initListData(memoryBookListResult.getData());
//                            mAdapter.notifyDataSetChanged();
                            Log.e("zxd","添加成功");
//                            Toast.makeText(MemoryPickContactsActivity.this,"获取成员成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e("zxd","添加失败");
//                            Toast.makeText(MemoryPickContactsActivity.this,"获取成员失败",Toast.LENGTH_SHORT).show();
                            flag=true;
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        flag=true;
                    }
                }, list);
            }

        }).start();
    }

}
