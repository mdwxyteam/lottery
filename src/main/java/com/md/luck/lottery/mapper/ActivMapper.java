package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Activ;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivMapper {

    /**
     * 新增activ
     * @param activ activ
     * @return int
     */
    @Insert("INSERT INTO lottery_activ (sponsorid, sponsor, location, address, condition_type, sponsor_claim, state, adv, condition, release_time)" +
            " VALUES (#{activ.sponsorid}, #{activ.sponsor}, #{activ.location}, #{activ.address}, #{activ.conditionType}" +
            ", #{activ.sponsorClaim}, #{activ.state}, #{activ.adv}, #{activ.condition}, now())")
    int add(@Param("activ") Activ activ);
}
