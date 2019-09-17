package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Sponsor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SponsorMapper {

    @Insert("INSERT INTO lottery_sponsor (sponsor, type, detalis, position, type_id) VALUES (#{sponsor.sponsor},#{sponsor.type} ,#{sponsor.detalis} ,#{sponsor.position} ,#{sponsor.typeId})")
    int add(@Param("sponsor") Sponsor sponsor);
}
