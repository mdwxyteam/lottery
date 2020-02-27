package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Prize;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrizeMapper {

    /**
     * 新增奖品
     * @param prize 奖品对象
     * @return int
     */
    @Insert("INSERT INTO lottery_prize (id, prize_description, icon_url, prize_count,is_delete) VALUES (#{prize.id}, #{prize.prizeDescription}, #{prize.iconUrl}, #{prize.prizeCount},0)")
    int add(@Param("prize") Prize prize);

    /**
     * 查询所有奖品信息
     * @return List<Prize>
     */
    @Select("SELECT * FROM lottery_prize WHERE is_delete = 0")
    List<Prize> all();

    @Select("<script> SELECT * FROM lottery_prize WHERE is_delete = 0" +
            " <if test='prizeDescription !=null'>AND LOCATE(#{prizeDescription}, `prize_description`)>0 </if>" +
            "</script>")
    List<Prize> queryByPrizeDescription(@Param("prizeDescription") String prizeDescription);

    /**
     * 更新商品
     * @param prize
     * @return
     */
    @Update("UPDATE lottery_prize SET prize_description = #{prize.prizeDescription}," +
            " icon_url = #{prize.iconUrl}," +
            " prize_count = #{prize.prizeCount}" +
            " WHERE id = #{prize.id}")
    int update(@Param("prize") Prize prize);

    /**
     * 删除奖品状态
     * @param prize
     * @return
     */
    @Update("UPDATE lottery_prize SET is_delete = #{prize.isDelete} WHERE id = #{prize.id}")
    int updateDel(@Param("prize") Prize prize);

}
