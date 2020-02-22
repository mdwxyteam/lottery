package com.md.luck.lottery.common;

import lombok.Data;

/**
 * 助力记录实体类属性
 */
@Data
public class CastAttributes {
    private String id = "id";
    private String activid = "activid";
    private String actAddRecordId = "actAddRecordId";
    private String openid = "openid";
    private String nickName = "nickName";
    private String icon = "icon";
    private String castCulp = "castCulp";

    public CastAttributes(Long id) {
        this.id = this.id + "_" + id;
        this.activid = this.activid + "_" + id;
        this.actAddRecordId = this.actAddRecordId + "_" + id;
        this.openid = this.openid + "_" + id;
        this.nickName = this.nickName + "_" + id;
        this.icon = this.icon + "_" + id;
        this.castCulp = this.castCulp + "_" + id;
    }
    public static CastAttributes getInstance(Long id) {
        return new CastAttributes(id);
    }
}
