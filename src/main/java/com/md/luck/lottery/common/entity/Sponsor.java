package com.md.luck.lottery.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * 赞助商
 */
@Data
public class Sponsor {
    private long id;
    /**
     * 赞助商名称
     */
    private String sponsorName;
    /**
     * 赞助商类型
     */
    private String type;
    /**
     * 详细信息
     */
    private String detalis;
    /**
     * 类型id
     */
    private Long typeId;

    /**
     * 地点坐标
     */
    private String location;
    /**
     * 地点
     */
    private String address;

    /**
     * 创建时间
     */
    private Date creatTime;
}
