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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sy.mingding.Adapter.MainContentAdapter;
import com.sy.mingding.Base.BaseActivity;
import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.User;
import com.sy.mingding.Model.UserModel;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Utils.IMMLeaks;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.db.NewFriendManager;
import com.sy.mingding.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity {

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



        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        toast(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    toast(status.getMsg());
                    //Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());


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

                    case R.id.navigation_chat:
                        mContentPager.setCurrentItem(0);
                        LogUtil.d(getClass().getName(),"click --->chat");
                        return true;
                    case R.id.navigation_todo:
                        mContentPager.setCurrentItem(1);
                        LogUtil.d(getClass().getName(),"click --->todo");
                        return true;
                    case R.id.navigation_statistic:
                        mContentPager.setCurrentItem(2);
                        LogUtil.d(getClass().getName(),"click --->statistic");
                        return true;
                    case R.id.navigation_settings:
                        mContentPager.setCurrentItem(3);
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

    @Override
    protected void initView() {
        mMainNavigation = (BottomNavigationView) findViewById(R.id.main_navigation);
        mContentPager = (ViewPager) findViewById(R.id.content_pager);
        mNavigationMenu = mMainNavigation.getMenu();

        //创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter =new MainContentAdapter(supportFragmentManager);
        mContentPager.setAdapter(mainContentAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            toast("nihaa");
        //    iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
            toast("nihaa");
           // iv_conversation_tips.setVisibility(View.GONE);
        }
        //TODO 好友管理：是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            toast("nihaa");
          //  iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
            toast("nihaa");
           // iv_contact_tips.setVisibility(View.GONE);
        }
    }
}
