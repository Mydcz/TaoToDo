package com.km.kernel;

import android.app.Application;
import android.content.Context;

import com.km.db.DataBaseManage;

/**
 * 核心Application
 * 用于基础初始化操作，如数据库初始化等
 * 注：如果有多个module，每个module都需要继承该Application
 *    如有耗时操作请放置异步，避免APP启动耗时。
 */
public class KernelApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //初始化数据库上下文对象
        DataBaseManage.setContext(mContext);
        DataBaseManage.getInstance();
    }

    public static Context getContext() {
        return mContext;
    }
}
