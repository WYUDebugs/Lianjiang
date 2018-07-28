 package com.example.sig.lianjiang.activity;

 import android.os.Build;
 import android.os.Bundle;
 import android.support.v4.app.FragmentManager;
 import android.support.v7.app.AppCompatActivity;
 import android.view.Gravity;
 import android.view.LayoutInflater;
 import android.view.MotionEvent;
 import android.view.View;
 import android.view.Window;
 import android.view.WindowManager;
 import android.widget.FrameLayout;
 import android.widget.Toast;

 import com.example.sig.lianjiang.event.HideButtonEvent;
 import com.example.sig.lianjiang.event.ShowButtonEvent;
 import com.example.sig.lianjiang.fragment.DynamicFragment;
 import com.example.sig.lianjiang.fragment.FriendFragment;
 import com.example.sig.lianjiang.fragment.MenuLeftFragment;
 import com.example.sig.lianjiang.fragment.MessageFragment;
 import com.example.sig.lianjiang.fragment.StarFragment;
 import com.example.sig.lianjiang.leftmenu.FlowingView;
 import com.example.sig.lianjiang.leftmenu.LeftDrawerLayout;
 import com.example.sig.lianjiang.view.CircleImageView;
 import com.example.sig.lianjiang.view.DragDeleteTextView;
 import com.example.sig.lianjiang.view.MainNavigateTabBar;

 import de.greenrobot.event.EventBus;


 public class MainActivity extends AppCompatActivity implements View.OnClickListener{

     private static final String TAG_PAGE_MESSAGE = "消息";
     private static final String TAG_PAGE_FRIEND = "联系人";
     private static final String TAG_PAGE_PUBLISH = "发布";
     private static final String TAG_PAGE_DYNAMIC = "发现";
     private static final String TAG_PAGE_STAR = "星球";
     private FrameLayout continer,closeMenu;
     public static LeftDrawerLayout leftDrawerLayout;
     private FlowingView flowingView;


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
         setContentView(R.layout.activity_main);

         mNavigateTabBar = (MainNavigateTabBar) findViewById(R.id.mainTabBar);
         //messageNum=(DragDeleteTextView)findViewById(R.id.message_num);
         mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

         mNavigateTabBar.addTab(MessageFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.message, R.mipmap.message_select, TAG_PAGE_MESSAGE));
         mNavigateTabBar.addTab(FriendFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.friend, R.mipmap.friend_select, TAG_PAGE_FRIEND));
         mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, TAG_PAGE_PUBLISH));
         mNavigateTabBar.addTab(DynamicFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.dynamic, R.mipmap.dynamic_select, TAG_PAGE_DYNAMIC));
         mNavigateTabBar.addTab(StarFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.star, R.mipmap.star_select, TAG_PAGE_STAR));
         EventBus.getDefault().register(this);
         init();
     }


     @Override
     protected void onSaveInstanceState(Bundle outState) {
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
     }

 }

