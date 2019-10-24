package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface ActivPrizeService {
    /**
     * 新增活动与奖品关联
     * @param ativId 活动id
     * @param prizeId 奖品id
     * @param prizeCount 奖品数量
     * @return ResponMsg
     */
    ResponMsg add(Long ativId, Long prizeId, int prizeCount, String icouUrl);

    /**
     * 通过活动id查询活动与奖品关联数据
     * @param ativId 活动id
     * @return ResponMsg
     */
    ResponMsg queryActivPrize(long ativId);
}
