package com.md.luck.lottery.common.entity;

import lombok.Data;

@Data
public class RequestBodyOrder {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 支付码
     */
    private String payCode;
}
