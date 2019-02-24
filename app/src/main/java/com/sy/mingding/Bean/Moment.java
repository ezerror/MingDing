package com.sy.mingding.Bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @Author: ez
 * @Time: 2019/2/21 22:56
 * @Description: memont表
 */
public class Moment extends BmobObject implements Serializable {


    private User user;
    //用户描述
    private String text;
    //用户描述文字
    private List<BmobFile> photoList;
    //用户上传图片集合

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<BmobFile> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<BmobFile> photoList) {
        this.photoList = photoList;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}