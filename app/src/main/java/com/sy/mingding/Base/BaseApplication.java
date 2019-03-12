package com.sy.mingding.Base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.sy.mingding.IM.MessageHandler;
import com.sy.mingding.Utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;


public class BaseApplication extends Application {

    private static BaseApplication INSTANCE;
    public static BaseApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(BaseApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(BaseApplication a) {
        BaseApplication.INSTANCE = a;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LogUtils
        LogUtil.init(this.getPackageName(),false);

        //初始化Bmob
        //Bmob.initialize(this,"67ad7af91544222fc4c4bab406ebe024");
        //初始化Bmob IM
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new MessageHandler(this));
        }


    }

    /**全局handler
     *
     * */
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


    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

