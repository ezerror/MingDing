package com.sy.mingding.Activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Utils.UserUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends Activity {

    private Toolbar mSettingsTopBar;
    private TextView mUsername;
    private View mRootContentView;
    private ImageView mProfileImage;
    private TextView mTipLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActivityManager.addActivity(this);
        initView();
        initEvent();

    }

    private void initEvent() {
        //返回键
        mSettingsTopBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh();

        //进入用户界面
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SettingActivity.this,UserSettingActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        refresh();
    }

    private void refresh() {
        //更新
        User user =UserUtil.get_user();
        mUsername.setText(user.getNickname());
        mTipLogin.setVisibility(View.INVISIBLE);
        if(user.getIcon()!=null){
            Picasso.with(this)
                    .load(user.getIcon().getUrl())
                    .placeholder(R.drawable.ic_profile_boy)
                    .into(mProfileImage);
        }

    }


    private void initView() {
        mUsername = findViewById(R.id.username);
        mSettingsTopBar = findViewById(R.id.user_top_bar);
        mRootContentView = getWindow().getDecorView().findViewById(android.R.id.content);
        mProfileImage = findViewById(R.id.profile_image_settings);
        mTipLogin = findViewById(R.id.tip_login);

    }
}
