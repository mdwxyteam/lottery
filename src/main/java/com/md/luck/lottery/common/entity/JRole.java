package com.md.luck.lottery.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class JRole extends BaseEntity{
    private String nickName;
    private String roleName;
    private String openid;
    private List<String> urls;
}
