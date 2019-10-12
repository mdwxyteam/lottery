package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Sponsor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SponsorMapper {

    @Insert("INSERT INTO lottery_sponsor (sponsor, type, detalis, location, address, type_id, creat_time) VALUES (#{sponsor.sponsorName},#{sponsor.type} ,#{sponsor.detalis} ,#{sponsor.location} , #{sponsor.address} ,#{sponsor.typeId}), now()")
    int add(@Param("sponsor") Sponsor sponsor);

    @Select("SELECT * FROM lottery_sponsor")
    List<Sponsor> all();

    @Select("SELECT * FROM lottery_sponsor WHERE type_id = #{typeId}")
    List<Sponsor> byType(@Param("typeId") Long typeId);
}
