package com.example.sig.lianjiang.view;
//import android.app.Fragment;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.lianjiang.adapter.MessageAdapter;
import com.example.sig.lianjiang.db.InviteMessgeDao;
import com.example.sig.lianjiang.fragment.DynamicFragment;
import com.example.sig.lianjiang.fragment.FriendFragment;
import com.example.sig.lianjiang.fragment.MessageFragment;
import com.example.sig.lianjiang.utils.ThemeUtils;


import com.example.sig.lianjiang.R;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sig on 2018/7/9.
 */

public class MainNavigateTabBar extends LinearLayout implements View.OnClickListener {

    private static final String KEY_CURRENT_TAG = "com.startsmake.template。currentTag";
    private List<ViewHolder> mViewHolderList;
    private OnTabSelectedListener mTabSelectListener;
    private FragmentActivity mFragmentActivity;
    private String mCurrentTag;
    private String mRestoreTag;
    /*主内容显示区域View的id*/
    private int mMainContentLayoutId;
    /*选中的Tab文字颜色*/
    private ColorStateList mSelectedTextColor;
    /*正常的Tab文字颜色*/
    private ColorStateList mNormalTextColor;
    /*Tab文字的颜色*/
    private float mTabTextSize;
    /*默认选中的tab index*/
    private int mDefaultSelectedTab = 1;

    private int mCurrentSelectedTab;

    private UpdateTabBar mUpdateTabBar;

    private InviteMessgeDao inviteMessgeDao=new InviteMessgeDao(getContext());

    private Fragment mfragment=null;


    public MainNavigateTabBar(Context context) {
        this(context, null);

    }

