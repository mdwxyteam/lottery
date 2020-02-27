package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Insert("INSERT INTO lottery_goods (id, goods_name, goods_img, price, state, pay_num, actual_price, goods_group, group_icon)" +
            "VALUES (#{goods.id}, #{goods.goodsName}, #{goods.goodsImg}, #{goods.price}, #{goods.state}, #{goods.payNum}, #{goods.actualPrice}, #{goods.goodsGroup}, #{goods.groupIcon})")
    int insert(@Param("goods") Goods goods);

    @Select("<script> SELECT * FROM lottery_goods WHERE " +
            "state = #{state}" +
            "<if test='goodsName !=null'> AND LOCATE(#{goodsName}, `goods_name`)>0 </if>" +
            "</script>")
    List<Goods> conditionPage(@Param("state") int state, @Param("goodsName") String goodsName);

    @Update("<script> UPDATE lottery_goods <set>" +
            "<if test='goods.goodsName != null'> goods_name = #{goods.goodsName}, </if>" +
            "<if test='goods.goodsImg != null'> goods_img = #{goods.goodsImg}, </if>" +
            "<if test='goods.price != null'> price = #{goods.price}, </if>" +
            "<if test='goods.state != null'> state = #{goods.state}, </if>" +
            "<if test='goods.actualPrice != null'> actual_price = #{goods.actualPrice}, </if>" +
            "<if test='goods.goodsGroup != null'> goods_group = #{goods.goodsGroup}, </if>" +
            "<if test='goods.groupIcon != null'> group_icon = #{goods.groupIcon}, </if>" +
            "<if test='goods.payNum != null'> pay_num = #{goods.payNum} </if>" +
            "</set>" +
            "WHERE id = #{goods.id}" +
            "</script>")
    int edit(@Param("goods") Goods goods);

    @Select("SELECT id, goods_name, goods_img, price, actual_price, pay_num  FROM lottery_goods WHERE state = #{state}")
    List<Goods> queryByStateWeixin(@Param("state") int state);

    @Select("SELECT *  FROM lottery_goods WHERE state = #{state}")
    List<Goods> queryByState(@Param("state") int state);

    @Select("SELECT * FROM lottery_goods WHERE state = #{state} AND id = #{id}")
    Goods queryByStateAndId(@Param("state") int state, @Param("id") Long id);

    @Select("SELECT * FROM lottery_goods WHERE id = #{id}")
    Goods queryById( @Param("id") Long id);
}
