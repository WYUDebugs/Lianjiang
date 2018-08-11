package com.example.sig.lianjiang.receiver;

/**
 * Created by sig on 2018/8/6.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.sig.lianjiang.StarryHelper;
import com.example.sig.lianjiang.activity.VideoCallActivity;
import com.example.sig.lianjiang.activity.VoiceCallActivity;
import com.hyphenate.util.EMLog;

public class CallReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!StarryHelper.getInstance().isLoggedIn())
            return;
        //username
        String from = intent.getStringExtra("from");
        //call type
        String type = intent.getStringExtra("type");
        if("video".equals(type)){ //video call
            context.startActivity(new Intent(context, VideoCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else{ //voice call
            context.startActivity(new Intent(context, VoiceCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        EMLog.d("CallReceiver", "app received a incoming call");
    }

}

