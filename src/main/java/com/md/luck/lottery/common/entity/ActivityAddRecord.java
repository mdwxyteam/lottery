package com.md.luck.lottery.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * 活动参与用户
 */
@Data
public class ActivityAddRecord extends BaseEntity{
    /**
     * 活动id
     */
    private Long activId;
    /**
     * 微信用户唯一标识
     */
    private String openid;
    /**
     * 微信用户昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String icon;
    /**
     * 助力 数量
     */
    private Integer culp;
    /**
     * 排名
     */
    private Long rank;
    /**
     * 队友数量
     */
    private Integer teamMateCount;
    /**
     * 添加时间
     */
    private Date addTime;
}
