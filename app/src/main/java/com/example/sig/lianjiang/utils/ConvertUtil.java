package com.example.sig.lianjiang.utils;

import android.content.Context;

/**
 * Created by sig on 2018/9/13.
 */

public class ConvertUtil {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        int px = (int) (dipValue * scale + 0.5f);

        return px;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
