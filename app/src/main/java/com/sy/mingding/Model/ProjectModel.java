package com.sy.mingding.Model;

import android.content.Context;
import android.os.Message;
import android.view.View;

import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.Constants.Constants;
import com.sy.mingding.Utils.DialogUtil;
import com.sy.mingding.Utils.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ProjectModel {

//    public static void queryProjectOwner(final View view) {
//
//        if (BmobUser.isLogin()) {
//            BmobQuery<Project> query = new BmobQuery<>();
//            query.addWhereEqualTo("owner", BmobUser.getCurrentUser(User.class));
//            query.order("-updatedAt");
//            //包含作者信息
//            query.include("owner");
//             query.findObjects(new FindListener<Project>() {
//
//                @Override
//                public void done(List<Project> object, BmobException e) {
//
//                    if (e == null) {
//                        LogUtil.d("BMOBBBBBBB", "查询成功"+object.get(0).getProjectName());
//                    } else {
//                        LogUtil.d("BMOBBBBBBB", e.toString());
//                    }
//                }
//
//            });
//        } else {
//            Snackbar.make(view, "请先登录", Snackbar.LENGTH_LONG).show();
//        }
//    }





    public static Project addProject(final View view, final String projectName){
    final Project project=new Project();
    project.setProjectName(projectName);
    project.setUserId(UserModel.getCurrentUser());
    project.save(new SaveListener<String>() {
        @Override
        public void done(String s, BmobException e) {
            if (e == null) {
                    //需要数据传递，用下面方法；
                    Message msg = new Message();
                    msg.obj = project;//可以是基本类型，可以是对象，可以是List、map等；
                    msg.what=Constants.HANDLER_ADD_PROJECT;
                   if (msg.obj != null) {
                       BaseApplication.mHandler.sendMessage(msg);
                   }
            } else {
                LogUtil.e("BMOB", e.toString());
            }
        }
    });
    return project;
}


    public static void deleteProjectFromSql(final Project pro, final Context context){
        final Project project=new Project();
        project.setObjectId(pro.getObjectId());
        DialogUtil.showWaitingDialog(context,null,"删除中···");


        BmobQuery<Todo> query = new BmobQuery<>();
        LogUtil.d("QUERY---",project.getObjectId());
        query.addWhereEqualTo("project", new BmobPointer(pro));
        query.order("createdAt");
        query.include("todoName");
        query.findObjects(new FindListener<Todo>() {

                @Override
                public void done(List<Todo> object, BmobException e) {

                    if (e == null) {
                        for (Todo p:object) {
                            TodoModel.deleteTodoFromSql(p.getObjectId());
                        }
                    } else {
                        LogUtil.e("QUERY---",e.getMessage()+"--->"+e.getErrorCode());
                    }
                }

            });

        DialogUtil.closeWaitingDialog();

        project.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    if (context != null) {
                        DialogUtil.closeWaitingDialog();
                    }
                    LogUtil.d("ProjectModel---->:deleteProjectFromSql","成功删除Project id:"+project.getObjectId());
                }else{
                    LogUtil.d("ProjectModel---->:deleteProjectFromSql","error-->"+e.getErrorCode()+"   "+e.getMessage());
                }
            }
        });
    }
}
