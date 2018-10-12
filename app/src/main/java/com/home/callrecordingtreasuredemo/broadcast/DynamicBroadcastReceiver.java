package com.home.callrecordingtreasuredemo.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class DynamicBroadcastReceiver extends BaseBroadcastReceiver {

    private IntentFilter intentFilter;

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    @Override
    public IntentFilter getIntentFilter() {
        intentFilter = new IntentFilter();
        intentFilter.setPriority(1000);                                                             // 如果多个receiver满足响应的条件, 系统会优先触发prioriyt搞的那个receive
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");                          // 主动打电话时, 发送的广播
        intentFilter.addAction("android.intent.action.PHONE_STATE");                                // 电话状态改变时, 发送的广播(振铃,挂断,接通)
        return intentFilter;
    }
}
