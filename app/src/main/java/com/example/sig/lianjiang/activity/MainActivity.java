 package com.example.sig.lianjiang.activity;

 import android.os.Build;
 import android.os.Bundle;
 import android.support.v4.app.FragmentManager;
 import android.support.v7.app.AppCompatActivity;
 import android.util.Log;
 import android.view.Gravity;
 import android.view.LayoutInflater;
 import android.view.MotionEvent;
 import android.view.View;
 import android.view.Window;
 import android.view.WindowManager;
 import android.widget.FrameLayout;
 import android.widget.ImageView;
 import android.widget.Toast;

 import com.example.sig.lianjiang.R;
 import com.example.sig.lianjiang.bean.UserResultDto;
 import com.example.sig.lianjiang.common.APPConfig;
 import com.example.sig.lianjiang.event.HideButtonEvent;
 import com.example.sig.lianjiang.event.ShowButtonEvent;
 import com.example.sig.lianjiang.fragment.DynamicFragment;
 import com.example.sig.lianjiang.fragment.FriendFragment;
 import com.example.sig.lianjiang.fragment.MenuLeftFragment;
 import com.example.sig.lianjiang.fragment.MessageFragment;
 import com.example.sig.lianjiang.fragment.StarFragment;
 import com.example.sig.lianjiang.leftmenu.FlowingView;
 import com.example.sig.lianjiang.leftmenu.LeftDrawerLayout;
 import com.example.sig.lianjiang.utils.OkHttpUtils;
 import com.example.sig.lianjiang.utils.PopupMenuUtil;
 import com.example.sig.lianjiang.view.CircleImageView;
 import com.example.sig.lianjiang.view.DragDeleteTextView;
 import com.example.sig.lianjiang.view.MainNavigateTabBar;

 import cn.bmob.sms.BmobSMS;
 import cn.bmob.v3.Bmob;
 import de.greenrobot.event.EventBus;
 import android.annotation.SuppressLint;
 import android.annotation.TargetApi;
 import android.content.BroadcastReceiver;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.IntentFilter;
 import android.net.Uri;
 import android.os.Build;
 import android.os.Bundle;
 import android.os.PowerManager;
 import android.support.annotation.NonNull;
 import android.support.v4.app.Fragment;
 import android.support.v4.app.FragmentTransaction;
 import android.support.v4.content.LocalBroadcastManager;
 import android.view.KeyEvent;
 import android.view.View;
 import android.widget.Button;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.hyphenate.EMCallBack;
 import com.hyphenate.EMClientListener;
 import com.hyphenate.EMContactListener;
 import com.hyphenate.EMMessageListener;
 import com.hyphenate.EMMultiDeviceListener;
 import com.hyphenate.chat.EMClient;
 import com.hyphenate.chat.EMMessage;
 import com.example.sig.lianjiang.Constant;
 import com.example.sig.lianjiang.StarryHelper;
 import com.example.sig.lianjiang.HMSPushHelper;
 import com.example.sig.lianjiang.R;
 import com.example.sig.lianjiang.db.InviteMessgeDao;
 import com.example.sig.lianjiang.db.UserDao;
 import com.example.sig.lianjiang.runtimepermissions.PermissionsManager;
 import com.example.sig.lianjiang.runtimepermissions.PermissionsResultAction;
 import com.hyphenate.easeui.domain.EaseUser;
 import com.hyphenate.easeui.utils.EaseCommonUtils;
 import com.hyphenate.easeui.utils.EaseUserUtils;
 import com.hyphenate.util.EMLog;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;


 public class MainActivity extends BaseActivity implements View.OnClickListener,MainNavigateTabBar.UpdateTabBar{

     protected static final String TAG = "MainActivity";
     private static final String TAG_PAGE_MESSAGE = "消息";
     private static final String TAG_PAGE_FRIEND = "联系人";
     private static final String TAG_PAGE_PUBLISH = "发布";
     private static final String TAG_PAGE_DYNAMIC = "发现";
     private static final String TAG_PAGE_STAR = "星球";
     private MessageFragment messageFragment=new MessageFragment();
     private FriendFragment friendFragment=new FriendFragment();
     private DynamicFragment dynamicFragment=new DynamicFragment();
     private StarFragment starFragment=new StarFragment();
     private FrameLayout continer,closeMenu;
     public static LeftDrawerLayout leftDrawerLayout;
     private FlowingView flowingView;
     // user logged into another device
     public boolean isConflict = false;
     // user account was removed
     private boolean isCurrentAccountRemoved = false;
     private int Messagecount=0;
     private boolean friendTip=false;
     private boolean dynamicTip=false;
     private boolean starTip=false;
     private ImageView addThing;
     private PopupMenuUtil popupMenuUtil=new PopupMenuUtil();
     private List<EaseUser> contactList= new ArrayList<EaseUser>();
     private Map<String, EaseUser> contactsMap;
     private UserResultDto resultDto;


     public static MainNavigateTabBar mNavigateTabBar;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             Window window = getWindow();
             // Translucent status bar
             window.setFlags(
                     WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                     WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         }

         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             String packageName = getPackageName();
             PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
             if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                 try {
                     //some device doesn't has activity to handle this intent
                     //so add try catch
                     Intent intent = new Intent();
                     intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                     intent.setData(Uri.parse("package:" + packageName));
                     startActivity(intent);
                 } catch (Exception e) {
                 }
             }
         }

         //make sure activity will not in background if user is logged into another device or removed
         if (getIntent() != null &&
                 (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                         getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                         getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
             StarryHelper.getInstance().logout(false,null);
             finish();
             startActivity(new Intent(this, LoginActivitysteup1.class));
             return;
         } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
             finish();
             startActivity(new Intent(this, LoginActivitysteup1.class));
             return;
         }
         setContentView(R.layout.activity_main);
         // runtime permission for android 6.0, just require all permissions here for simple
         requestPermissions();
         mNavigateTabBar = (MainNavigateTabBar) findViewById(R.id.mainTabBar);
         //messageNum=(DragDeleteTextView)findViewById(R.id.message_num);
         mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

         mNavigateTabBar.addTab(MessageFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.message, R.mipmap.message_select, TAG_PAGE_MESSAGE));
         mNavigateTabBar.addTab(FriendFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.friend, R.mipmap.friend_select, TAG_PAGE_FRIEND));
         mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, TAG_PAGE_PUBLISH));
         mNavigateTabBar.addTab(DynamicFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.dynamic, R.mipmap.dynamic_select, TAG_PAGE_DYNAMIC));
         mNavigateTabBar.addTab(StarFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.star, R.mipmap.star_select, TAG_PAGE_STAR));
         mNavigateTabBar.setUpdateMessageNum(this);
         addThing= (ImageView) findViewById(R.id.tab_post_icon);
         addThing.setOnClickListener(this);
         EventBus.getDefault().register(this);
         init();
         showExceptionDialogFromIntent(getIntent());
         inviteMessgeDao = new InviteMessgeDao(this);
         UserDao userDao = new UserDao(this);
         //register broadcast receiver to receive the change of group from DemoHelper
         registerBroadcastReceiver();


         EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
         EMClient.getInstance().addClientListener(clientListener);
         EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
         //debug purpose only
         registerInternalDebugReceiver();

         // 获取华为 HMS 推送 token
         HMSPushHelper.getInstance().getHMSToken(this);
         setHeadandname();
     }

     EMClientListener clientListener = new EMClientListener() {
         @Override
         public void onMigrate2x(boolean success) {
             Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
             if (success) {
                 refreshUIWithMessage();
             }
         }
     };

     @TargetApi(23)
     private void requestPermissions() {
         PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
             @Override
             public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onDenied(String permission) {
                 //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
             }
         });
     }

     EMMessageListener messageListener = new EMMessageListener() {

         @Override
         public void onMessageReceived(List<EMMessage> messages) {
             // notify new message
             for (EMMessage message: messages) {
                 StarryHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
             }
             refreshUIWithMessage();
         }

         @Override
         public void onCmdMessageReceived(List<EMMessage> messages) {
             refreshUIWithMessage();
         }

         @Override
         public void onMessageRead(List<EMMessage> messages) {
         }

         @Override
         public void onMessageDelivered(List<EMMessage> message) {
         }

         @Override
         public void onMessageRecalled(List<EMMessage> messages) {
             refreshUIWithMessage();
         }

         @Override
         public void onMessageChanged(EMMessage message, Object change) {}
     };

     private void refreshUIWithMessage() {
         runOnUiThread(new Runnable() {
             public void run() {
                 mNavigateTabBar.refreshTip();
                 // refresh unread count
//                 updateUnreadLabel();
//                 if (MainNavigateTabBar.getCurrentSelectedTab() == 0) {
//                     // refresh conversation list
//                     if (conversationListFragment != null) {
//                         conversationListFragment.refresh();
//                     }
//                 }
             }
         });
     }
    //底部切换回调函数
     @Override
     public void back(View view) {
         super.back(view);
     }

     @Override
     public int messageNum(){
//         int count = getUnreadMsgCountTotal();
         int count=100;
         Log.e("1234","5");
         return count;
     }
     @Override
     public boolean friendTip(){
//         int count = getUnreadAddressCountTotal();
//         if(count>0){
//             return true;
//         }else {
//             return false;
//         }
         return false;
     }
     @Override
     public boolean DynamicTip(){
         return false;
     }
     @Override
     public boolean starTip(){
         return false;
     }
     @Override
     public void cleanMessage(){

     }
     private void registerBroadcastReceiver() {
         broadcastManager = LocalBroadcastManager.getInstance(this);
         IntentFilter intentFilter = new IntentFilter();
         intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
         intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
         broadcastReceiver = new BroadcastReceiver() {

             @Override
             public void onReceive(Context context, Intent intent) {
//                 updateUnreadLabel();
//                 updateUnreadAddressLable();
                 mNavigateTabBar.refreshTip();
//                 if (currentTabIndex == 0) {
//                     // refresh conversation list
//                     if (conversationListFragment != null) {
//                         conversationListFragment.refresh();
//                     }
//                 } else if (currentTabIndex == 1) {
//                     if(contactListFragment != null) {
//                         contactListFragment.refresh();
//                     }
//                 }
                 String action = intent.getAction();
                 if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                     if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                         GroupsActivity.instance.onResume();
                     }
                 }
             }
         };
         broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
     }
     private void setHeadandname(){
         getContactList();
         getFriend(contactList);
     }
     private void getContactList() {
         contactList.clear();
         if(contactsMap == null){
             return;
         }
         synchronized (this.contactsMap) {
             Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
             List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
             while (iterator.hasNext()) {
                 Map.Entry<String, EaseUser> entry = iterator.next();
                 // to make it compatible with data in previous version, you can remove this check if this is new app
                 if (!entry.getKey().equals("item_new_friends")
                         && !entry.getKey().equals("item_groups")
                         && !entry.getKey().equals("item_chatroom")
                         && !entry.getKey().equals("item_robots")){
                     if(!blackList.contains(entry.getKey())){
                         //filter out users in blacklist
                         EaseUser user = entry.getValue();
                         EaseCommonUtils.setUserInitialLetter(user);
                         contactList.add(user);
                     }
                 }
             }
         }

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
                         Log.d("testRun", "response------" + response.toString());
                         try {// 不要在这个try catch里对ResultDto进行调用，因为这里解析json数据可能会因为后台出错等各种问题导致解析结果异常
                             // 解析后台传过来的json数据时，ResultDto类里Object要改为对应的实体,例如User或者List<User>
                             resultDto = OkHttpUtils.getObjectFromJson(response.toString(), UserResultDto.class);
                         } catch (Exception e) {
                             //json数据解析出错，可能是后台传过来的数据有问题，有可能是ResultDto实体相应的参数没对应上，客户端出错
                             resultDto = UserResultDto.error("Exception:"+e.getClass());
                             e.printStackTrace();
                             Toast.makeText(MainActivity.this,"服务器出错了",Toast.LENGTH_SHORT).show();
                             Log.e("wnf", "Exception------" + e.getMessage());
                         }
                         if(resultDto.getData()!=null){
                             String name=resultDto.getData().getName();
                             String head=APPConfig.img_url +resultDto.getData().getHeadimage();
                             EaseUserUtils.setUserNick(id,name);
                             EaseUserUtils.setUserAvatar(id,head);

                         }else {

                         }
                     }

                     @Override
                     public void onFailure(Exception e) {
                         Log.d("testRun", "请求失败------Exception:"+e.getMessage());
                         Toast.makeText(MainActivity.this, "网络请求失败，请重试！", Toast.LENGTH_SHORT).show();
                     }
                 }, list);
             }

         }).start();

     }

     public void getFriend(List<EaseUser> contactList){
         for(int i=0;i<contactList.size();i++){
             getNameAndHeadPost(contactList.get(i).getUsername());
         }
         getNameAndHeadPost(EMClient.getInstance().getCurrentUser());
     }
     public class MyContactListener implements EMContactListener {
         @Override
         public void onContactAdded(String username) {}
         @Override
         public void onContactDeleted(final String username) {
             runOnUiThread(new Runnable() {
                 public void run() {
                     if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                             username.equals(ChatActivity.activityInstance.toChatUsername)) {
                         String st10 = getResources().getString(R.string.have_you_removed);
                         Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                 .show();
                         ChatActivity.activityInstance.finish();
                     }
                 }
             });
             updateUnreadAddressLable();
         }
         @Override
         public void onContactInvited(String username, String reason) {}
         @Override
         public void onFriendRequestAccepted(String username) {}
         @Override
         public void onFriendRequestDeclined(String username) {}
     }

     public class MyMultiDeviceListener implements EMMultiDeviceListener {

         @Override
         public void onContactEvent(int event, String target, String ext) {

         }

         @Override
         public void onGroupEvent(int event, String target, final List<String> username) {
             switch (event) {
                 case EMMultiDeviceListener.GROUP_LEAVE:
                     ChatActivity.activityInstance.finish();
                     break;
                 default:
                     break;
             }
         }
     }

     private void unregisterBroadcastReceiver(){
         broadcastManager.unregisterReceiver(broadcastReceiver);
     }



     /**
      * update unread message count
      */
     public void updateUnreadLabel() {
         Messagecount = getUnreadMsgCountTotal();
         mNavigateTabBar.refreshTip();
     }

     /**
      * update the total unread count
      */
     public void updateUnreadAddressLable() {
         runOnUiThread(new Runnable() {
             public void run() {
                 mNavigateTabBar.refreshTip();
             }
         });

     }

     /**
      * get unread event notification count, including application, accepted, etc
      *
      * @return
      */
     public int getUnreadAddressCountTotal() {
         int unreadAddressCountTotal = 0;
         unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
         return unreadAddressCountTotal;
     }

     /**
      * get unread message count
      *
      * @return
      */
     public int getUnreadMsgCountTotal() {
         return EMClient.getInstance().chatManager().getUnreadMsgsCount();
     }

     private InviteMessgeDao inviteMessgeDao;

     @Override
     protected void onResume() {
         super.onResume();

         if (!isConflict && !isCurrentAccountRemoved) {
             updateUnreadLabel();
             updateUnreadAddressLable();
         }

         // unregister this event listener when this activity enters the
         // background
         StarryHelper sdkHelper = StarryHelper.getInstance();
         sdkHelper.pushActivity(this);

         EMClient.getInstance().chatManager().addMessageListener(messageListener);
     }

     @Override
     protected void onPause() {
         super.onPause();

         EMClient.getInstance().chatManager().removeMessageListener(messageListener);
         EMClient.getInstance().removeClientListener(clientListener);
         StarryHelper sdkHelper = StarryHelper.getInstance();
         sdkHelper.popActivity(this);
     }

