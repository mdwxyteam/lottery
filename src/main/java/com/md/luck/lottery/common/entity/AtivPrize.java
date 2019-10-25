package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 活动奖品关联
 */
@Data
public class AtivPrize {
    private Long id;
    /**
     * 活动id
     */
    private Long ativId;
    /**
     * 奖品id
     */
    private Long prizeId;
    /**
     * 奖品数量
     */
    private int prizeCount;
    /**
     * 奖品获取条件（一等奖）
     */
    private String ranking;
    /**
     * 奖品url
     */
    private String iconUrl;
    /**
     * 奖品描述
     */
    private String prizeDescription;
}
