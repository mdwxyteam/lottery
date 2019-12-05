package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.PayCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PayCodeMapper {

    @Insert("INSERT INTO lottery_pay_code (pay_code, actual_price, status) VALUES (#{payCode.payCode}, #{payCode.actualPrice}, #{payCode.status})")
    void insert(@Param("payCode")PayCode payCode);

    @Select("SELECT * FROM lottery_pay_code WHERE status = #{status} AND pay_code = #{payCode}")
    PayCode queryByPayCode(@Param("status") Integer status, @Param("payCode") String payCode);

    @Update("UPDATE lottery_pay_code SET status = #{status} WHERE id = #{id}")
    void updatePayCodeStatus(@Param("status") int status, @Param("id") long id);
}
