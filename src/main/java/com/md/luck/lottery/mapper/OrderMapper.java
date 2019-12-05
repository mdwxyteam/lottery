package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.LotteryOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO lottery_order (goods_name, goods_id, order_price, creat_time, openid, nick_name, pay_code, pay_code_id) VALUES" +
            "(#{order.goodsName}, #{order.goodsId}, #{order.orderPrice}, #{order.creatTime}, #{order.openid}, #{order.nickName}, #{order.payCode}, #{order.payCodeId})")
    void creatOrder(@Param("order") LotteryOrder order);
}
