package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.SponsorType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @Insert("INSERT INTO lottery_sponsor_type (type_name) VALUES (#{sponsorType.typeName})")
    void add(@Param("sponsorType") SponsorType sponsorType);

    /**
     * 查询所有商户类型信息
     * @return List<SponsorType>
     */
    @Select("SELECT * FROM lottery_sponsor_type")
    List<SponsorType> all();


}
