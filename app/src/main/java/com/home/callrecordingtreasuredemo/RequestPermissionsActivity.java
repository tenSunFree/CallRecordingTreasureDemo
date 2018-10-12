package com.home.callrecordingtreasuredemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class RequestPermissionsActivity extends AppCompatActivity {

    private FrameLayout writeExternalStorageFrameLayout, recordAudioFrameLayout,
            readPhoneStateFrameLayout, readContactsFrameLayout;
    private Button requestPermissionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        writeExternalStorageFrameLayout = findViewById(R.id.writeExternalStorageFrameLayout);
        recordAudioFrameLayout = findViewById(R.id.recordAudioFrameLayout);
        readPhoneStateFrameLayout = findViewById(R.id.readPhoneStateFrameLayout);
        readContactsFrameLayout = findViewById(R.id.readContactsFrameLayout);
        requestPermissionsButton =  findViewById(R.id.requestPermissionsButton);

        requestPermissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDynamicPermissions();
            }
        });

        requestDynamicPermissions();
    }

    /**
     * 同一組內的任意一個權限被授予, 其他的權限也會被授予
     * 請求需要的動態權限
     */
    private void requestDynamicPermissions() {
        XXPermissions.with(this)
                .permission(
                        Permission.SYSTEM_ALERT_WINDOW,                                         // 顯示在其他應用程式上層
                        Permission.WRITE_EXTERNAL_STORAGE,                                      // 存取儲存空間權限
                        Permission.RECORD_AUDIO,                                                 // 錄音權限
                        Permission.READ_PHONE_STATE,                                             // 管理通話權限
                        Permission.READ_CONTACTS)                                                // 存取聯絡人權限
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        Log.d("more", "hasPermission, granted: " + granted.size());
                        Log.d("more", "hasPermission, isAll: " + isAll);
                        for (int i = 0; i < granted.size(); i++) {
                            Log.d("more", "hasPermission, granted.get(" + i + "): " + granted.get(i));

                            switch (granted.get(i)) {
                                case "android.permission.WRITE_EXTERNAL_STORAGE":
                                    writeExternalStorageFrameLayout.setVisibility(View.GONE);
                                    break;
                                case "android.permission.RECORD_AUDIO":
                                    recordAudioFrameLayout.setVisibility(View.GONE);
                                    break;
                                case "android.permission.READ_PHONE_STATE":
                                    readPhoneStateFrameLayout.setVisibility(View.GONE);
                                    break;
                                case "android.permission.READ_CONTACTS":
                                    readContactsFrameLayout.setVisibility(View.GONE);
                                    break;
                            }
                        }

                        if (isAll) {
                            Intent intent = new Intent(
                                    RequestPermissionsActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        Log.d("more", "noPermission, denied: " + denied.size());
                        Log.d("more", "hasPermission, quick: " + quick);
                        for (int i = 0; i < denied.size(); i++) {
                            Log.d("more", "noPermission, denied.get(" + i + "): " + denied.get(i));
                        }
                    }
                });
    }
}
