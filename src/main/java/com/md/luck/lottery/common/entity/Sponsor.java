package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 赞助商
 */
@Data
public class Sponsor {
    private long id;
    /**
     * 赞助商名称
     */
    private String sponsor;
    /**
     * 赞助商类型
     */
    private long type;
    /**
     * 详细信息
     */
    private String position;
    /**
     * 地点
     */
    private String detalis;
    /**
     * 类型id
     */
    private int typeId;
}
