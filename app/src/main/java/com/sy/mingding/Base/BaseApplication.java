package com.sy.mingding.Base;

import android.app.Application;

import com.sy.mingding.Utils.LogUtil;

import cn.bmob.v3.Bmob;


public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LogUtils
        LogUtil.init(this.getPackageName(),false);

        //初始化Bmob
        Bmob.initialize(this,"67ad7af91544222fc4c4bab406ebe024");
    }


}

