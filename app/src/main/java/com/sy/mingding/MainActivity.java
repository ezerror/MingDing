

package com.sy.mingding;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sy.mingding.Adapter.MainContentAdapter;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.Utils.PagerBinder;


public class MainActivity extends FragmentActivity {

    private ViewPager mContentPager;
    private BottomNavigationView mMainNavigation;
    private Menu mNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.d(this,"---> hello world");
        initView();
        initEvent();
    }

    private void initEvent() {
        //将底部导航栏和viewpager绑定
        PagerBinder.bind(mMainNavigation,mContentPager);

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
