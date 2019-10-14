package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Sponsor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SponsorMapper {

    @Insert("INSERT INTO lottery_sponsor (sponsor_name, type, detalis, location, address, type_id, creat_time, status) VALUES (#{sponsor.sponsorName},#{sponsor.type} ,#{sponsor.detalis} ,#{sponsor.location} , #{sponsor.address} ,#{sponsor.typeId}, now(), #{sponsor.status})")
    int add(@Param("sponsor") Sponsor sponsor);

    @Select("SELECT * FROM lottery_sponsor")
    List<Sponsor> all();

    @Select("SELECT * FROM lottery_sponsor WHERE id = #{sponsorId}")
    Sponsor bySponsorId(@Param("sponsorId") Long sponsorId);

    @Select("<script>SELECT * FROM lottery_sponsor WHERE status = #{status}" +
            "<if test= 'typeId !=null'>  AND type_id = #{typeId}</if>" +
            "<if test='sponsorName !=null'> AND sponsor_name = #{sponsorName}</if>" +
            "</script>")
    List<Sponsor> byType(@Param("typeId") Long typeId, @Param("sponsorName") String sponsorName, @Param("status") int status);

    @Update("<script> UPDATE lottery_sponsor <set> " +
            "<if test='sponsor.sponsorName != null'> sponsor_name = #{sponsor.sponsorName} </if>," +
            "<if test='sponsor.type != null'> type = #{sponsor.type} </if>," +
            "<if test='sponsor.detalis != null'> detalis = #{sponsor.detalis} </if>," +
            "<if test='sponsor.typeId != null'> type_id = #{sponsor.typeId} </if>," +
            "<if test='sponsor.location != null'> location = #{sponsor.location} </if>," +
            "<if test='sponsor.address != null'> address = #{sponsor.address} </if>" +
            "</set>" +
            "WHERE id = #{sponsor.id}" +
            "</script>")
    int update(Sponsor sponsor);

    @Update("UPDATE lottery_sponsor SET status = #{status}")
    int delByStatus(@Param("status") int status);
}
