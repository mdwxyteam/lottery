package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.RequestBodyOrder;

/**
 * 订单相关逻辑
 */
public interface OrderService {
    /**
     * 创建订单
     * @param requestBodyOrder 参数对象
     * @return ResponMsg
     */
    ResponMsg creatOrder(RequestBodyOrder requestBodyOrder);
}
