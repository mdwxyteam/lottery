package com.md.luck.lottery.common.entity;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

/**
 * 商品实体类
 */
@Data
public class Goods {
    private long id;
    /**
     * 商品价格
     */
    private Integer price;
    /**
     * 商品状态 状态：1：正在售卖中；2：售卖结束；3：售卖准备中；-1：关闭（删除）
     */
    private Integer state;
    /**
     * 购买人数小于等于price
     */
    private Integer payNum;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品图片
     */
    private String goodsImg;
    /**
     * 实际售价
     */
    private Integer actualPrice;
    /**
     * 商品相关讨论群
     */
    private String goodsGroup;
    /**
     * 群二维码
     */
    private String groupIcon;

    public boolean isEmpty() {
        if (ObjectUtil.hasEmpty(price, goodsName, goodsImg, actualPrice)){
            return true;
        }else {
            return false;
        }
    }
}