//     @Override
//     protected void onSaveInstanceState(Bundle outState) {
//         outState.putBoolean("isConflict", isConflict);
//         outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
//         super.onSaveInstanceState(outState);
//     }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
             moveTaskToBack(false);
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }

     private android.app.AlertDialog.Builder exceptionBuilder;
     private boolean isExceptionDialogShow =  false;
     private BroadcastReceiver internalDebugReceiver;
//     private ConversationListFragment conversationListFragment;
     private BroadcastReceiver broadcastReceiver;
     private LocalBroadcastManager broadcastManager;

     private int getExceptionMessageId(String exceptionType) {
         if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
             return R.string.connect_conflict;
         } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
             return R.string.em_user_remove;
         } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
             return R.string.user_forbidden;
         }
         return R.string.Network_error;
     }
     /**
      * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
      */
     private void showExceptionDialog(String exceptionType) {
         isExceptionDialogShow = true;
         StarryHelper.getInstance().logout(false,null);
         String st = getResources().getString(R.string.Logoff_notification);
         if (!MainActivity.this.isFinishing()) {
             // clear up global variables
             try {
                 if (exceptionBuilder == null)
                     exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                 exceptionBuilder.setTitle(st);
                 exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                 exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                         exceptionBuilder = null;
                         isExceptionDialogShow = false;
                         finish();
                         Intent intent = new Intent(MainActivity.this, LoginActivitysteup1.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                     }
                 });
                 exceptionBuilder.setCancelable(false);
                 exceptionBuilder.create().show();
                 isConflict = true;
             } catch (Exception e) {
                 EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
             }
         }
     }

     private void showExceptionDialogFromIntent(Intent intent) {
         EMLog.e(TAG, "showExceptionDialogFromIntent");
         if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
             showExceptionDialog(Constant.ACCOUNT_CONFLICT);
         } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
             showExceptionDialog(Constant.ACCOUNT_REMOVED);
         } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
             showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
         } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                 intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
             this.finish();
             startActivity(new Intent(this, LoginActivitysteup1.class));
         }
     }

     @Override
     protected void onNewIntent(Intent intent) {
         super.onNewIntent(intent);
         showExceptionDialogFromIntent(intent);
     }

     /**
      * debug purpose only, you can ignore this
      */
     private void registerInternalDebugReceiver() {
         internalDebugReceiver = new BroadcastReceiver() {

             @Override
             public void onReceive(Context context, Intent intent) {
                 StarryHelper.getInstance().logout(false,new EMCallBack() {

                     @Override
                     public void onSuccess() {
                         runOnUiThread(new Runnable() {
                             public void run() {
                                 finish();
                                 startActivity(new Intent(MainActivity.this, LoginActivitysteup1.class));
                             }
                         });
                     }

                     @Override
                     public void onProgress(int progress, String status) {}

                     @Override
                     public void onError(int code, String message) {}
                 });
             }
         };
         IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
         registerReceiver(internalDebugReceiver, filter);
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
         PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
     }

     //**********************************************************************************
     @Override
     protected void onSaveInstanceState(Bundle outState) {
         outState.putBoolean("isConflict", isConflict);
         outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
         super.onSaveInstanceState(outState);
         mNavigateTabBar.onSaveInstanceState(outState);
     }


     public void onClickPublish(View v) {
         Toast.makeText(this, "发布", Toast.LENGTH_LONG).show();
     }


     private void init() {
         initView();
         initEvent();
     }

     private void initView() {
         leftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.leftDrawerLayout);
         flowingView = (FlowingView) findViewById(R.id.flowingView);
     }

     private void initEvent() {
         setFragment();
     }

     private void setFragment(){
         FragmentManager fm = getSupportFragmentManager();
         MenuLeftFragment menuLeftFragment = (MenuLeftFragment) fm.findFragmentById(R.id.container_menu);
         if (menuLeftFragment == null) {
             fm.beginTransaction().add(R.id.container_menu, menuLeftFragment = new MenuLeftFragment()).commit();
         }
         setCloseMenuTouch();
         leftDrawerLayout.setFluidView(flowingView);
         leftDrawerLayout.setMenuFragment(menuLeftFragment);
     }

     private void setCloseMenuTouch() {
         continer = (FrameLayout) findViewById(android.R.id.content);
         closeMenu = new FrameLayout(this);
         continer.addView(closeMenu);
         FrameLayout.LayoutParams fmPara = new FrameLayout.LayoutParams(250,FrameLayout.LayoutParams.MATCH_PARENT);
         fmPara.gravity = Gravity.RIGHT;
         closeMenu.setLayoutParams(fmPara);
         closeMenu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (leftDrawerLayout.isShownMenu()) {
                     leftDrawerLayout.closeDrawer();
                 }
             }
         });


         closeMenu.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                     if (leftDrawerLayout.isShownMenu()) {
                         leftDrawerLayout.closeDrawer();
                     }
                 }
                 return false;
             }
         });
     }

     @Override
     public void onClick(View v) {
         switch (v.getId()){
//             case R.id.top_head:
//                 leftDrawerLayout.openDrawer();
//                 break;
             case R.id.tab_post_icon:
                 popupMenuUtil._show(this, addThing);
                 break;

         }
     }

     public void onEvent(HideButtonEvent hideButtonEvent) {
         if(hideButtonEvent.isHide()){
             closeMenu.setVisibility(View.GONE);
         }
     }
     public void onEvent(ShowButtonEvent showButtonEvent){
         if(showButtonEvent.isShow()){
             closeMenu.setVisibility(View.VISIBLE);
         }
     }

     @Override
     public void onBackPressed() {
         if (leftDrawerLayout.isShownMenu()) {
             leftDrawerLayout.closeDrawer();
         }
         super.onBackPressed();
     }

     @Override
     protected void onDestroy() {
         EventBus.getDefault().unregister(this);
         super.onDestroy();
         if (exceptionBuilder != null) {
             exceptionBuilder.create().dismiss();
             exceptionBuilder = null;
             isExceptionDialogShow = false;
         }
         unregisterBroadcastReceiver();

         try {
             unregisterReceiver(internalDebugReceiver);
         } catch (Exception e) {
         }
     }

 }

