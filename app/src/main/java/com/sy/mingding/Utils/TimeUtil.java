package com.sy.mingding.Utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;

/**
 * The type Time util.
 *
 * @Author: ez
 * @Time: 2019 /2/17 1:59
 * @Description: 功能描述
 */
public class TimeUtil {


    /**
     * 判断是否够一个小时了
     *
     * @param time the time
     * @return sample 5.1小时 or 25分钟
     */
    public static String getTimeFromInteger(Integer time){
        String returnTime;
        if (time/3600>0){
            float tmp=(float)time/3600;
            BigDecimal bd = new BigDecimal((float) tmp);
            bd = bd.setScale(1, 4);
            tmp = bd.floatValue();
            returnTime=tmp+"小时";
        }
        else {
            returnTime=time/60+"分钟";
        }
        return returnTime;
    }

    /**
     * Gets time from bmob date.
     *
     * @param date 日期
     * @return 小时 :分钟(HOUR:MINUTE)
     */
    public static String getTimeFromBmobDate(String date)  {
        String returnTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date returndate = null;
        try {
            returndate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar returncal = Calendar.getInstance();
        returncal.setTime(returndate);
        returnTime=returncal.get(Calendar.HOUR)+":"+returncal.get(Calendar.MINUTE);

        return returnTime;
    }
}
