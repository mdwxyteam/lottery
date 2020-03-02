package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.ActivityAddRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ActivityAddRecordMapper {
    @Insert("INSERT INTO lottery_activity_add_record (id, activ_id, openid, nick_name, icon, culp, rank, team_mate_count, add_time) VALUES (#{activityAddRecord.id}, #{activityAddRecord.activId}," +
            "#{activityAddRecord.openid}, #{activityAddRecord.nickName}, #{activityAddRecord.icon}, #{activityAddRecord.culp}, #{activityAddRecord.rank}, #{activityAddRecord.teamMateCount}, #{activityAddRecord.addTime})")
//    @Options(useGeneratedKeys = true, keyProperty = "activityAddRecord.id")
    void add(@Param("activityAddRecord") ActivityAddRecord activityAddRecord);

    @Insert("<script> INSERT INTO lottery_activity_add_record (id, activ_id, openid, nick_name, icon, culp, rank, team_mate_count, add_time) VALUES " +
            "<foreach collection='activityAddRecords' item='item' index='index' separator=','>" +
            "(#{item.id}, #{item.activId}, #{item.openid}, #{item.nickName}, #{item.icon}, #{item.culp}, #{item.rank}, #{item.teamMateCount}, #{item.addTime})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("activityAddRecords") List<ActivityAddRecord> activityAddRecords);

    @Select("SELECT activ_id FROM lottery_activity_add_record WHERE openid = #{openid}")
    ActivityAddRecord queryByOpenid(@Param("openid") String openid);

    @Select("SELECT * FROM lottery_activity_add_record WHERE activ_id = #{activId} ORDER BY rank DESC")
    List<ActivityAddRecord> queryByActivId(@Param("activId") Long activId);

    @Select("SELECT * FROM lottery_activity_add_record laar JOIN lottery_activ  la ON la.id = laar.activ_id " +
            "WHERE la.activ_type = #{activType} AND la.state =#{state}")
    List<ActivityAddRecord> queryByActiv(@Param("activType") Integer activType, @Param("state") Integer state);

    @Select("SELECT culp, team_mate_count FROM lottery_activity_add_record WHERE id = #{id} AND openid = #{openid}")
    ActivityAddRecord queryTeamMateCountAndCulp( @Param("id") Long id, @Param("openid") String openid);

    @Select("SELECT * FROM lottery_activity_add_record WHERE openid = #{openid} AND activ_id = #{activid}")
    ActivityAddRecord queryByOpenidAndId(@Param("openid") String openid, @Param("activid") Long activid);

    @Select("SELECT * FROM lottery_activity_add_record WHERE id = #{id}")
    ActivityAddRecord queryById( @Param("id") Long id);

    @Select("SELECT id, team_mate_count, @curRank := @curRank + 1 AS rank " +
            "FROM lottery_activity_add_record P, (SELECT @curRank := 0) q WHERE id = #{id} AND openid = #{openid} ORDER BY culp DESC ")
    ActivityAddRecord queryTeamMateCountAndRank( @Param("id") Long id, @Param("openid") String openid);

    @Update("UPDATE lottery_activity_add_record SET culp = #{culp}, team_mate_count = #{teamMateCount} WHERE id = #{id} AND openid = #{openid}")
    int updateCulpAndTeamMateCount( @Param("culp") Integer culp, @Param("teamMat   eCount") Integer teamMateCount, @Param("id") Long id, @Param("openid") String openid);

    @Update("UPDATE lottery_activity_add_record SET rank = #{rank} WHERE id = #{id} AND openid = #{openid}")
    int updateCulpRank( @Param("rank") Long rank, @Param("id") Long id, @Param("openid") String openid);


}
