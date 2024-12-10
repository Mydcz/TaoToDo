package com.km.tao.utils;

import java.util.Locale;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 格式化字符串
     * @param format 格式
     * @param obj 参数
     * @return 格式化后的字符串
     */
    public static String format(String format, Object... obj) {
        return String.format(Locale.ENGLISH, format, obj);
    }
}
