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
    @Insert("INSERT INTO lottery_activ (sponsorid, sponsor, location, address, conditionid, sponsor_claim, state, adv)" +
            " VALUES (#{activ.sponsorid}, #{activ.sponsor}, #{activ.location}, #{activ.address}, #{activ.conditionid}" +
            ", #{activ.sponsorClaim}, #{activ.state}, #{activ.adv})")
    int add(@Param("activ") Activ activ);
}
