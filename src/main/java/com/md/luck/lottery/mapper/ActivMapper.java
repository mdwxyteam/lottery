package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.WeixinActivChildChild;
import com.md.luck.lottery.common.entity.WeixnActiv;
import com.md.luck.lottery.common.entity.respons.WeixinActivRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface ActivMapper {

    /**
     * 新增activ
     * @param activ activ
     * @return int
     */
    @Insert("INSERT INTO lottery_activ (id, del_state, sponsorid, sponsor_name, location, address, condition_type, sponsor_claim, state, adv, `condition`," +
            " release_time, conditional_description, add_condition, markdown_adv, carousel, activ_type)" +
            " VALUES (#{activ.id}, #{activ.delState}, #{activ.sponsorid}, #{activ.sponsorName}, #{activ.location}, #{activ.address}, #{activ.conditionType}" +
            ", #{activ.sponsorClaim}, #{activ.state}, #{activ.adv}, #{activ.condition}, now(), #{activ.conditionalDescription}," +
            " #{activ.addCondition}, #{activ.markdownAdv}, #{activ.carousel}, #{activ.activType})")
//    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
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

    @Select("SELECT * FROM lottery_activ WHERE carousel = #{carousel}")
    List<Activ> queryByCarousel(@Param("carousel") Integer carousel);


    @Select("SELECT * FROM lottery_activ WHERE activ_type = #{activType} AND state =#{state}")
    List<Activ> queryByActivTypeAndState(@Param("activType") Integer activType, @Param("state") Integer state);

    @Select("SELECT la.id, la.`condition`, la.add_condition, la.conditional_description FROM lottery_activ la WHERE la.activ_type = #{activType} AND la.state =#{state}")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(property = "ativPrizes", column = "id",
                    many = @Many(select = "com.md.luck.lottery.mapper.AtivPrizeMapper.queryByAtivId"))
    })
    List<WeixnActiv> queryWeixinActiv(@Param("activType") Integer activType, @Param("state") Integer state);

    @Select("SELECT la.id, la.state, la.`condition`, la.add_condition, la.popularity, la.count_num, la.conditional_description, la.sponsor_claim, la.sponsor_name " +
            "FROM lottery_activ la WHERE la.activ_type = #{activType}  AND la.id = #{id}")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(property = "ativPrizes", column = "id",
                    many = @Many(select = "com.md.luck.lottery.mapper.AtivPrizeMapper.queryByAtivId"))
    })
    WeixinActivChildChild queryWeixinActivByIdAndActivType(@Param("activType") Integer activType,  @Param("id") Long id);

    @Select("SELECT popularity FROM  lottery_activ  WHERE id = #{id}")
    int queryPopularity(@Param("id") Long id);

    @Update("UPDATE lottery_activ SET popularity = #{popularity} WHERE id = #{id}")
    int updatePopularity(@Param("popularity") Integer popularity, @Param("id") Long id);

    @Update("UPDATE lottery_activ SET popularity = #{popularity}, count_num = #{countNum} WHERE id = #{id}")
    int updatePopularityAndCountNum(@Param("popularity") Integer popularity, @Param("countNum") Integer countNum, @Param("id") Long id);

    @Select("SELECT count_num FROM  lottery_activ  WHERE id = #{id}")
    int queryCountNum(@Param("id") Long id);

    @Update("UPDATE lottery_activ SET count_num = #{countNum} WHERE id = #{id}")
    int updateCountNum(@Param("countNum") Integer countNum, @Param("id") Long id);

    @Update("UPDATE lottery_activ SET popularity = #{popularity}, state = #{state} WHERE id = #{id}")
    int updatePopularityAndState(@Param("popularity") Integer popularity, @Param("state") Integer state, @Param("id") Long id);

    @Update("UPDATE lottery_activ SET popularity = #{popularity}, state = #{state}, count_num = #{countNum} WHERE id = #{id}")
    int updatePopularityAndCounNumAndState(@Param("popularity") Integer popularity, @Param("state") Integer state, @Param("countNum") Integer countNum,  @Param("id") Long id);

    @Select("SELECT la.id, la.`condition`, la.add_condition, la.conditional_description, laar.add_time FROM lottery_activ la JOIN lottery_activity_add_record laar ON la.id = laar.activ_id " +
            "WHERE laar.openid = #{openid} ORDER BY laar.add_time DESC")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(property = "ativPrizes", column = "id",
                    many = @Many(select = "com.md.luck.lottery.mapper.AtivPrizeMapper.queryByAtivId"))
    })
    List<WeixinActivRecord> aueryGrabRecordByOpenid(@Param("openid") String openid);

    @Select("SELECT la.id, la.`condition`, la.add_condition, la.conditional_description FROM lottery_activ la " +
            "WHERE la.id = #{id}")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(property = "ativPrizes", column = "id",
                    many = @Many(select = "com.md.luck.lottery.mapper.AtivPrizeMapper.queryByAtivId"))
    })
    WeixinActivRecord aueryGrabRecordById(@Param("id") Long id);

    @Select("SELECT la.id, la.`condition`, la.add_condition, la.conditional_description, llr.add_time FROM lottery_activ la JOIN lottery_lucky_record llr ON la.id = llr.activ_id " +
            "WHERE llr.openid = #{openid} ORDER BY llr.add_time DESC")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(property = "ativPrizes", column = "id",
                    many = @Many(select = "com.md.luck.lottery.mapper.AtivPrizeMapper.queryByAtivId"))
    })
    List<WeixinActivRecord> aueryLuckRecordByOpenid(@Param("openid") String openid);
}
