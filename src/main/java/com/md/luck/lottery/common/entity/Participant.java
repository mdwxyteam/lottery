package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 活动参与者
 */
@Data
public class Participant {
    /**
     * id 主键自增长
     */
    private Long id;
    /**
     * 参与者微信昵称
     */
    private String nickName;
    /**
     * 微信用户openid
     */
    private String openid;
    /**
     * 活动id
     */
    private String activId;
    /**
     * 是否中奖（0：没有；1中奖）
     */
    private String drawPrize;
    /**
     * 是否开奖（0：没有；1：已开奖）
     */
    private String openLottery;
}
