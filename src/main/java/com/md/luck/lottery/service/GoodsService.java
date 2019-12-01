package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Goods;

public interface GoodsService {
    /**
     * 新增商品
     * @param goods goods
     * @return ResponMsg
     */
    ResponMsg add(Goods goods);

    /**
     * 分页查询商品
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param goodsName 商品名称
     * @param state 商品状态
     * @return ResponMsg
     */
    ResponMsg page(int pageNum, int pageSize, String goodsName, int state);

    /**
     * 编辑商品
     * @param goods 商品对象
     * @return ResponMsg
     */
    ResponMsg edit(Goods goods);
}
