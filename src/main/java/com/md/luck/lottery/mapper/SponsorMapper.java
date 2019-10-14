package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Sponsor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SponsorMapper {

    @Insert("INSERT INTO lottery_sponsor (sponsor_name, type, detalis, location, address, type_id, creat_time) VALUES (#{sponsor.sponsorName},#{sponsor.type} ,#{sponsor.detalis} ,#{sponsor.location} , #{sponsor.address} ,#{sponsor.typeId}, now())")
    int add(@Param("sponsor") Sponsor sponsor);

    @Select("SELECT * FROM lottery_sponsor")
    List<Sponsor> all();

    @Select("SELECT * FROM lottery_sponsor WHERE id = #{sponsorId}")
    Sponsor bySponsorId(@Param("sponsorId") Long sponsorId);

    @Select("SELECT * FROM lottery_sponsor WHERE type_id = #{typeId}")
    List<Sponsor> byType(@Param("typeId") Long typeId);

    @Update("<script> UPDATE lottery_sponsor <set> " +
            "<if test='sponsor.sponsorName != null'> sponsor_name = #{sponsor.sponsorName}," +
            "<if test='sponsor.type != null'> type = #{sponsor.type}," +
            "<if test='sponsor.detalis != null'> detalis = #{sponsor.detalis}," +
            "<if test='sponsor.typeId != null'> type_id = #{sponsor.typeId}," +
            "<if test='sponsor.location != null'> location = #{sponsor.location}," +
            "<if test='sponsor.address != null'> address = #{sponsor.address}" +
            "</set>" +
            "WHERE id = #{sponsor.id}" +
            "</script>")
    int update(Sponsor sponsor);
}
