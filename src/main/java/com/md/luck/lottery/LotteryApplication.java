package com.md.luck.lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author madong
 */
@SpringBootApplication
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }
//    public static void main(String[] args) {
//        String str = "&#123456;&#128525;";
//        Pattern pattern = Pattern.compile("\\&\\#\\d+;");
//        Matcher matcher = pattern.matcher(str);
////        if (matcher.start() > 0) {
////            System.out.println("-------");
////        }
//        System.out.println(matcher.groupCount());
//        while (matcher.find()) {
//            String s = matcher.group(0);
////            String s1 = matcher.group(1);
//            System.out.println(s);
//            System.out.println("--------------");
////            System.out.println(s1);
//        }
//    }
}
