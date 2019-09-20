package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Prize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrizeMapper {

    /**
     * 新增奖品
     * @param prize 奖品对象
     * @return int
     */
    @Insert("INSERT INTO lottery_prize (prize_description, icon_url, prize_count) VALUES (#{prize.prizeDescription}, #{prize.iconUrl}, #{prize.prizeCount})")
    int add(@Param("prize") Prize prize);

    /**
     * 查询所有奖品信息
     * @return List<Prize>
     */
    @Select("SELECT * FROM lottery_prize")
    List<Prize> all();

}
