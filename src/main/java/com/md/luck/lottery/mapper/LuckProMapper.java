package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.LuckPeo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LuckProMapper {
    @Insert("INSERT INTO lottery_luck_peo (id, record_id, openid, nick_name, icon, rank, prize_name, activ_id) VALUES" +
            " (#{luckPeo.id}, #{luckPeo.recordId}, #{luckPeo.openid}, #{luckPeo.nickName}, #{luckPeo.icon}, #{luckPeo.rank}, #{luckPeo.prizeName}, #{luckPeo.activId})")
    void insert(@Param("luckPeo") LuckPeo luckPeo);

    @Insert("<script> INSERT INTO lottery_luck_peo (id, record_id, openid, nick_name, icon, rank, prize_name, activ_id) VALUES" +
            "<foreach collection='luckPeos' item='luckPeo' index='index' separator=','>" +
            " (#{luckPeo.id}, #{luckPeo.recordId}, #{luckPeo.openid}, #{luckPeo.nickName}, #{luckPeo.icon}, #{luckPeo.rank}, #{luckPeo.prizeName}, #{luckPeo.activId})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("luckPeos") List<LuckPeo> luckPeos);

    @Select("SELECT * FROM lottery_luck_peo WHERE activ_id = #{activId}")
    List<LuckPeo> queryByActivId(@Param("activId") Long activId);
}
