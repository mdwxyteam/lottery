package com.md.luck.lottery.common.util;

import com.sun.xml.internal.fastinfoset.sax.Properties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BasePath {
//    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static String basePath = System.getProperty("user.dir") + "/src/main/resources";

//    static {
//
//        try {
////            if ()
//            if (Thread.currentThread().getContextClassLoader() == null) {
//                System.out.println("------------null------");
//            }
//            if (Thread.currentThread().getContextClassLoader().getResource("") == null) {
//                System.out.println("------------resoutse=null-------");
//                System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
//            }
////            basePath = URLDecoder.decode(, "utf-8");
////            System.out.println(basePath);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
////            Properties pps = new Properties();
////            InputStream stream = new BasePath().getClass()
////                    .getClassLoader()
////                    .getResourceAsStream("defult.properties");
////            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
////            pps.load(br);
//
//    }
    public BasePath() {}
    public static String getBasePath() {
//        try {
//            basePath = URLDecoder.decode(basePath, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(basePath);
        return basePath;
    }
}
