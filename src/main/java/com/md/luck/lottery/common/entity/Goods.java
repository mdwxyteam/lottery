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
    private int price;
    /**
     * 商品状态 状态：1：正在售卖中；2：售卖结束；3：售卖准备中；-1：关闭（删除）
     */
    private int state = 3;
    /**
     * 购买人数小于等于price
     */
    private int payNum;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品图片
     */
    private String goodsImg;

    public boolean isEmpty() {
        if (ObjectUtil.hasEmpty(price, state, payNum, goodsName, goodsImg)){
            return true;
        }else {
            return false;
        }
    }
}
