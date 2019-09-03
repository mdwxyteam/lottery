package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Prize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PrizeMapper {

    /**
     * 新增奖品
     * @param prize 奖品对象
     * @return int
     */
    @Insert("INSERT INTO lottery_prize (prize_description, icon_url, prize_count) VALUES (#{prize.prize_description}, #{prize.icon_url}, #{prize.prize_count})")
    int add(@Param("prize") Prize prize);
}
