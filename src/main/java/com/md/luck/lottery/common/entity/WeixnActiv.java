package com.md.luck.lottery.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class WeixnActiv extends BaseEntity{
    /**
     * 开奖条件（2019.12.09 || 3000）
     */
    private String condition;
    /**
     * 参与条件
     */
    private String addcondition;
    /**
     * 参与条件
     */
    private String conditionalDescription;
    /**
     * 奖品
     */
    private List<AtivPrize> ativPrizes;
}
