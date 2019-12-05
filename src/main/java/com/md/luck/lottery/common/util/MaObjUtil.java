package com.md.luck.lottery.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * 对象工具
 */
public class MaObjUtil {

    /**
     * 判断所有对象是不是都是null或“”
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isAllEmpty(Object... objs) {
        for (Object obj: objs) {
            boolean empty = MaObjUtil.isEmpty(obj);
            if (!empty) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否有空或“”
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean hasEmpty(Object... objs) {
        for (Object obj: objs) {
            boolean empty = MaObjUtil.isEmpty(obj);
            if (empty) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为null或“”
     * @param obj obj
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return StrUtil.isEmpty((CharSequence) obj);
        }
        return false;
    }
}
