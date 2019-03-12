package com.sy.mingding.Model;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.sy.mingding.Activity.MainActivity;
import com.sy.mingding.Base.BaseModel;
import com.sy.mingding.Bean.Friend;
import com.sy.mingding.Bean.User;
import com.sy.mingding.Utils.ActivityManager;
import com.sy.mingding.Utils.LogUtil;
import com.sy.mingding.listener.QueryUserListener;
import com.sy.mingding.listener.UpdateCacheListener;
import com.sy.mingding.widget.Dialog;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserModel extends BaseModel {
    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {}

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
                    UserModel.getInstance().login(username, password, new LogInListener() {
                        @Override
                        public void done(Object var1, BmobException var2) {

                        }
                    });

                } else {
                    LogUtil.e("LOGINEXCEPTE",e.getErrorCode()+e.getMessage());
                    switch (e.getErrorCode()){
                        case 202:Snackbar.make(view, "用户名"+username+"已被注册" , Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


//    //登录
//    //仅在LoginActivity使用
//    public static void user_login(final View view, String username, String password) {
//            BmobUser.loginByAccount(username, password, new LogInListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//                //    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();
//                    Intent intent =new Intent(view.getContext(),MainActivity.class);
//                    ActivityManager.exit();
//                    view.getContext().startActivity(intent);
//
//
//                } else {
//                    //Snackbar.make(view, "登录失败：" + e.getErrorCode()+e.getMessage(), Snackbar.LENGTH_LONG).show();
//                    LogUtil.e("LOGINEXCEPTE",e.getErrorCode()+e.getMessage());
//                    switch (e.getErrorCode()){
//                        case 109:Snackbar.make(view, "登录失败：该用户未注册 " , Snackbar.LENGTH_LONG).show();
//                        case 304:Snackbar.make(view, "登录失败：用户名为空" , Snackbar.LENGTH_LONG).show();
//
//                    }
//                }
//            }
//        });
//    }

    public void login(String username, String password, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.done(null,new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.done(null,new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    listener.done(getCurrentUser(), null);
                }else {
                    listener.done(user, new BmobException(e.getErrorCode(), e.getMessage()));
                }
            }
        });
    }

    //获取当前User
    public static User getCurrentUser(){
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            return user;
        } else {
            return null;
        }
    }
    //退出当前用户
    public void logout(){
        BmobUser.logOut();
    }

    /**
     * TODO 用户管理：2.5、查询用户
     *
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username, final int limit, final FindListener<User> listener) {
        BmobQuery<User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            User user = BmobUser.getCurrentUser(User.class);
            query.addWhereNotEqualTo("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereEqualTo("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "查无此人"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });
    }








    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String avatar = info.getAvatar();
        String title = conversation.getConversationTitle();
        String icon = conversation.getConversationIcon();
        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
        if (!username.equals(title) || (avatar != null && !avatar.equals(icon))) {
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(User s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String avatar = s.getAvatar();
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //TODO 用户管理：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().updateUserInfo(info);
                        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {
                        Logger.e(e);
                    }
                    listener.done(null);
                }
            });
        } else {
            listener.done(null);
        }
    }

    /**
     * TODO 用户管理：2.6、查询指定用户信息
     *
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(
                new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {

                            if (list != null && list.size() > 0) {
                                listener.done(list.get(0), null);
                            } else {
                                listener.done(null, new BmobException(000, "查无此人"));
                            }
                        } else {
                            listener.done(null, e);
                        }
                    }
                });
    }




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
    //TODO 好友管理：9.12、添加好友
    public void agreeAddFriend(User friend, SaveListener<String> listener) {
        Friend f = new Friend();
        User user = BmobUser.getCurrentUser(User.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.save(listener);
    }

}
