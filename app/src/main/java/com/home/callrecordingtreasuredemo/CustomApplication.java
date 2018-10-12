package com.home.callrecordingtreasuredemo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class CustomApplication extends Application {

    public static final String CHANNEL_ID = "examplesServiceChannel";
    private List<NotificationChannel> notificationChannels;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationChannels = new ArrayList<NotificationChannel>();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                                      // Build.VERSION.SDK_INT是系统的版本

            /** 获取NotificationManager, 並創建通知渠道 */
            NotificationChannel serviceNotificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Examples Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT                                      // IMPORTANCE_HIGH, 开启通知会弹出, 发出提示音, 状态栏中显示
            );

            notificationChannels.add(serviceNotificationChannel);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannels(notificationChannels);
        }
    }
}
