package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 活动表
 */
@Data
public class Activ {
    private Long id;
    /**]
     * 赞助商id
     */
    private Long sponsorid;
    /**
     * 赞助商名称
     */
    private String sponsor;
    /**
     * 地点坐标
     */
    private String location;
    /**
     * 地点
     */
    private String address;
    /**
     * 开奖条件id
     */
    private Long conditionid;
    /**
     * 赞助商要求
     */
    private String sponsorClaim;
    /**
     * 0:未结束；1结束；-1测试
     */
    private int state;
    /**
     * 广告富文本框
     */
    private String adv;
}
