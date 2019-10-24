package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.AtivPrize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AtivPrizeMapper {

    @Insert("INSERT INTO lottery_ativ_prize (ativ_id, prize_id, prize_count, ranking, icon_url) VALUES (#{ativPrize.ativId}, #{ativPrize.prizeId}, #{ativPrize.prizeCount}, #{ativPrize.ranking}, #{ativPrize.icon_url})")
    int add(@Param("ativPrize") AtivPrize ativPrize);

    @Select("SELECT * FROM lottery_ativ_prize WHERE ativ_id = #{id}")
    AtivPrize queryByAtivId(@Param("id") long id);
}
