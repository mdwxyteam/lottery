package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 活动助力用户实体类
 */
@Data
public class CastCulp extends BaseEntity {
    /**
     * 活动id
     */
    private Long activid;
    /**
     * 活动参与记录id
     */
    private Long actAddRecordId;
    /**
     * 微信用户唯一标识
     */
    private String openid;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String icon;
    /**
     * 给好友助力数量
     */
    private Integer castCulp;
}
