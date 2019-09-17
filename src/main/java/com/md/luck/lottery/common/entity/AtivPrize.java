package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 活动奖品关联
 */
@Data
public class AtivPrize {
    private Long id;
    /**
     * 赞助商id
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
}
