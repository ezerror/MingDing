package com.sy.mingding.Utils.BeanUtils;

import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.Timing;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.Utils.Constants;
import com.sy.mingding.Utils.LogUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @Author: ez
 * @Time: 2019/2/14 20:43
 * @Description: Timing表操作
 */
public class TimingUtil {

    public static void addTiming(String todoid,Integer time){
        final Timing timing=new Timing();
        timing.setTime(time);
        Todo todo=new Todo();
        todo.setObjectId(todoid);
        timing.setTodo(todo);
        timing.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtil.e("BMOB","addTiming"+timing);
                } else {
                    LogUtil.e("BMOB", e.toString());
                }
            }
        });
        TodoUtil.updateTodoSumTime(todoid,time);
    }
}
