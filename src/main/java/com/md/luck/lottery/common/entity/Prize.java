package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 奖品
 */
@Data
public class Prize {
    private long id;
    /**
     * 奖品描述
     */
    private String prizeDescription;
    /**
     * 奖品url
     */
    private String iconUrl;
    /**
     * 奖品数量
     */
    private int prizeCount;
    /**
     *  删除状态
     *  0:未删除 1:已删除
     */
    private int isDelete = 0;
}
