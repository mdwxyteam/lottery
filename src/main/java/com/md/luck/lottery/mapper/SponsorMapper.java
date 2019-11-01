package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Sponsor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SponsorMapper {

    @Insert("INSERT INTO lottery_sponsor (sponsor_name, type, detalis," +
            " location, address, type_id, creat_time, status, mark_down," +
            " principal, contact, cover) VALUES" +
            " (#{sponsor.sponsorName},#{sponsor.type} ,#{sponsor.detalis} ,#{sponsor.location} ," +
            " #{sponsor.address} ,#{sponsor.typeId}, now(), #{sponsor.status}, #{sponsor.markDown}," +
            " #{sponsor.principal}, #{sponsor.contact}, #{sponsor.cover})")
    int add(@Param("sponsor") Sponsor sponsor);

    @Select("SELECT * FROM lottery_sponsor")
    List<Sponsor> all();

    @Select("SELECT * FROM lottery_sponsor WHERE id = #{sponsorId}")
    Sponsor bySponsorId(@Param("sponsorId") Long sponsorId);

    @Select("<script>SELECT * FROM lottery_sponsor WHERE status = #{status}" +
            "<if test= 'typeId !=null'>  AND type_id = #{typeId}</if>" +
            "<if test='sponsorName !=null'> AND LOCATE(#{sponsorName}, `sponsor_name`)>0 </if>" +
            "</script>")
    List<Sponsor> byType(@Param("typeId") Long typeId, @Param("sponsorName") String sponsorName, @Param("status") int status);

    @Update("<script> UPDATE lottery_sponsor <set> " +
            "<if test='sponsor.sponsorName != null'> sponsor_name = #{sponsor.sponsorName} </if>," +
            "<if test='sponsor.type != null'> type = #{sponsor.type} </if>," +
            "<if test='sponsor.detalis != null'> detalis = #{sponsor.detalis} </if>," +
            "<if test='sponsor.typeId != null'> type_id = #{sponsor.typeId} </if>," +
            "<if test='sponsor.location != null'> location = #{sponsor.location} </if>," +
            "<if test='sponsor.address != null'> address = #{sponsor.address} </if>," +
            "<if test='sponsor.principal != null'> principal = #{sponsor.principal} </if>," +
            "<if test='sponsor.contact != null'> contact = #{sponsor.contact} </if>," +
            "<if test='sponsor.cover != null'> cover = #{sponsor.cover} </if>," +
            "<if test='sponsor.markDown != null'> mark_down = #{sponsor.markDown} </if>" +
            "</set>" +
            "WHERE id = #{sponsor.id}" +
            "</script>")
    int update(@Param("sponsor") Sponsor sponsor);

    @Update("UPDATE lottery_sponsor SET status = #{status} WHERE id = #{id}")
    int delByStatus(@Param("id") long id, @Param("status") int status);

    @Select("<script>SELECT * FROM lottery_sponsor WHERE status = #{sponsor.status}" +
            "<if test= 'sponsor.typeId !=null'>  AND type_id = #{sponsor.typeId}</if>" +
            "<if test='sponsor.sponsorName !=null'> AND LOCATE(#{sponsor.sponsorName}, `sponsor_name`)>0 </if>" +
            "</script>")
    List<Sponsor> query(@Param("sponsor") Sponsor sponsor);
}
