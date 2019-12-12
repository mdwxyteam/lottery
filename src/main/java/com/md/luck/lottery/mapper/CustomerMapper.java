package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CustomerMapper {

    @Insert("INSERT INTO lottery_customer (openid, uuserid, sex, icon, nick_name) VALUES" +
            " (#{customer.openid}, #{customer.uuserid}, #{customer.sex}, #{customer.icon}" +
            ", #{customer.nickName})")
    int add(@Param("customer") Customer customer);

    @Select("SELECT * FROM lottery_customer WHERE openid = #{openid}")
    Customer queryByOpenid(@Param("openid") String openid);

}
