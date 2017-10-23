package com.mtf.citypicker.db;

import android.database.sqlite.SQLiteDatabase;

import com.mtf.citypicker.MyApplication;
import com.mtf.citypicker.green.DaoMaster;
import com.mtf.citypicker.green.DaoSession;

/**
 * 数据库操作类
 * Created by mtf on 2017/10/18.
 */

public class GreenDaoUtils {
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static class GreenDaoUtilsHolder {
        private static final GreenDaoUtils INSTANCE = new GreenDaoUtils();
    }

    private GreenDaoUtils() {
    }

    public static GreenDaoUtils getSingleton() {
        return GreenDaoUtilsHolder.INSTANCE;
    }

    private void initGreenDao() {
        //临时数据使用
        SQLdm s = new SQLdm();
        db = s.openDatabase(MyApplication.getInstances());
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        if (mDaoMaster == null) {
            initGreenDao();
        }
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        if (db == null) {
            initGreenDao();
        }
        return db;
    }
}
