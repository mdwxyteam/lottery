package com.md.luck.lottery.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 助力分数工具
 */
public class NumberUtil {
    //判断是否为（正/负）整数或小数
    public static  boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[-]*[0-9]+(.[0-9]+)?$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    //分数加时间戳转成double：整数部分是分数，小数部分是时间戳
    public static Double strTime2Double(String score,String flag) throws ParseException {
        if(StringUtils.isBlank(score) || !isNumeric(score)){
            return 0.0;
        }

        if ("1".equals(flag)) {
            long nowSecond = System.currentTimeMillis();
            String str = "2040-01-01 08:00:00.001";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date date = sdf.parse(str);
            long diff = date.getTime() - nowSecond;
            String scoreStr = score +"."+ diff;
            Double scoreDouble = Double.parseDouble(scoreStr);
            return scoreDouble;
        }
        double sd = Double.parseDouble(score);
        return sd;
    }

    //取整数部分
    public static Long round(Double score){
        String scoreStr = "";
        if(isENum(score+"")){
            BigDecimal bd = new BigDecimal(score+"");
            scoreStr = bd.toPlainString();
        }else{
            scoreStr = score+"";
        }
        String intStr = scoreStr.split("\\.")[0];
        long scoreL = Long.parseLong(intStr);
        return scoreL;
    }

    //判断是否为科学计算法
    static String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";//科学计数法正则表达式
    static Pattern pattern = Pattern.compile(regx);
    public static boolean isENum(String input){//判断输入字符串是否为科学计数法
        return pattern.matcher(input).matches();
    }
}
