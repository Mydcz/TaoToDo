package com.km.db;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.km.db.bean.CategoryBean;
import com.km.db.bean.TaskBean;
import com.km.db.dao.CategoryDao;
import com.km.db.dao.TaskDao;

/**
 * 数据库管理类
 * 用于数据库操作处理数据工具类
 */
@SuppressLint("StaticFieldLeak")
@Database(entities = {
        TaskBean.class,
        CategoryBean.class
}, version = DataBaseManage.VERSION, exportSchema = false)
public abstract class DataBaseManage extends RoomDatabase {
    public final static int VERSION = 1;
    private final static String DATA_BASE_NAME = "Tao.db";
    private static Context mContext;
    private static volatile DataBaseManage mInstance;

    public static void setContext(Context context) {
        mContext = context;
    }

    public DataBaseManage(){
    }

    public static DataBaseManage getInstance() {
        if (mContext != null) {
            if (mInstance == null) {
                synchronized (DataBaseManage.class) {
                    mInstance = Room.databaseBuilder(mContext, DataBaseManage.class, DATA_BASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return mInstance;
    }

    public abstract TaskDao getTaskDao();

    public abstract CategoryDao getCategoryDao();

}
