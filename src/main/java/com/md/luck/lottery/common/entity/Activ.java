package com.md.luck.lottery.common.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.Date;

/**
 * 活动表
 */
@Data
public class Activ {
    private Long id = 0l;
    /**
     * 赞助商id
     */
    private Long sponsorid;
    /**
     * 赞助商名称
     */
    private String sponsorName;
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
     * 开奖条件（2019.12.09 || 3000）
     */
    private String condition;
    /**
     * 开讲条件描述开奖条件例子（2019.12.09日开奖）
     */
    private String conditionalDescription;
    /**
     * 赞助商要求
     */
    private String sponsorClaim;
    /**
     * 1:未结束；0结束；-1测试
     */
    private int state = 1;
    /**
     * 活动发布时间 时间戳
     */
    private Date releaseTime = new Date();
    /**
     * 广告富文本框
     */
    private String adv;
    /**
     * 活动当前参与人数
     */
    private int countNum;
    /**
     *  删除状态
     *  赞助商状态 {0：禁用；1：启用}
     */
    private int delState = 1;
    /**
     * 参与条件
     */
    private String addCondition;

    public boolean isEmpty() {
        if (ObjectUtil.hasEmpty(sponsorid, sponsorName, location, address, condition, sponsorClaim, state, adv, conditionType)) {
            return true;
        }
        return false;
    }
}
