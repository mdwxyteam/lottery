package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface PayCodeService {
    /**
     * 创建支付码
     * Integer price 价格
     * @return ResponMsg
     */
    ResponMsg creatPayCode(Integer price);
}
