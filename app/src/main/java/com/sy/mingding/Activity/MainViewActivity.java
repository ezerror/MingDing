package com.sy.mingding.Activity;


import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sy.mingding.Adapter.MainContentAdapter;
import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Utils.LogUtil;

import cn.bmob.v3.BmobUser;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainViewActivity extends FragmentActivity {

    private ViewPager mContentPager;
    private BottomNavigationView mMainNavigation;
    private Menu mNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        LogUtil.d(this,"---> hello world");
        initView();
        initEvent();


        //动态申请权限  api23以上
        String[] Permission= {WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this,Permission,1);

    }



    private void initEvent() {
        //将底部导航栏和viewpager绑定
//        PagerBinder.bind(mMainNavigation,mContentPager);

        mMainNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_todo:
                        mContentPager.setCurrentItem(0);
                        LogUtil.d(getClass().getName(),"click --->todo");
                        return true;
                    case R.id.navigation_statistic:
                        mContentPager.setCurrentItem(1);
                        LogUtil.d(getClass().getName(),"click --->statistic");
                        return true;
                    case R.id.navigation_settings:
                        mContentPager.setCurrentItem(2);
                        LogUtil.d(getClass().getName(),"click --->settings");
                        return true;
                }
                return false;
            }
        });

        mNavigationMenu = mMainNavigation.getMenu();
        mContentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                MenuItem NavigationMenuItem;
                NavigationMenuItem= mNavigationMenu.getItem(i);
                NavigationMenuItem.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void initView() {
        mMainNavigation = (BottomNavigationView) findViewById(R.id.main_navigation);
        mContentPager = (ViewPager) findViewById(R.id.content_pager);
        mNavigationMenu = mMainNavigation.getMenu();

        //创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter =new MainContentAdapter(supportFragmentManager);
        mContentPager.setAdapter(mainContentAdapter);


    }
}
