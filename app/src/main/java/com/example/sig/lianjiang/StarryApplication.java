package com.example.sig.lianjiang;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by sig on 2018/8/7.
 */

public class StarryApplication extends Application{
    public static Context applicationContext;
    private static StarryApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        applicationContext = this;
        instance = this;
//        SDKInitializer.initialize(applicationContext);
        //init demo helper
        StarryHelper.getInstance().init(applicationContext);

        // 初始化华为 HMS 推送服务
//        HMSPushHelper.getInstance().initHMSAgent(instance);
    }

    public static StarryApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
