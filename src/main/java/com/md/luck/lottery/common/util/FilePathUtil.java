package com.md.luck.lottery.common.util;

public class FilePathUtil {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    //public static final String FILE_SEPARATOR = File.separator;

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }

    /**
     * 双正斜杠 // 替换未单正斜杠 /
     * @param path path
     * @return String
     */
    public static String getLinePath(String path) {
        return path.replace("//", "/");
    }
}
