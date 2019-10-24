package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Activ;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ActivMapper {

    /**
     * 新增activ
     * @param activ activ
     * @return int
     */
    @Insert("INSERT INTO lottery_activ (del_state, sponsorid, sponsor_name, location, address, condition_type, sponsor_claim, state, adv, `condition`, release_time, conditional_description, add_condition)" +
            " VALUES (#{activ.delState}, #{activ.sponsorid}, #{activ.sponsorName}, #{activ.location}, #{activ.address}, #{activ.conditionType}" +
            ", #{activ.sponsorClaim}, #{activ.state}, #{activ.adv}, #{activ.condition}, now(), #{activ.conditionalDescription}, #{activ.addCondition})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int add(@Param("activ") Activ activ);

    @Select("<script> SELECT * FROM lottery_activ WHERE " +
            "condition_type = #{conditionType}" +
            "<if test='sponsorName !=null'> AND LOCATE(#{sponsorName}, `sponsor_name`)>0 </if>" +
            "</script>")
    List<Activ> conditionPage(@Param("conditionType") int conditionType, @Param("sponsorName") String sponsorName);

    @Update("UPDATE lottery_activ SET del_state = #{delState} WHERE id = #{id}")
    int updateDelState(@Param("id") long id,@Param("delState") int delState);

    @Select("SELECT * FROM lottery_activ WHERE id = #{id}")
    Activ activById(@Param("id") long id);
}
