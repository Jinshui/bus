package com.bus.services.util;

import java.util.Calendar;

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
}
