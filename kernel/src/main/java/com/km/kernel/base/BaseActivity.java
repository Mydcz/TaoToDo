package com.km.kernel.base;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

/**
 * Activity基类
 *
 * @param <T>
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T mBinding;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = getViewBinding();
        setContentView(mBinding.getRoot());
        initView();
        //定义初始化数据的方法
        initData();
    }

    protected abstract T getViewBinding();

    protected abstract void initView();

    protected abstract void initData();

    public void showActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
