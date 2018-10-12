package com.home.callrecordingtreasuredemo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.home.callrecordingtreasuredemo.MainActivity;
import com.home.callrecordingtreasuredemo.R;
import com.home.callrecordingtreasuredemo.broadcast.DynamicBroadcastReceiver;
import com.home.callrecordingtreasuredemo.widget.ShowPhoneDialog;

import static com.home.callrecordingtreasuredemo.CustomApplication.CHANNEL_ID;

public class CustomService extends Service {

    public static CustomService customService;
    private Notification notification;
    private DynamicBroadcastReceiver dynamicBroadcastReceiver;
    private String dialedPhoneNumber = "", acceptedPhoneNumber = "";
    private ShowPhoneDialog showPhoneDialog;

    public CustomService() {
        customService = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /** 創建3組PendingIntent, 分別包含著不同的webViewUrl */
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(                                       // PendingIntent可以看作是对Intent的包装, 当点击消息时就会向系统发送mainIntent意图, 就算在执行时当前Application已经不存在了, 也能通过存在PendingIntent里的Application的Context 照样执行Intent
                this, 1, mainIntent, 0);

        /** 实例化NotificationCompat.Builder对象 */
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("服務準備就緒")
                .setContentText("顯示該通知目的是阻止系統自動殺死錄音任務")
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);                                                        // 开始前台服务, 参数1 唯一的通知标识, 参数2 通知消息
        return START_NOT_STICKY;                                                                  // 如果在执行完onStartCommand后, 服务被异常kill掉, 系统不会自动重启该服务
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 註冊 電話的廣播
     */
    private void registerReceiver() {
        dynamicBroadcastReceiver = new DynamicBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                } else {

                    /** 如果是来电 */
                    TelephonyManager tManager = (TelephonyManager) context
                            .getSystemService(Service.TELEPHONY_SERVICE);

                    /** 电话的状态 */
                    switch (tManager.getCallState()) {

                        /** 如果有來電, 則進入等待接听状态 */
                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.d("more", "CALL_STATE_RINGING, 等待接听状态");
                            acceptedPhoneNumber = intent.getStringExtra("incoming_number");  // 获取來電的手机号码
                            break;

                        /** 接听状态 */
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.d("more", "CALL_STATE_OFFHOOK, 接听状态");
                            dialedPhoneNumber = intent.getStringExtra("incoming_number");
                            break;

                        /** 挂断状态 */
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.d("more", "CALL_STATE_IDLE, 挂断状态");
                            if (!dialedPhoneNumber.equals("")) {
                                showPhoneDialog("撥打電話", "電話號碼: " + dialedPhoneNumber);
                            } else if (!acceptedPhoneNumber.equals("")) {
                                showPhoneDialog("接聽電話", "電話號碼: " + acceptedPhoneNumber);
                            }
                            break;
                    }
                }
            }
        };
        registerReceiver(dynamicBroadcastReceiver, dynamicBroadcastReceiver.getIntentFilter());
    }

    /**
     * 顯示PhoneDialog
     */
    private void showPhoneDialog(String title, String content) {
        showPhoneDialog =
                new ShowPhoneDialog(
                        this, R.style.ShowPhoneDialog);

        /** 设置窗口类型的时候判断是否为8.0及以上系统, 然后进行不同的设置 */
        WindowManager.LayoutParams lp = showPhoneDialog.getWindow().getAttributes();
        lp.width = 160;                                                                            // 设置宽度
        lp.height = 90;                                                                            // 设置高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        showPhoneDialog.getWindow().setAttributes(lp);

        showPhoneDialog.setTitleText(title);
        showPhoneDialog.setContentText(content);
        showPhoneDialog.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhoneDialog.dismiss();
            }
        });
        showPhoneDialog.show();

        /** 將儲存的電話號碼清空 */
        dialedPhoneNumber = "";
        acceptedPhoneNumber = "";
    }
}
