package com.md.luck.lottery.service;

import com.md.luck.lottery.common.entity.AtivPrize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivPrizeMapper {

    /**
     * 新增ativPrize
     * @param ativPrize
     * @return
     */
    @Insert("INSERT INTO lottery_ativ_prize (ativ_id, prize_id, prize_count) VALUES (#{ativPrize.ativId}, #{ativPrize.prizeId}, #{ativPrize.prizeCount})")
    int add(@Param("ativPrize") AtivPrize ativPrize);
}
