package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.LuckyRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LuckRecordMapper {

    @Insert("INSERT INTO lottery_lucky_record (activ_id, openid, nick_name, icon, luck, add_time) VALUES (#{luckyRecord.activId}," +
            "#{luckyRecord.openid}, #{luckyRecord.nickName}, #{luckyRecord.icon}, #{luckyRecord.luck}, now())")
    void insert(@Param("luckyRecord") LuckyRecord luckyRecord);

    @Select("SELECT * FROM lottery_lucky_record WHERE openid = #{openid} AND activ_id =#{activId}")
    LuckyRecord queryByActivIdAndOpenid(@Param("openid") String openid, @Param("activId") Long activId);

    @Select("SELECT icon FROM lottery_lucky_record WHERE activ_id = #{activId}")
    List<LuckyRecord> queryIconByActivId(@Param("activId") Long activId);

    @Select("SELECT * FROM lottery_lucky_record WHERE activ_id = #{activId}")
    List<LuckyRecord> queryByActivId(@Param("activId") Long activId);

    @Update("UPDATE lottery_lucky_record SET luck = #{luck} WHERE id = #{id}")
    void updateLuck(@Param("luck") Integer luck, @Param("id") Long id);
}
