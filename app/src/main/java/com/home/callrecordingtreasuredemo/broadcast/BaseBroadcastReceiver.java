package com.home.callrecordingtreasuredemo.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public abstract class BaseBroadcastReceiver extends BroadcastReceiver
        implements IntentFilterBroadcastReceiver {

    @Override
    public IntentFilter getIntentFilter() {
        return null;
    }
}
