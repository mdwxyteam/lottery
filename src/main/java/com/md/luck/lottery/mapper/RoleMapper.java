package com.md.luck.lottery.mapper;

import com.md.luck.lottery.common.entity.JRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("SELECT lc.nick_name, wr.role_name, m.url FROM lottery_customer lc JOIN j_user_role ur ON lc.openid = ur.openid JOIN j_weixin_role wr ON ur.role_id = wr.id" +
            " JOIN j_menu_role mr ON wr.id = mr.role_id JOIN j_menu m ON m.id = mr.menu_id WHERE lc.openid = #{openid}")
    List<JRole> queryByOpenid(@Param("openid") String openid);

    @Insert("INSERT INTO j_user_role (openid, role_id) VALUES (#{openid}, #{roleId})")
    void insertUserAuth(@Param("openid") String openid, @Param("roleId") Long roleid);
}
