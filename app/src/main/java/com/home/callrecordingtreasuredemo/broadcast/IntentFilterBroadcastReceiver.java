package com.home.callrecordingtreasuredemo.broadcast;

import android.content.IntentFilter;

public interface IntentFilterBroadcastReceiver {

    /**
     * 声明基础的提供 intent过滤器方法
     */
    IntentFilter getIntentFilter();
}
