package com.md.luck.lottery.common.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class MyURLEncoding {
    public static void main(String[] args) throws Exception {
        String str = "http://www.baidu.com?wd=中国&language=简体汉语&size=Song14";
//        String result = GetRealUrl(str);
        String result = URLEncoder.encode(str, "utf-8");;
        System.out.println(result);
    }

    //对url中的参数进行url编码
    public static String GetRealUrl(String str) {
        try {
            int index = str.indexOf("?");
            if (index < 0) return str;
            String query = str.substring(0, index);
            String params = str.substring(index + 1);
            Map map = GetArgs(params);
            String encodeParams = TransMapToString(map);
            return query + "?" + encodeParams;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }

    //将url参数格式转化为map
    public static Map GetArgs(String params) throws Exception {
        Map map = new HashMap();
        String[] pairs = params.split("&");
        for (int i = 0; i < pairs.length; i++) {
            int pos = pairs[i].indexOf("=");
            if (pos == -1) continue;
            String argname = pairs[i].substring(0, pos);
            String value = pairs[i].substring(pos + 1);
            value = URLEncoder.encode(value, "utf-8");
            map.put(argname, value);
        }
        return map;
    }

    //将map转化为指定的String类型
    public static String TransMapToString(Map map) {
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (java.util.Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }

    //将String类型按一定规则转换为Map
    public static Map TransStringToMap(String mapString) {
        Map map = new HashMap();
        java.util.StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "&"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "=");
        return map;
    }
}
