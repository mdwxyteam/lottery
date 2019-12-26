package com.md.luck.lottery.common.entity;

import lombok.Data;

@Data
public class WeixinActivChildChild  extends  WeixinActivChild{
    /**
     * 人气 热度
     */
    private Integer popularity;
    /**
     * 1:未结束；0结束；-1测试
     */
    private Integer state;
}
