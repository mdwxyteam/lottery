package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Activ;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivMapper {

    /**
     * 新增activ
     * @param activ activ
     * @return int
     */
    @Insert("INSERT INTO lottery_activ (sponsorid, sponsor_name, location, address, condition_type, sponsor_claim, state, adv, condition, release_time)" +
            " VALUES (#{activ.sponsorid}, #{activ.sponsorName}, #{activ.location}, #{activ.address}, #{activ.conditionType}" +
            ", #{activ.sponsorClaim}, #{activ.state}, #{activ.adv}, #{activ.condition}, now())")
    int add(@Param("activ") Activ activ);

    @Select("<script> SELECT " +
            "</script>")
    List<Activ> conditionPage(@Param("conditionType") int conditionType, @Param("sponsorName") String sponsorName);
}
