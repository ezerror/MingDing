package com.sy.mingding.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Utils.BeanUtils.UserUtil;
import com.sy.mingding.widget.AutoFillEmailEditText;

import cn.bmob.v3.BmobUser;

public class LoginActivity extends Activity  {

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
                UserUtil.user_login(mRootContentView,getApplicationContext(),username,password);
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNameET.getText().toString();
                String password = mPassWordET.getText().toString();
                UserUtil.User_signUp(mRootContentView,username,password);
            }
        });

        //判断是否已经登录
        if (BmobUser.isLogin()) {
            Intent intent =new Intent(LoginActivity.this,MainViewActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void initView() {
        mUserNameET = findViewById(R.id.username_et);
        mPassWordET = findViewById(R.id.password);
        mRegisterButton = findViewById(R.id.regist);
        mLoginButton = findViewById(R.id.login);
        mRootContentView = getWindow().getDecorView().findViewById(android.R.id.content);


    }


}
