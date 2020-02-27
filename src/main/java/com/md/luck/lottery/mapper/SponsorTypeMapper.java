package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.SponsorType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: SponsorTypeMapper
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/8/29 10:48
 */
@Mapper
public interface SponsorTypeMapper {

    /**
     * 保存商户类型接口
     * @param sponsorType
     */
    @Insert("INSERT INTO lottery_sponsor_type (id, type_name,is_status) VALUES (#{sponsorType.id},#{sponsorType.typeName},1)")
    void add(@Param("sponsorType") SponsorType sponsorType);

    /**
     * 查询所有商户类型信息
     * @return List<SponsorType>
     */
    @Select("SELECT * FROM lottery_sponsor_type")
    List<SponsorType> all();

    /**
     *  更新奖品
     * @param sponsorType
     * @return
     */
    @Update("UPDATE lottery_sponsor_type SET type_name = #{sponsorType.typeName} WHERE id = #{sponsorType.id}")
    int update(@Param("sponsorType") SponsorType sponsorType);

    /**
     *  更新奖品状态
     * @param sponsorType
     * @return
     */
    @Update("UPDATE lottery_sponsor_type SET is_status = #{sponsorType.isStatus} WHERE id = #{sponsorType.id}")
    int updateStatus(@Param("sponsorType") SponsorType sponsorType);

}



