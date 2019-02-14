package com.sy.mingding.Bean;

import cn.bmob.v3.BmobObject;

/**
 * @Author: ez
 * @Time: 2019/2/14 20:40
 * @Description: Timeing表
 */
public class Timing extends BmobObject {
    private Integer time;
    private Todo todo;


    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
