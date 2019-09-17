package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.SponsorPrize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SponsorPrizeMapper {

    /**
     * 新增sponsorPrize
     * @param sponsorPrize
     * @return int
     */
    @Insert("INSERT INTO lottery_sponsor_prize (sponid, prizeid, prize_count) VALUES (#{sponsorPrize.sponid}, #{sponsorPrize.prizeid}, #{sponsorPrize.prizeCount})")
    int add(@Param("sponsorPrize") SponsorPrize sponsorPrize);
}
