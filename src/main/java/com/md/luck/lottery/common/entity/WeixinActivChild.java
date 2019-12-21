package com.md.luck.lottery.common.entity;

import lombok.Data;

@Data
public class WeixinActivChild extends WeixnActiv {
    /**
     * 当前活动参与人数
     */
    private Integer countNum;
    /**
     * 活动开奖条件描述 1万3千人气开奖
     */
    private String conditionalDescription;
    /**
     * 赞助商要求
     */
    private String sponsorClaim;
    /**
     * 赞助商名称
     */
    private String sponsorName;
    /**
     * 广告
     */
    private String adv;
}
