package com.example.sig.lianjiang.receiver;
import com.hyphenate.chat.EMChatService;
import com.hyphenate.util.EMLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sig on 2018/8/6.
 */

public class StartServiceReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                && !intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
            return;
        }
        EMLog.d("boot", "start IM service on boot");
        Intent startServiceIntent=new Intent(context, EMChatService.class);
        startServiceIntent.putExtra("reason", "boot");
        try {
            context.startService(startServiceIntent);
        } catch (Exception e) {
            EMLog.d("EMMonitorReceiver", "exception in start service, e: " + e.getMessage());
        }
    }
}

