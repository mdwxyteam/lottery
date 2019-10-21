package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.AtivPrize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AtivPrizeMapper {

    @Insert("INSERT INTO lottery_ativ_prize (ativ_id, prize_id, prize_count, ranking) VALUES (#{ativPrize.ativId}, #{ativPrize.prizeId}, #{ativPrize.prizeCount}, #{ativPrize.ranking})")
    int add(@Param("ativPrize") AtivPrize ativPrize);
}
