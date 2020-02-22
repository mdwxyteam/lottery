package com.md.luck.lottery.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 获取随机数工具类
 */
public class MaMathUtil {
    private static int ONE = 1;
    private Integer[] nums = null;

    private MaMathUtil(int length) {
        nums = new Integer[length];
    }

    public static MaMathUtil instance(int length) {
        return new MaMathUtil(length);
    }

    /**
     * 在一定范围内计算一个不重复的随机数
     * @return Integer
     */
    public Integer random(int maxSzie) {
        int ran1 = RandomUtil.randomInt(MaMathUtil.ONE, maxSzie);
        for (Integer i: nums) {
            if (i == ran1) {
                ran1 = this.random(maxSzie);
            }
        }
        return ran1;
    }

    /**
     * 生成id
     * @return
     */
    public static Long creatId() {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long id = snowflake.nextId();
        return id;
    }
}
