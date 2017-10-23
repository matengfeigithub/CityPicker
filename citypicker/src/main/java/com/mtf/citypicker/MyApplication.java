package com.mtf.citypicker;

import android.app.Application;

import com.mtf.citypicker.db.GreenDaoUtils;


/**
 * Created by mtf on 2017/6/6.
 */

public class MyApplication extends Application {

    public static MyApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        GreenDaoUtils.getSingleton();//数据库初始化
    }

    public static MyApplication getInstances() {
        return instances;
    }
}
