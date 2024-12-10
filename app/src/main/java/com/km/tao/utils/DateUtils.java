package com.km.tao.utils;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

/**
 * 时间转换工具
 */
public class DateUtils {

    public static String formatTime(long currentTimeMillis, String pattern) {
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

}
