package com.sy.mingding.Utils.BeanUtils;

import com.sy.mingding.Base.BaseApplication;
import com.sy.mingding.Bean.Timing;
import com.sy.mingding.Bean.Todo;
import com.sy.mingding.Utils.Constants;
import com.sy.mingding.Utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @Author: ez
 * @Time: 2019/2/14 20:43
 * @Description: Timing表操作
 */
public class TimingUtil {

    public static void addTiming(final String todoid, final Integer time, Date start, Date end) {
        final BmobDate startTime = new BmobDate(start);
        final BmobDate endTime = new BmobDate(end);
        final Timing timing = new Timing();
        Todo todo = new Todo();
        todo.setObjectId(todoid);
        timing.setTime(time);
        timing.setTodo(todo);
        timing.setStartTime(startTime);
        timing.setEndTime(endTime);
        timing.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtil.e("BMOB", "addTiming" + timing);
                } else {
                    LogUtil.e("BMOB", e.toString());
                }
            }
        });


        TodoUtil.updateTodoSumTime(todoid, time);
    }
}
