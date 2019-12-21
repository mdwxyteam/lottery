package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.LotteryOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO lottery_order (goods_name, goods_id, order_price, creat_time, openid, nick_name, pay_code, pay_code_id) VALUES" +
            "(#{order.goodsName}, #{order.goodsId}, #{order.orderPrice}, now(), #{order.openid}, #{order.nickName}, #{order.payCode}, #{order.payCodeId})")
    void creatOrder(@Param("order") LotteryOrder order);

    @Select("SELECT nick_name, pay_code, order_price, goods_name, pay_code FROM lottery_order WHERE goods_id = #{goodsId} AND openid =#{openid}")
    LotteryOrder queryByOpenidAndGoodsId(@Param("goodsId") Long goodsId, @Param("openid") String openid);
}
