package com.md.luck.lottery.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * con表达式工具
 */
public class ConUtil {
    /***
     *
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 将字符串时间转换为date对象
     * @param dateTimeStr 字符串时间
     * @param fomat 格式
     * @return Date
     */
    public static Date parse(String dateTimeStr, String fomat) {
        try {
            return new SimpleDateFormat(fomat).parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /***
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     * @param dateTime  : 时间点
     * @return
     */
    public static String getCron(String dateTime, String fomat){
        Date date = parse(dateTime, fomat);
        String dateFormat="ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }
}
