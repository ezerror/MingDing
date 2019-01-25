package com.sy.mingding.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class Dialog {
    private static ProgressDialog progressDialog;

    public static void showWaiting(Context context,String msg,int time) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
        progressDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();

            }
        }, time);

    }
}
