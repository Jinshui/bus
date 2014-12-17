package com.bus.services.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static int getHHmm(Calendar calendar){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * 100 + minute;
    }

    public static int getYyyyMMdd(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        return year * 10000 + (month + 1) * 100 + date;
    }

    public static String toChineseDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return toChineseDate(calendar);
    }

    public static String toChineseDate(Calendar calendar){
        String[] weekdays = {"日", "一", "二", "三", "四", "五", "六"};
        String format = "MM月dd日 星期" + weekdays[calendar.get(Calendar.DAY_OF_WEEK)] + " HH:mm";
        SimpleDateFormat frm1 = new SimpleDateFormat(format);
        return frm1.format(calendar.getTime());
    }
}
