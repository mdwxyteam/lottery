package com.md.luck.lottery.common.entity;

import lombok.Data;

@Data
public class LuckPeo extends BaseEntity{
    /**
     * 活动参与记录id(抽奖， 抢)
     */
    private Long recordId;
    private String openid;
    private String nickName;
    private String icon;
    /**
     * 名次0：表示未中奖
     */
    private Integer rank;
    /**
     * 奖品名称
     */
    private String prizeName;
}
