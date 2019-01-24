package com.sy.mingding.Activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.sy.mingding.R;

public class UserActivity extends Activity {

    private Toolbar mUserTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        initEvent();

    }

    private void initEvent() {

        //返回键
        mUserTopBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mUserTopBar = findViewById(R.id.user_top_bar);

    }
}
