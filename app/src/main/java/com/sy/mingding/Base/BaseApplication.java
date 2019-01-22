package com.sy.mingding.Base;

import android.app.Application;

import com.sy.mingding.Utils.LogUtil;


public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LogUtils
        LogUtil.init(this.getPackageName(),false);
    }


}

