package com.km.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.km.pocketsphinx.PocketListener;
import com.km.pocketsphinx.PocketSphinxService;
import com.km.pocketsphinx.PocketSphinxUtil;
import com.km.pocketsphinx.kit.RecognizerSetupListener;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private Intent serviceIntent;
    private PocketSphinxUtil pocketSphinxUtil;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.request_permission).setOnClickListener(v -> onRecordAudioGranted());
        findViewById(R.id.start_service).setOnClickListener(v -> {
            if (hasRecordAudioPermission()) {
                startPocketSphinxService();
            } else {
                Toast.makeText(MainActivity.this, "请先申请权限", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.start_record).setOnClickListener(v -> {
            if (serviceIntent == null) {
                Toast.makeText(MainActivity.this, "请先开启服务", Toast.LENGTH_SHORT).show();
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(MainActivity.this::forTest, 1000);
            }
        });
        findViewById(R.id.stop_record).setOnClickListener(v -> {
            if (pocketSphinxUtil != null) {
                pocketSphinxUtil.stopRecord();
            }
        });
    }

    private boolean hasRecordAudioPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO);
    }

    @AfterPermissionGranted(100)
    private void onRecordAudioGranted() {
        if (hasRecordAudioPermission()) {
            Toast.makeText(this, "已获取录音权限", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, 100, Manifest.permission.RECORD_AUDIO).build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void startPocketSphinxService() {
        serviceIntent = new Intent();
        serviceIntent.setClass(this, PocketSphinxService.class);
        startService(serviceIntent);
        Toast.makeText(this, "PocketSphinxService 启动成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pocketSphinxUtil != null) {
            pocketSphinxUtil.stopRecord();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
    }


    private void forTest() {
        pocketSphinxUtil = PocketSphinxUtil.get();
        if (pocketSphinxUtil == null) {
            return;
        }
        pocketSphinxUtil.runRecognizerSetup(new RecognizerSetupListener() {
            @Override
            public void onRecognizerAlreadySetup() {

            }

            @Override
            public Exception doInBackGround() {
                return null;
            }

            @Override
            public void onRecognizerPrepareError() {

            }

            @Override
            public void onRecognizerPrepareSuccess() {

            }
        });

        pocketSphinxUtil.startRecord("zh_test", new PocketListener() {
            @Override
            public void onSpeechStart() {

            }

            @Override
            public void onSpeechResult(List<String> strings) {
                Log.d("MainActivity", "find result =" + strings);
                if (strings == null) {
                    return;
                }
                for (String string : strings) {
                    Log.d("MainActivity", "find string =" + string);
                }


            }

            @Override
            public void onSpeechError(String error) {

            }
        });
    }


}
