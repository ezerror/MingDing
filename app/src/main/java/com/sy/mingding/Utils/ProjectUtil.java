package com.sy.mingding.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.sy.mingding.Bean.Project;
import com.sy.mingding.Bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ProjectUtil {

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
}
