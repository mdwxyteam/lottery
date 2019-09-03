package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Prize;

public interface PrizeService {
    /**
     * 新增奖品
     * @param prizeDescription 奖品描述
     * @param iconUrl 奖品图片地址
     * @param prizeCount 奖品数量
     * @return ResponMsg<Prize>
     */
    ResponMsg<Prize> add(String prizeDescription, String iconUrl, int prizeCount);
}
