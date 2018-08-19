package com.example.sig.lianjiang.utils;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import com.example.sig.lianjiang.R;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * Created by sig on 2018/8/19.
 */

public class PopupMenuUtil implements View.OnClickListener{

    private View rootVew;
    private PopupWindow popupWindow;
    private Context context;
    private RelativeLayout rlClick;//发布的布局
    private ImageView ivBtn; //发布的图标
    //图标
    private LinearLayout llTest1, llTest2;
    float animatorProperty[] = null;//动画执行的 属性值数组
    int top = 0;    //第一排图 距离屏幕底部的距离
    int bottom = 0; //第二排图 距离屏幕底部的距离

    private void _createView(final Context context) {
        this.context=context;
        //弹窗的布局
        rootVew = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);
        //popupWindow类的构造方法的四个参数
        // 一:popupWindow的布局,二:popupWindow的宽,三:popupWindow的高,四:设置popupWindow失去焦点
        popupWindow = new PopupWindow(rootVew,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                false);
        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        //popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);

        if (animatorProperty == null) {
            top = dip2px(context, 310);
            bottom = dip2px(context, 210);
            animatorProperty = new float[]{bottom, 60, -30, -20 - 10, 0};
        }

        initLayout(context);
    }
    //将dp转化为px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    //初始化
    private void initLayout(Context context) {
        rlClick = (RelativeLayout) rootVew.findViewById(R.id.pop_rl_click);
        ivBtn = (ImageView) rootVew.findViewById(R.id.pop_iv_img);
        llTest1 = (LinearLayout) rootVew.findViewById(R.id.test1);
        llTest2 = (LinearLayout) rootVew.findViewById(R.id.test2);

        ivBtn.setOnClickListener(this);
        rlClick.setOnClickListener(this);
        llTest1.setOnClickListener(this);
        llTest2.setOnClickListener(this);

    }
    //点击事件,打开popupWindow后如果再次点击加号图标,调用_rlClickAction()关闭弹窗
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pop_iv_img) {
            _rlClickAction();
        } else {
            Toast.makeText(context, "点击", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 刚打开popupWindow 执行的动画
     */
    private void _openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        _startAnimation(llTest1, 500, animatorProperty);
        _startAnimation(llTest2, 500, animatorProperty);
    }
    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            _closeAnimation(llTest1, 300, top);
            _closeAnimation(llTest2, 300, top);

            rlClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 300);

        }
    }
    /**
     * 关闭popupWindow
     */

    public void _close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 弹起 popupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void _show(Context context, View parent) {
        _createView(context);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            _openPopupWindowAction();
        }
    }

    //检测popupWindow弹窗的状态
    public boolean _isShowing() {
        if (popupWindow == null) {
            return false;
        } else {
            return popupWindow.isShowing();
        }
    }

    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     mView
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void _closeAnimation(View view, int duration, int next) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }


}
