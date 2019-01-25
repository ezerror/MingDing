package com.sy.mingding.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.sy.mingding.Activity.LoginActivity;
import com.sy.mingding.Activity.MainViewActivity;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserUtil {

    //User注册
    public static void User_signUp(final View view,String username,String password) {
        final User user = new User();
        user.setUsername(username);
        user.setNickname(username);
        user.setPassword(password);
        user.setEmail(username);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                } else {
                    switch (e.getErrorCode()){
                        case 304:Snackbar.make(view, "该用户名已被注册" , Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //登录
    //仅在LoginActivity使用
    public static void user_login(final View view, final Context context, String username, String password) {
        BmobUser.loginByAccount(username, password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();
                    Intent intent =new Intent(context,MainViewActivity.class);
                    context.startActivity(intent);

                } else {
                    Snackbar.make(view, "登录失败：" + e.getErrorCode()+e.getMessage(), Snackbar.LENGTH_LONG).show();

                    switch (e.getErrorCode()){
                        case 109:Snackbar.make(view, "登录失败：该用户未注册 " , Snackbar.LENGTH_LONG).show();
                        case 304:Snackbar.make(view, "登录失败：用户名为空" , Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //获取当前User
    public static User get_user(){
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            return user;
        } else {
            return null;
        }
    }
    //
    public static void User_update(final View view,String target,String s) {
        final User user = BmobUser.getCurrentUser(User.class);
        switch (s){
            case "nickname":user.setNickname(target);

        }
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "更新用户信息成功：" + user.getNickname(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "更新用户信息失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    Log.e("error", e.getMessage());
                }
            }
        });
    }
    public static void User_update_icon(final View view,BmobFile target) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setIcon(target);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "更新用户ICON成功：", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "更新用户ICON失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    Log.e("error", e.getMessage());
                }
            }
        });
    }

}
