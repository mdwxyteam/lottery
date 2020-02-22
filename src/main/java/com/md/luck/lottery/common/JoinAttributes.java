package com.md.luck.lottery.common;

import lombok.Data;

/**
 * 活动参与记录实体类属性
 */
@Data
public class JoinAttributes {
    private String id = "id";
    private String openid = "openid";
    private String activId = "activId";
    private String nickName = "nickName";
    private String icon = "icon";
    private String culp = "culp";
    private String rank = "rank";
    private String teamMateCount = "teamMateCount";
    private String addTime = "addTime";

    private JoinAttributes(String openid) {
        this.openid = this.openid + "_" + openid;
        this.id = id + "_" + openid;
        this.activId = this.activId + "_" + openid;
        this.nickName = this.nickName + "_" + openid;
        this.icon = this.icon + "_" + openid;
        this.culp = this.culp + "_" + openid;
        this.rank = this.rank + "_" + openid;
        this.teamMateCount = this.teamMateCount + "_" + openid;
        this.addTime = this.addTime + "_" + openid;
    }

    public static JoinAttributes getInstance(String openid) {
        return new JoinAttributes(openid);
    }
}
