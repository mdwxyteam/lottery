package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 赞助商奖品关联
 */
@Data
public class SponsorPrize {
    private Long id;
    /**
     * 赞助商id
     */
    private Long sponid;
    /**
     * 奖品id
     */
    private Long prizeid;
    /**
     * 奖品数量
     */
    private int prizeCount;
}
