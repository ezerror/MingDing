package com.sy.mingding.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;

public class Todo extends BmobObject {

    private String todoName;
    private Project project ;
    private User user;





    public String getTodoName(){
        return this.todoName;
    }
    public void setTodoName(String todoName){
        this.todoName = todoName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
