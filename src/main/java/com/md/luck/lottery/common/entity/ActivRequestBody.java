package com.md.luck.lottery.common.entity;

import com.md.luck.lottery.common.PrizeChild;
import lombok.Data;

import java.util.List;

@Data
public class ActivRequestBody extends Activ{
    private List<PrizeChild> prizeList;
    /**
     * 关联奖品数据是否更改
     */
    private boolean prizeBool = false;
}
