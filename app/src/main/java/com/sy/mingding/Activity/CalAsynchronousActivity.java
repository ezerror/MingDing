package com.sy.mingding.Activity;

import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.sy.mingding.Bean.Event;
import com.sy.mingding.Bean.Timing;
import com.sy.mingding.R;
import com.sy.mingding.Utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * @Author: ez
 * @Time: 2019/2/15 3:15
 * @Description:  异步调用日历视图
 */
public class CalAsynchronousActivity extends CalendarActivity  {

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    boolean calledNetwork = false;
    private Date mDateEndTime;
    private Date mDateStartTime;

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Download events from network if it hasn't been done already. To understand how events are
        // downloaded using retrofit, visit http://square.github.io/retrofit
        if (!calledNetwork) {
            BmobQuery<Timing> TimingBmobQuery = new BmobQuery<>();
            TimingBmobQuery.include("todo");
            TimingBmobQuery.findObjects(new FindListener<Timing>() {
                public void done(List<Timing> list, BmobException e) {
                    if (e == null) {
                        LogUtil.e("BMOBUTIL", "成功");
                        for (Timing timing : list) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar calStartTime = Calendar.getInstance();
                            try {
                                mDateStartTime = sdf.parse(timing.getStartTime().getDate());
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            calStartTime.setTime(mDateStartTime);

                            Calendar calEndTime = Calendar.getInstance();
                            try {
                                mDateEndTime = sdf.parse(timing.getEndTime().getDate());
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            calEndTime.setTime(mDateEndTime);


                            long time = (calEndTime.getTimeInMillis()-calStartTime.getTimeInMillis())/60000;

                            Log.d("chazhi", "done: "+time);
                            WeekViewEvent event = null;
                            if(timing.getTodo()!=null){
                                event = new WeekViewEvent(1, timing.getTodo().getTodoName()+getEventTitle(calStartTime,time), calStartTime, calEndTime);
                            }
                            else {
                                event = new WeekViewEvent(1, timing.getTimingName()+getEventTitle(calStartTime,time), calStartTime, calEndTime);
                            }

                            event.setColor(getResources().getColor(R.color.event_color_01));
                            events.add(event);
                        }
                    } else {
                        LogUtil.e("BMOBUTIL", "失败");
                    }
                    getWeekView().notifyDatasetChanged();
                }
            });
            calledNetwork = true;
        }

        // Return only the events that matches newYear and newMonth.
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }

    /**
     * Checks if an event falls into a specific year and month.
     * @param event The event to check for.
     * @param year The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }



}
