package com.md.luck.lottery.common.entity;

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
}
