package com.sy.mingding.Utils.BeanUtils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.sy.mingding.Activity.LoginActivity;
import com.sy.mingding.Activity.MainViewActivity;
import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.User;
import com.sy.mingding.R;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.widget.Dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserUtil {

    //User注册
    public static void User_signUp(final View view, final String username, final String password) {
        final User user = new User();
        //todo:
        user.setUsername(username);
        user.setNickname(username);
        user.setPassword(password);
        user.setEmail(username);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                    Dialog.showWaiting(view.getContext(),"正在注册中···",3000);
                    user_login(view,view.getContext(),username,password);

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
                    ActivityManager.exit();
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
                    Snackbar.make(view, "更新成功" , Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "更新失败" , Snackbar.LENGTH_LONG).show();
                    //Log.e("error", e.getMessage());
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
                    Snackbar.make(view, "头像更换成功：", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "头像更换失败，请稍后再试" , Snackbar.LENGTH_LONG).show();
                    //Snackbar.make(view, "头像更换失败，请稍后再试" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
