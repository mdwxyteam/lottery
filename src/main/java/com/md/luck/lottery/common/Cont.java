package com.md.luck.lottery.common;

public final class Cont {
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int NEGATIVE_ONE = -1;

    public static final Long WEIXIN_USER_ROLE = 1L;

    public static final int MAX_PAGE_SIZE = 36;

    public static final Integer[] RANDOM_LIMIT = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * 正在售卖状态
     */
    public static final int SELL = 1;
    /**
     * 售卖结束状态
     */
    public static final int SELL_END = 2;
    /**
     * 准备售卖状态
     */
    public static final int SELL_READY = 3;
    /**
     * 商品删除（关闭）状态
     */
    public static final int GOODS_DEL = -1;

    public static final String quartz = "HLOTTERY";
}