    public MainNavigateTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainNavigateTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MainNavigateTabBar, 0, 0);

        ColorStateList tabTextColor = typedArray.getColorStateList(R.styleable.MainNavigateTabBar_navigateTabTextColor);
        ColorStateList selectedTabTextColor = typedArray.getColorStateList(R.styleable.MainNavigateTabBar_navigateTabSelectedTextColor);

        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.MainNavigateTabBar_navigateTabTextSize, 0);
        mMainContentLayoutId = typedArray.getResourceId(R.styleable.MainNavigateTabBar_containerId, 0);

        mNormalTextColor = (tabTextColor != null ? tabTextColor : context.getResources().getColorStateList(R.color.tab_text_normal));


        if (selectedTabTextColor != null) {
            mSelectedTextColor = selectedTabTextColor;
        } else {
            ThemeUtils.checkAppCompatTheme(context);
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            mSelectedTextColor = context.getResources().getColorStateList(typedValue.resourceId);
        }

        mViewHolderList = new ArrayList<>();
    }

    /*
    *回调接口
     */

    public interface UpdateTabBar{
        public int messageNum();
        public boolean friendTip();
        public boolean DynamicTip();
        public boolean starTip();
        public void cleanMessage();
    }

    public void setUpdateMessageNum(UpdateTabBar updateTabBar){
        mUpdateTabBar=updateTabBar;
    }

    public void addTab(Class frameLayoutClass, TabParam tabParam) {

        int defaultLayout = R.layout.comui_tab_view;
//        if (tabParam.tabViewResId > 0) {
//            defaultLayout = tabParam.tabViewResId;
//        }
        if (TextUtils.isEmpty(tabParam.title)) {
            tabParam.title = getContext().getString(tabParam.titleStringRes);
        }

        View view = LayoutInflater.from(getContext()).inflate(defaultLayout, null);
        view.setFocusable(true);

        ViewHolder holder = new ViewHolder();

        holder.tabIndex = mViewHolderList.size();

        holder.fragmentClass = frameLayoutClass;
        holder.tag = tabParam.title;
        holder.pageParam = tabParam;

        holder.tabIcon = (ImageView) view.findViewById(R.id.tab_icon);
        holder.tabTitle = ((TextView) view.findViewById(R.id.tab_title));
        holder.unread_msg_number=(DragDeleteTextView) view.findViewById(R.id.unread_msg_number);
        holder.tab_tip=(ImageView)view.findViewById(R.id.tab_tip);
        holder.unread_msg_number.setOnDeleteTextListener(new DragDeleteTextView.OnDeleteTextListener() {
            @Override
            public void onDelete(View view) {
//                mUpdateTabBar.cleanMessage();
                Toast.makeText(getContext(),"2333",Toast.LENGTH_SHORT).show();
            }
        });
        if (TextUtils.isEmpty(tabParam.title)) {
            holder.tabTitle.setVisibility(View.INVISIBLE);
        } else {
            holder.tabTitle.setText(tabParam.title);                        //修改标志
        }

        if (mTabTextSize != 0) {
            holder.tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
        }
        if (mNormalTextColor != null) {
            holder.tabTitle.setTextColor(mNormalTextColor);
        }

        if (tabParam.backgroundColor > 0) {
            view.setBackgroundResource(tabParam.backgroundColor);
        }

        if (tabParam.iconResId > 0) {
            holder.tabIcon.setImageResource(tabParam.iconResId);
        } else {
            holder.tabIcon.setVisibility(View.INVISIBLE);
        }

        if (tabParam.iconResId > 0 && tabParam.iconSelectedResId > 0) {
            view.setTag(holder);
            view.setOnClickListener(this);
            mViewHolderList.add(holder);
        }

        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
        refreshTip();

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mMainContentLayoutId == 0) {
            throw new RuntimeException("mFrameLayoutId Cannot be 0");
        }
        if (mViewHolderList.size() == 0) {
            throw new RuntimeException("mViewHolderList.size Cannot be 0, Please call addTab()");
        }
        if (!(getContext() instanceof FragmentActivity)) {
            throw new RuntimeException("parent activity must is extends FragmentActivity");
        }
        mFragmentActivity = (FragmentActivity) getContext();

        ViewHolder defaultHolder = null;

        hideAllFragment();
        if (!TextUtils.isEmpty(mRestoreTag)) {
            for (ViewHolder holder : mViewHolderList) {
                if (TextUtils.equals(mRestoreTag, holder.tag)) {
                    defaultHolder = holder;
                    mRestoreTag = null;
                    break;
                }
            }
        } else {
            defaultHolder = mViewHolderList.get(mDefaultSelectedTab);
        }

        showFragment(defaultHolder);
    }

    @Override
    public void onClick(View v) {
        Object object = v.getTag();
        if (object != null && object instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) v.getTag();
            showFragment(holder);
            if (mTabSelectListener != null) {
                mTabSelectListener.onTabSelected(holder);
            }
        }
    }
    public void refreshTip(){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                for (ViewHolder holder : mViewHolderList){
                    showTip(holder);
                    refreshFragment(holder);
                }

            }
        });
    }
    public void refreshFragment(ViewHolder holder){
        mFragmentActivity = (FragmentActivity) getContext();
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();

        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
        if (fragment == null) {
            fragment = getFragmentInstance(holder.tag);
            transaction.add(mMainContentLayoutId, fragment, holder.tag);
        } else {
            if(holder.tabIndex==0){
                ((MessageFragment)fragment).refresh();
            }else if(holder.tabIndex==1){
                ((FriendFragment)fragment).refresh();
            }else if(holder.tabIndex==2){
//                ((DynamicFragment)fragment).refresh();
            }else {
//
            }

        }

        Log.e("1234",Integer.toString(mCurrentSelectedTab));
    }
    public void refreshHead(){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                for (ViewHolder holder : mViewHolderList){
//                    showTip(holder);
                    refreshFragmentHead(holder);
                }

            }
        });
    }
    public void refreshFragmentHead(ViewHolder holder){
        mFragmentActivity = (FragmentActivity) getContext();
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();

        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
        if (fragment == null) {
            fragment = getFragmentInstance(holder.tag);
            transaction.add(mMainContentLayoutId, fragment, holder.tag);
        } else {
            if(holder.tabIndex==0){
                ((MessageFragment)fragment).update();
            }else if(holder.tabIndex==1){
                ((FriendFragment)fragment).refresh();
            }else if(holder.tabIndex==2){
//                ((DynamicFragment)fragment).refresh();
            }else {
//
            }

        }

        Log.e("1234",Integer.toString(mCurrentSelectedTab));
    }
    private void showTip(ViewHolder holder){
        if(holder.tabIndex==0){
            int num= EMClient.getInstance().chatManager().getUnreadMsgsCount();;
            if(num>0&&num<100){
                holder.unread_msg_number.setVisibility(View.VISIBLE);
                holder.unread_msg_number.setText(Integer.toString(num));
            }else if(num>=100){
                holder.unread_msg_number.setVisibility(View.VISIBLE);
                holder.unread_msg_number.setText("99+");
            }else {
                holder.unread_msg_number.setVisibility(View.INVISIBLE);
            }
            holder.tab_tip.setVisibility(View.INVISIBLE);

            Log.e("123","1");
        }else if(holder.tabIndex==1){
            holder.unread_msg_number.setVisibility(View.INVISIBLE);
            int count=inviteMessgeDao.getUnreadMessagesCount();
            boolean tip;
            if(count>0){
                tip=true;
            }else {
                tip=false;
            }
            if(tip){
                holder.tab_tip.setVisibility(View.VISIBLE);
            }else {
                holder.tab_tip.setVisibility(View.INVISIBLE);
            }
            Log.e("123","2");
        }else if(holder.tabIndex==2){
            holder.unread_msg_number.setVisibility(View.INVISIBLE);
//            boolean tip=mUpdateTabBar.DynamicTip();
            boolean tip=false;
            if(tip){
                holder.tab_tip.setVisibility(View.VISIBLE);
            }else {
                holder.tab_tip.setVisibility(View.INVISIBLE);
            }
            Log.e("123","3");
        }else{
            holder.unread_msg_number.setVisibility(View.INVISIBLE);
//            boolean tip=mUpdateTabBar.starTip();
            boolean tip=false;
            if(tip){
                holder.tab_tip.setVisibility(View.VISIBLE);
            }else {
                holder.tab_tip.setVisibility(View.INVISIBLE);
            }
            Log.e("123","4");
        }
    }
    /**
     * 显示 holder 对应的 fragment
     *
     * @param holder
     */
    private void showFragment(ViewHolder holder) {
        showTip(holder);
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        if (isFragmentShown(transaction, holder.tag)) {
            return;
        }
        setCurrSelectedTabByTag(holder.tag);

        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
        if (fragment == null) {
            fragment = getFragmentInstance(holder.tag);
            transaction.add(mMainContentLayoutId, fragment, holder.tag);
        } else {
            if(holder.tabIndex==0){
                ((MessageFragment)fragment).refresh();
            }else if(holder.tabIndex==1){
                ((FriendFragment)fragment).refresh();
            }else if(holder.tabIndex==2){
//                ((DynamicFragment)fragment).refresh();
            }else {
//
            }
            transaction.show(fragment);
        }
        transaction.commit();
        mCurrentSelectedTab = holder.tabIndex;
        Log.e("1234",Integer.toString(mCurrentSelectedTab));
    }

    private boolean isFragmentShown(FragmentTransaction transaction, String newTag) {
        if (TextUtils.equals(newTag, mCurrentTag)) {
            return true;
        }

        if (TextUtils.isEmpty(mCurrentTag)) {
            return false;
        }

        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(mCurrentTag);
        if (fragment != null && !fragment.isHidden()) {
            transaction.hide(fragment);
        }

        return false;
    }

    /*设置当前选中tab的图片和文字颜色*/
    private void setCurrSelectedTabByTag(String tag) {
        if (TextUtils.equals(mCurrentTag, tag)) {
            return;
        }
        for (ViewHolder holder : mViewHolderList) {
            if (TextUtils.equals(mCurrentTag, holder.tag)) {
                holder.tabIcon.setImageResource(holder.pageParam.iconResId);
                holder.tabTitle.setTextColor(mNormalTextColor);
            } else if (TextUtils.equals(tag, holder.tag)) {
                holder.tabIcon.setImageResource(holder.pageParam.iconSelectedResId);
                holder.tabTitle.setTextColor(mSelectedTextColor);
            }
        }
        mCurrentTag = tag;
    }


    private Fragment getFragmentInstance(String tag) {
        Fragment fragment = null;
        for (ViewHolder holder : mViewHolderList) {
            if (TextUtils.equals(tag, holder.tag)) {
                try {
                    fragment = (Fragment) Class.forName(holder.fragmentClass.getName()).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return fragment;
    }

    private void hideAllFragment() {
        if (mViewHolderList == null || mViewHolderList.size() == 0) {
            return;
        }
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();

        for (ViewHolder holder : mViewHolderList) {
            Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
            if (fragment != null && !fragment.isHidden()) {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    public void setSelectedTabTextColor(ColorStateList selectedTextColor) {
        mSelectedTextColor = selectedTextColor;
    }

    public void setSelectedTabTextColor(int color) {
        mSelectedTextColor = ColorStateList.valueOf(color);
    }

    public void setTabTextColor(ColorStateList color) {
        mNormalTextColor = color;
    }

    public void setTabTextColor(int color) {
        mNormalTextColor = ColorStateList.valueOf(color);
    }

    public void setFrameLayoutId(int frameLayoutId) {
        mMainContentLayoutId = frameLayoutId;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRestoreTag = savedInstanceState.getString(KEY_CURRENT_TAG);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_CURRENT_TAG, mCurrentTag);
    }

    private static class ViewHolder {
        public String tag;
        public TabParam pageParam;
        public ImageView tabIcon;
        public TextView tabTitle;
        public Class fragmentClass;
        public int tabIndex;
        public DragDeleteTextView unread_msg_number;
        public ImageView tab_tip;
    }


    public static class TabParam {
        public int backgroundColor = android.R.color.white;
        public int iconResId;
        public int iconSelectedResId;
        public int titleStringRes;
        //        public int tabViewResId;
        public String title;


        public TabParam(int iconResId, int iconSelectedResId, String title) {
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.title = title;
        }

            public TabParam(int iconResId, int iconSelectedResId, int titleStringRes) {
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.titleStringRes = titleStringRes;
        }

        public TabParam(int backgroundColor, int iconResId, int iconSelectedResId, int titleStringRes) {
            this.backgroundColor = backgroundColor;
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.titleStringRes = titleStringRes;
        }

        public TabParam(int backgroundColor, int iconResId, int iconSelectedResId, String title) {
            this.backgroundColor = backgroundColor;
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.title = title;
        }
    }


    public interface OnTabSelectedListener {
        void onTabSelected(ViewHolder holder);
    }

    public void setTabSelectListener(OnTabSelectedListener tabSelectListener) {
        mTabSelectListener = tabSelectListener;
    }

    public void setDefaultSelectedTab(int index) {
        if (index >= 0 && index < mViewHolderList.size()) {
            mDefaultSelectedTab = index;
        }
    }

    public void setCurrentSelectedTab(int index) {
        if (index >= 0 && index < mViewHolderList.size()) {
            ViewHolder holder = mViewHolderList.get(index);
            showFragment(holder);
        }
    }

    public int getCurrentSelectedTab(){
        return mCurrentSelectedTab;
    }

    public Fragment getMfragment(){
        return mfragment;
    }


}

