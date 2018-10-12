package com.home.callrecordingtreasuredemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home.callrecordingtreasuredemo.service.CustomService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** 啟動監聽電話的服務 */
        Intent serviceIntent = new Intent(MainActivity.this, CustomService.class);
        startService(serviceIntent);
    }
}
