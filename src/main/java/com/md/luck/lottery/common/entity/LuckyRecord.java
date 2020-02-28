package com.md.luck.lottery.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * 抽奖记录表
 */
@Data
public class LuckyRecord extends BaseEntity{
    private Long activId;
    private String openid;
    private String nickName;
    private String icon;
    /**
     * 0：没有中奖；1：中奖
     */
    private Integer luck;
    /**
     * 参与时间
     */
    private String addTime;
}
