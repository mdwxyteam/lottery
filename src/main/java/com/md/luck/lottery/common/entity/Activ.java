package com.md.luck.lottery.common.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
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
    private Integer conditionType;
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
    private Integer state;
    /**
     * 活动发布时间 时间戳
     */
    private Date releaseTime = new Date();
    /**
     * 广告富文本框
     */
    private String adv;
    /**
     * 广告富文本框markdown
     */
    private String markdownAdv;
    /**
     * 活动当前参与人数
     */
    private Integer countNum;
    /**
     *  删除状态
     *  赞助商状态 {0：禁用；1：启用}
     */
    private Integer delState;
    /**
     * 参与条件
     */
    private String addCondition;
    /**
     * 0:不上轮播；1上轮播
     */
    private Integer carousel;
    /**
     * 0:抽奖；1:表示抢
     */
    private Integer activType;

    /**
     * 抢类型活动中需要的 人气
     */
    private Integer popularity;

    public boolean isEmpty() {
        if (MaObjUtil.hasEmpty(sponsorid, sponsorName, location, address, condition, sponsorClaim, state, adv, conditionType, markdownAdv)) {
            return true;
        }
        return false;
    }
    public boolean hasNotEmpty() {
        if (!MaObjUtil.isAllEmpty(carousel, activType, sponsorid, sponsorName, location, address, condition, sponsorClaim, state, adv, conditionType, markdownAdv)) {
            return true;
        }
        return false;
    }
}
