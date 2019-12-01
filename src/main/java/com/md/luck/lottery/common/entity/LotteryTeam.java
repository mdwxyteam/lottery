package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 团队实体类
 */
@Data
public class LotteryTeam extends BaseEntity{
    /**
     * 默认以创团创始人的openid为团队唯一值
     */
    private String openid;
    /**
     * 默认以创团创始人的昵称为团队名称
     */
    private String nickName;
    /**
     * 默认以创团创始人的头像为团队logo
     */
    private String icon;
    /**
     * 活动id
     */
    private long acid;
}
