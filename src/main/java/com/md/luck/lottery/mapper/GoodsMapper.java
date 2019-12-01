package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Insert("INSERT INTO lottery_goods (goods_name, goods_img, price, state, pay_num)" +
            "VALUES (#{goods.goodsName}, #{goods.goodsImg}, #{goods.price}, #{goods.state}, #{goods.payNum})")
    int insert(@Param("goods") Goods goods);

    @Select("<script> SELECT * FROM lottery_goods WHERE " +
            "state = #{state}" +
            "<if test='goodsName !=null'> AND LOCATE(#{goodsName}, `goods_name`)>0 </if>" +
            "</script>")
    List<Goods> conditionPage(@Param("state") int state, @Param("goodsName") String goodsName);

    @Update("<script> UPDATE lottery_goods <set>" +
            "<if test='goods.goodsName != null'> goods_name = #{goods.goodsName} </if>," +
            "<if test='goods.goodsImg != null'> goods_img = #{goods.goodsImg} </if>," +
            "<if test='goods.price != null'> price = #{goods.price} </if>," +
            "<if test='goods.state != null'> state = #{goods.state} </if>," +
            "<if test='goods.payNum != null'> pay_num = #{goods.payNum} </if>" +
            "</set>" +
            "WHERE id = #{goods.id}" +
            "</script>")
    int edit(@Param("goods") Goods goods);

    @Select("SELECT * FROM lottery_goods WHERE state = #{state}")
    List<Goods> queryByState(@Param("state") int state);
}
