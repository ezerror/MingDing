package com.sy.mingding.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * @Author: ez
 * @Time: 2019/2/14 20:40
 * @Description: Timeing表
 */
public class Timing extends BmobObject {
    private Integer time;
    private Todo todo;
    private BmobDate startTime;
    private BmobDate endTime;
    private String timingName;
    private User user;

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

    public BmobDate getStartTime() {
        return startTime;
    }

    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public String getTimingName() {
        return timingName;
    }

    public void setTimingName(String timingName) {
        this.timingName = timingName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
