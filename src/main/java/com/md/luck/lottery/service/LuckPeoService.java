package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface LuckPeoService {
    /**
     * 通过活动id查询中奖纪录
     * @param activId activId
     * @return ResponMsg
     */
    ResponMsg queryLucker(Long activId);
}
