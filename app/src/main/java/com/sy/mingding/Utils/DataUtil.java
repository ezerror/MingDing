package com.sy.mingding.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Author: ez
 * @Time: 2019/2/14 21:35
 * @Description: 功能描述
 */
public class DataUtil {

    /**
     * 当前时间所在一周各天
     *
     * @return 一周每天的零点
     */
    public static Map<String,String> getWeekDateBegin() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date MondayDate = cal.getTime();
        String monday = sdf.format(MondayDate);
        LogUtil.e("DATAUTIL","mondayDate-->"+monday);


        cal.add(Calendar.DATE, 1);
        Date TuesdayDate = cal.getTime();
        String tuesday = sdf.format(TuesdayDate);
        LogUtil.e("DATAUTIL","TuesdayDate-->"+tuesday);


        cal.add(Calendar.DATE, 1);
        Date WednesdayData= cal.getTime();
        String wednesday = sdf.format(WednesdayData);
        LogUtil.e("DATAUTIL","Wednesday-->"+wednesday);

        cal.add(Calendar.DATE, 1);
        Date ThursdayData= cal.getTime();
        String thursday = sdf.format(ThursdayData);
        LogUtil.e("DATAUTIL","Thursday-->"+thursday);

        cal.add(Calendar.DATE, 1);
        Date FridayData= cal.getTime();
        String friday = sdf.format(FridayData);
        LogUtil.e("DATAUTIL","Friday-->"+friday);

        cal.add(Calendar.DATE, 1);
        Date SaturdayData= cal.getTime();
        String saturday = sdf.format(SaturdayData);
        LogUtil.e("DATAUTIL","Saturday-->"+saturday);

        cal.add(Calendar.DATE, 1);
        Date SundayDate = cal.getTime();
        String sunday = sdf.format(SundayDate);
        LogUtil.e("DATAUTIL","Sunday-->"+sunday);

        map.put("1", monday);
        map.put("2", tuesday);
        map.put("3", wednesday);
        map.put("4", thursday);
        map.put("5", friday);
        map.put("6", saturday);
        map.put("7", sunday);
        return map;
    }


    /**
     * 当前时间所在一周各天
     *
     * @return 一周每天的零点
     */
    public static Map<String,String> getWeekDateEnd() {
        Map<String,String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date MondayDate = cal.getTime();
        String monday = sdf.format(MondayDate);
        LogUtil.e("DATAUTIL","mondayDate-->"+monday);


        cal.add(Calendar.DATE, 1);
        Date TuesdayDate = cal.getTime();
        String tuesday = sdf.format(TuesdayDate);
        LogUtil.e("DATAUTIL","TuesdayDate-->"+tuesday);


        cal.add(Calendar.DATE, 1);
        Date WednesdayData= cal.getTime();
        String wednesday = sdf.format(WednesdayData);
        LogUtil.e("DATAUTIL","Wednesday-->"+wednesday);

        cal.add(Calendar.DATE, 1);
        Date ThursdayData= cal.getTime();
        String thursday = sdf.format(ThursdayData);
        LogUtil.e("DATAUTIL","Thursday-->"+thursday);

        cal.add(Calendar.DATE, 1);
        Date FridayData= cal.getTime();
        String friday = sdf.format(FridayData);
        LogUtil.e("DATAUTIL","Friday-->"+friday);

        cal.add(Calendar.DATE, 1);
        Date SaturdayData= cal.getTime();
        String saturday = sdf.format(SaturdayData);
        LogUtil.e("DATAUTIL","Saturday-->"+saturday);

        cal.add(Calendar.DATE, 1);
        Date SundayDate = cal.getTime();
        String sunday = sdf.format(SundayDate);
        LogUtil.e("DATAUTIL","Sunday-->"+sunday);

        map.put("1", monday);
        map.put("2", tuesday);
        map.put("3", wednesday);
        map.put("4", thursday);
        map.put("5", friday);
        map.put("6", saturday);
        map.put("7", sunday);
        return map;
    }
    public static Map<String,Date> getMonthData(int month) {
       Map<String,Date> map = new HashMap();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH,month-1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        Date first = c.getTime();

        //获取当前月最后一天
        c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Date last = c.getTime();


        LogUtil.d("DATAUTIL","getMonthData  "+month+"  "+first+"  " +last);
        map.put("first", first);
        map.put("last", last);
        return map;
    }
    public static Map<String,Date> getTodayDate() {
        Map<String,Date> map = new HashMap();
        Calendar cal=Calendar.getInstance();
        cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date first = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date last = cal.getTime();
        LogUtil.d("DATAUTIL","getTodayData   "+first+"  " +last);
        map.put("first", first);
        map.put("last", last);
        return map;

    }
}
