package com.md.luck.lottery.common;

import com.md.luck.lottery.common.entity.Prize;
import lombok.Data;

@Data
public class PrizeChild extends Prize {
    /**
     * 第几名可以获取
     */
    private String ranking;
}
