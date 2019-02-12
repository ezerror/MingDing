package com.sy.mingding.Bean;

import cn.bmob.v3.BmobObject;

public class Project extends BmobObject  {
    private String projectName;
    private User userId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }


}
