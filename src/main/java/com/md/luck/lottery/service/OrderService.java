package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.RequestBodyOrder;
import com.md.luck.lottery.common.util.RequestBodyCheckOrder;

/**
 * 订单相关逻辑
 */
public interface OrderService {
    /**
     * 创建订单
     *
     * @param requestBodyOrder 参数对象
     * @return ResponMsg
     */
    ResponMsg creatOrder(String oepnid, RequestBodyOrder requestBodyOrder);

    /**
     * 创建订单前检测用户是否已经创建过
     *
     * @param requestBodyCheckOrder requestBodyCheckOrder
     * @return ResponMsg
     */
    ResponMsg checkOrder(String oepnid, RequestBodyCheckOrder requestBodyCheckOrder);
}
