package com.sy.mingding.Base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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


    //全局handler
    public   static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListener.heandleMessage(msg);
        }
    };

    private static HandlerListener mListener;
    public static void setOnHandlerListener(HandlerListener listener) {
        mListener = listener;
    }
    public  static HandlerListener getListener(){
        return mListener;
    }

    public  interface HandlerListener {
        void heandleMessage(Message msg);
    }


}

