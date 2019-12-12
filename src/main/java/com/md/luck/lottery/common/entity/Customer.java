package com.md.luck.lottery.common.entity;

import lombok.Data;

/**
 * 微信用户实体类
 */
@Data
public class Customer extends BaseEntity{
    private String openid;
    private String uuserid;
    /**
     * 性别，0：无；1：男；2：女
     */
    private Integer sex = 0;
    private String icon;
    private String nickName;
}
