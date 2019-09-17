package com.md.luck.lottery.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BasePath {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    static {
        try {
            basePath= URLDecoder.decode(basePath,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static String getBasePath() {
        return basePath;
    }
}
