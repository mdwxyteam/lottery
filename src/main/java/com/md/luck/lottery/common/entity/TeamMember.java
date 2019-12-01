package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 团队成员实体类
 */
@Data
public class TeamMember extends BaseEntity{
    /**
     * 团队id
     */
    private long teamId;
    /**
     * 团队成员唯一标识
     */
    private String openid;
    /**
     * 团队成员数量
     */
    private int memberCount;
}
