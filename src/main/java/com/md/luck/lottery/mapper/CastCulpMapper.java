package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.CastCulp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CastCulpMapper {

    @Insert("INSERT INTO lottery_cast_culp_record (activ_id, act_add_record_id, openid, nick_name, icon, cast_culp, record_openid) VALUES ( #{castCulp.activid}," +
            "#{castCulp.actAddRecordId}, #{castCulp.openid}, #{castCulp.nickName}, #{castCulp.icon}, #{castCulp.castCulp}, #{castCulp.recordOpenid})")
    void insert(@Param("castCulp")CastCulp castCulp);

    @Select("SELECT * FROM lottery_cast_culp_record lccr JOIN lottery_activ  la ON la.id = lccr.activ_id " +
            "WHERE la.activ_type = #{activType} AND la.state =#{state} ")
    List<CastCulp> queryByActiv(@Param("activType") Integer activType, @Param("state") Integer state);

    @Select("SELECT * FROM lottery_cast_culp_record WHERE openid = #{openid} AND activ_id = #{activid}")
    CastCulp queryByOpenidAndActivId(@Param("openid") String openid, @Param("activid") Long activid);

    @Select("SELECT * FROM lottery_cast_culp_record WHERE act_add_record_id = #{recordId}")
    List<CastCulp> queryByRecordId(@Param("recordId") Long recordId);
}
