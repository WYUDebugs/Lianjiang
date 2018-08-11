package com.example.sig.lianjiang.receiver;
import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;

/**
 * Created by sig on 2018/8/6.
 */

public class HMSPushReceiver extends PushReceiver{
    private static final String TAG = HMSPushReceiver.class.getSimpleName();

    @Override
    public void onToken(Context context, String token, Bundle extras){
        //没有失败回调，假定token失败时token为null
        if(token != null && !token.equals("")){
            EMLog.d("HWHMSPush", "register huawei hms push token success token:" + token);
            EMClient.getInstance().sendHMSPushTokenToServer("10492024", token);
        }else{
            EMLog.e("HWHMSPush", "register huawei hms push token fail!");
        }
    }
}

