package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 订单实体类
 */
@Data
public class LotteryOrder extends BaseEntity{
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 订单价格
     */
    private int orderPrice;
    /**
     * 订单生成时间
     */
    private String creatTime;
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 拼GO码
     */
    private String payCode;
    /**
     * 拼go码id
     */
    private Long payCodeId;
}
