package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 支付码
 */
@Data
public class PayCode extends BaseEntity{
    /**
     * 拼GO码 加密后的数据
     */
    private String payCode;
    /**
     * 真是价格加密数据
     */
    private String actualPrice;
    /**
     * 0:有效；-1：无效（失效）
     */
    private int status;
}
