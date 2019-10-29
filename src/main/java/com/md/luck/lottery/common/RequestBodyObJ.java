package com.md.luck.lottery.common;

import lombok.Data;

/**
 * 请求数据在body
 */
@Data
public class RequestBodyObJ {
    private long typeId;
    private String type;
    private String sponsor;
    private String location;
    private String address;
    private String detalis;
    private String markDown;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 封面图
     */
    private String cover;
}
