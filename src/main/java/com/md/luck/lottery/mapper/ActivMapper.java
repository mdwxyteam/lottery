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
    @Insert("INSERT INTO lottery_activ (del_state, sponsorid, sponsor_name, location, address, condition_type, sponsor_claim, state, adv, `condition`," +
            " release_time, conditional_description, add_condition, markdown_adv, carousel, activ_type)" +
            " VALUES (#{activ.delState}, #{activ.sponsorid}, #{activ.sponsorName}, #{activ.location}, #{activ.address}, #{activ.conditionType}" +
            ", #{activ.sponsorClaim}, #{activ.state}, #{activ.adv}, #{activ.condition}, now(), #{activ.conditionalDescription}," +
            " #{activ.addCondition}, #{activ.markdownAdv}, #{activ.carousel}, #{activ.activType})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void add(@Param("activ") Activ activ);

    @Select("<script> SELECT * FROM lottery_activ WHERE " +
            "condition_type = #{conditionType}" +
            "<if test='sponsorName !=null'> AND LOCATE(#{sponsorName}, `sponsor_name`)>0 </if>" +
            "</script>")
    List<Activ> conditionPage(@Param("conditionType") int conditionType, @Param("sponsorName") String sponsorName);

    @Update("UPDATE lottery_activ SET del_state = #{delState} WHERE id = #{id}")
    int updateDelState(@Param("id") long id,@Param("delState") int delState);

    @Select("SELECT * FROM lottery_activ WHERE id = #{id}")
    Activ activById(@Param("id") long id);

    @Update("<script> UPDATE lottery_activ <set>" +
            "<if test='activ.sponsorid != null'> sponsorid = #{activ.sponsorid}, </if>" +
            "<if test='activ.sponsorName != null'> sponsor_name = #{activ.sponsorName}, </if>" +
            "<if test='activ.location != null'> location = #{activ.location}, </if>" +
            "<if test='activ.address != null'> address = #{activ.address}, </if>" +
            "<if test='activ.conditionType != null'> condition_type = #{activ.conditionType}, </if>" +
            "<if test='activ.sponsorClaim != null'> sponsor_claim = #{activ.sponsorClaim}, </if>" +
            "<if test='activ.adv != null'> adv = #{activ.adv}, </if>" +
            "<if test='activ.condition != null'> `condition` = #{activ.condition}, </if>" +
            "<if test='activ.conditionalDescription != null'> conditional_description = #{activ.conditionalDescription}, </if>" +
            "<if test='activ.addCondition != null'> add_condition = #{activ.addCondition}, </if>" +
            "<if test='activ.markdownAdv != null'> markdown_adv = #{activ.markdownAdv}, </if>" +
            "<if test='activ.carousel != null'> carousel = #{activ.carousel}, </if>" +
            "<if test='activ.activType != null'> activ_type = #{activ.activType} </if>" +
            "</set>" +
            "WHERE id = #{activ.id}" +
            "</script>")
    int updateActiv(@Param("activ") Activ activ);
}
