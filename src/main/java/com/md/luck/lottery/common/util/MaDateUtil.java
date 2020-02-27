package com.md.luck.lottery.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 時間工具類
 */
public class MaDateUtil {
    /**
     * 格式化获取当前时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
