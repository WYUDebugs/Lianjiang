package com.example.sig.lianjiang.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.sig.lianjiang.activity.AddContactActivity;
import com.example.sig.lianjiang.activity.ChatActivity;
import com.example.sig.lianjiang.activity.GroupsActivity;
import com.example.sig.lianjiang.activity.MainActivity;
import com.example.sig.lianjiang.activity.NewFriendsMsgActivity;
import com.example.sig.lianjiang.activity.UserProfileActivity;
import com.example.sig.lianjiang.bean.UserResultDto;
import com.example.sig.lianjiang.common.APPConfig;
import com.example.sig.lianjiang.utils.OkHttpUtils;
import com.hyphenate.chat.EMClient;
import com.example.sig.lianjiang.Constant;
import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.StarryHelper.DataSyncListener;
import com.example.sig.lianjiang.R;
//import com.example.sig.lianjiang.conference.ConferenceActivity;
import com.example.sig.lianjiang.db.InviteMessgeDao;
import com.example.sig.lianjiang.db.UserDao;
import com.example.sig.lianjiang.view.ContactItemView;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Created by sig on 2018/7/9.
 */

public class FriendFragment extends EaseContactListFragment {

    private static final String TAG = FriendFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    private UserResultDto resultDto;

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_friend, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_friend, null);
        contentContainer.addView(loadingView);
        registerForContextMenu(listView);
    }
    @Override
    public void getContactList(){
        super.getContactList();
        getFriend(contactList);
    }
    public void getFriend(final List<EaseUser> contactList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(contactList!=null&&contactList.size()>0){
                    for(int i=0;i<contactList.size();i++){
                        getNameAndHeadPost(contactList.get(i).getUsername());
                    }
                    getNameAndHeadPost(EMClient.getInstance().getCurrentUser());
                    // sorting
                    Collections.sort(contactList, new Comparator<EaseUser>() {

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
                }
            }
        }).start();

    }
    public void getNameAndHeadPost(final String id) {
        final List<OkHttpUtils.Param> list = new ArrayList<OkHttpUtils.Param>();
        //可以传多个参数，这里只写传一个参数，需要传多个参数时list.add();
        OkHttpUtils.Param idParam = new OkHttpUtils.Param("id", id);
        list.add(idParam);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //post方式连接  url，post方式请求必须传参
                //参数方式：OkHttpUtils.post(url,OkHttpUtils.ResultCallback(),list)
                OkHttpUtils.post(APPConfig.findUserById, new OkHttpUtils.ResultCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("testRun", "response------b" + response.toString());
                        try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                            // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                            resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                        } catch (Exception e) {
                            //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                            resultDto = UserResultDto.error("Exception:"+e.getClass());
                            e.printStackTrace();
                            Toast.makeText(getContext(),"服务器出错了",Toast.LENGTH_SHORT).show();
                            Log.e("wnf", "Exception------" + e.getMessage());
                        }
                        if(resultDto.getData()!=null){
                            String name=resultDto.getData().getName();
                            String head=APPConfig.img_url +resultDto.getData().getHeadimage();
                            EaseUserUtils.setUserNick(id,name);
                            EaseUserUtils.setUserAvatar(id,head);
                            Log.d("zxd","子类");
                            if(id.equals(EMClient.getInstance().getCurrentUser())){
                                setHead(APPConfig.img_url + resultDto.getData().getHeadimage());
                                contactListLayout.init(contactList);
                                Log.d("zxd","子类刷新");
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                        Toast.makeText(getContext(), "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }, list);
            }

        }).start();

    }
    @Override
    public void refresh() {
        Map<String, EaseUser> m = StarryHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.refresh();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }

    protected void setHead(String uri){
        titleBar.setHead(uri);
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
                NetUtils.hasDataConnection(getActivity());
            }
        });

        //设置联系人数据
        Map<String, EaseUser> m = StarryHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser)listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("username", username));
                }
            }
        });


        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("zxd111","right已执行");
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        //打开侧滑菜单
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("zxd","已执行");
//                Toast.makeText(getContext(),"1111",Toast.LENGTH_SHORT).show();
                MainActivity.leftDrawerLayout.openDrawer();
            }
        });

        contactSyncListener = new ContactSyncListener();
        StarryHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        StarryHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        StarryHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        if (StarryHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (StarryHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            StarryHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if(blackListSyncListener != null){
            StarryHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if(contactInfoSyncListener != null){
            StarryHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }


    protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    break;
                case R.id.group_item:
                    // 进入群聊列表页面
                    startActivity(new Intent(getActivity(), GroupsActivity.class));
                    break;

                default:
                    break;
            }
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
        toBeProcessUsername = toBeProcessUser.getUsername();
        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            try {
                // delete contact
                deleteContact(toBeProcessUser);
                // remove invitation message
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
//        else if(item.getItemId() == R.id.add_to_blacklist){
//            moveToBlacklist(toBeProcessUsername);
//            return true;
//        }
        return super.onContextItemSelected(item);
    }


    /**
     * delete contact
     *
     * @param tobeDeleteUser
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
                    // remove user from memory and database
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteContact(tobeDeleteUser.getUsername());
                    StarryHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            contactList.remove(tobeDeleteUser);
                            contactListLayout.refresh();

                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }).start();

    }

    class ContactSyncListener implements DataSyncListener{
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DataSyncListener{

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    class ContactInfoSyncListener implements DataSyncListener{

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }

    }


}
