package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface SponsorPrizeService {
    /**
     * 新增赞助商与奖品关联
     * @param sponsorId 赞助商id
     * @param prizeid 奖品id
     * @param prizeCount 奖品数量
     * @return ResponMsg
     */
    ResponMsg add(Long sponsorId, Long prizeid, int prizeCount);
}
