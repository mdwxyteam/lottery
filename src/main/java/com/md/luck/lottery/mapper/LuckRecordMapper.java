package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.LuckyRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LuckRecordMapper {

    @Insert("INSERT INTO lottery_lucky_record (activ_id, openid, nick_name, icon, luck) VALUES (#{luckyRecord.activId}," +
            "#{luckyRecord.openid}, #{luckyRecord.nickName}, #{luckyRecord.icon}, #{luckyRecord.luck})")
    void insert(@Param("luckyRecord") LuckyRecord luckyRecord);

    @Select("SELECT * FROM lottery_lucky_record WHERE openid = #{openid} AND activ_id =#{activId}")
    LuckyRecord queryByActivIdAndOpenid(@Param("openid") String openid, @Param("activId") Long activId);

    @Select("SELECT icon FROM lottery_lucky_record WHERE activ_id = #{activId}")
    List<LuckyRecord> queryIconByActivId(@Param("activId") Long activId);
}
