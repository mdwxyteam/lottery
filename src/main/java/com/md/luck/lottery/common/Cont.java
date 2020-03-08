package com.md.luck.lottery.common;

public final class Cont {
    public static final int ZERO = 0;
    public static final Long ZEROL = 0L;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FIVE = 5;
    public static final int NEGATIVE_ONE = -1;

    public static final Long WEIXIN_USER_ROLE = 1L;

    public static final int MAX_PAGE_SIZE = 36;

    public static final Integer[] RANDOM_LIMIT = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * 微信模板消息id
     */
    public static final String TEMPLATE_ID = "E5UxHIU3fAwJVVhgDuHHZuENeFE2llw3QVYYmUGWbNg";

    /**
     * 结束标志
     */
    public static final String END = "END_END";


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

    /**
     * redis 存储参与活动数据的key前缀
     */
    public static final String ACTIV_RESDIS_KEY_PRE = "JOIN_KEY_";
    /**
     * redis 存储助力活动数据的key前缀
     */
    public static final String ACTIV_RESDIS_GRAB_PRE = "GRAB_KEY_";
    /**
     * redis 存储助力人员数据的key前缀
     */
    public static final String ACTIV_RESDIS_J_PRE = "J_KEY_";
    /**
     * REDIS 存储数据openid field key
     */
    public static final String OPENID = "OPENID_";

    /**
     * REDIS 排名存储数据
     */
    public static final String RANK_PRE = "RANK_";
    /**
     * REDIS 活动助力中奖记录存储数据
     */
    public static final String RANL_LUCKY_PRE = "RL_";
    /**
     * REDIS 活动结束标志存储数据
     */
    public static final String ACTIVITY_END_PRE = "A_END_";
    /**
     * 用户参与的活动保存记录
     */
    public static final String ADD_ACTIV_TAG_PRE = "A_ACTIV_TAG_";

}
