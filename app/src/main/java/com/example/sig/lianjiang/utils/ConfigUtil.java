package com.example.sig.lianjiang.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
/**
 * Created by sig on 2018/9/13.
 */

public class ConfigUtil
{
    // 引导图更换后，这里要升级一下引导图的版本后，这样，用户就能看到新的引导图了
    private static final int GUIDE_VERSION = 1;

    private static final String WELCOME_GUIDE = "welcome_guide";

    /**
     * 是否显示启动引导图
     *
     * @return
     */
    public static boolean needShowGuide(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int version = sharedPrefs.getInt(WELCOME_GUIDE, 0);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(WELCOME_GUIDE, GUIDE_VERSION);
        editor.commit();

        if (version != GUIDE_VERSION) {
            return true;
        }

        return false;
    }
}
