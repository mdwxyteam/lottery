package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.AtivPrize;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AtivPrizeMapper {

    @Insert("INSERT INTO lottery_ativ_prize (id, ativ_id, prize_id, prize_count, ranking, icon_url, prize_description) VALUES (#{ativPrize.id}, #{ativPrize.ativId}, #{ativPrize.prizeId}, #{ativPrize.prizeCount}, #{ativPrize.ranking}, #{ativPrize.iconUrl}, #{ativPrize.prizeDescription})")
    int add(@Param("ativPrize") AtivPrize ativPrize);

    @Select("SELECT * FROM lottery_ativ_prize WHERE ativ_id = #{id}")
    List<AtivPrize> queryByAtivId(@Param("id") long id);

    @Delete("DELETE FROM lottery_ativ_prize WHERE id = #{id}")
    void delAtivPrize(@Param("id") long id);

    @Select("SELECT lap.* FROM lottery_activ la JOIN lottery_ativ_prize lap ON la.id = lap.ativ_id WHERE la.carousel = #{carousel} GROUP BY lap.ativ_id")
    List<AtivPrize> queryByCarousel(@Param("carousel") Integer carousel);
}
