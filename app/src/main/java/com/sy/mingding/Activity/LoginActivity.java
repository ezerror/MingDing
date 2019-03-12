package com.sy.mingding.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sy.mingding.Base.BaseActivity;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Model.UserModel;
import com.sy.mingding.Utils.IMMLeaks;
import com.sy.mingding.event.RefreshEvent;
import com.sy.mingding.widget.AutoFillEmailEditText;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity {

    private AutoFillEmailEditText mUserNameET;
    private EditText mPassWordET;
    private AppCompatButton mRegisterButton;
    private AppCompatTextView mLoginButton;

    private static final String TAG = "LoginActivity";
    private View mRootContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityManager.addActivity(this);
        initView();
        initEvent();





    }
    private void initEvent() {

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNameET.getText().toString();
                String password = mPassWordET.getText().toString();
                UserModel.getInstance().login(username, password, new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            ActivityManager.exit();
                            startActivity(MainActivity.class, null, true);
                        } else {
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")" );
                        }
                    }
                });
            }
        });


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNameET.getText().toString();
                String password = mPassWordET.getText().toString();
                UserModel.User_signUp(mRootContentView,username,password);
            }
        });

        //判断是否已经登录
        if (BmobUser.isLogin()) {
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void initView() {
        mUserNameET = findViewById(R.id.username_et);
        mPassWordET = findViewById(R.id.password);
        mRegisterButton = findViewById(R.id.regist);
        mLoginButton = findViewById(R.id.login);
        mRootContentView = getWindow().getDecorView().findViewById(android.R.id.content);


    }
    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        //checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }


}
