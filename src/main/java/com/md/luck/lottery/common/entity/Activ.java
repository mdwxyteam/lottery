package com.md.luck.lottery.common.entity;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.Date;

/**
 * 活动表
 */
@Data
public class Activ {
    private Long id;
    /**
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
     * 开奖条件类型：1： 时间限制；2：人数限制
     */
    private int conditionType;
    /**
     * 开奖条件例子（2019.12.09日开奖）
     */
    private String condition;
    /**
     * 赞助商要求
     */
    private String sponsorClaim;
    /**
     * 0:未结束；1结束；-1测试
     */
    private int state;
    /**
     * 活动发布时间 时间戳
     */
    private Date releaseTime;
    /**
     * 广告富文本框
     */
    private String adv;

    public boolean isEmpty() {
        if (ObjectUtil.hasEmpty(sponsorid, sponsor, location, address, condition, sponsorClaim, state, adv, conditionType)) {
            return true;
        }
        return false;
    }
}
