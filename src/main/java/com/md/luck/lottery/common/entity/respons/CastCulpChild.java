package com.md.luck.lottery.common.entity.respons;

import com.md.luck.lottery.common.entity.CastCulp;
import lombok.Data;

@Data
public class CastCulpChild extends CastCulp {
    /**
     * 助力 数量
     */
    private Integer totalCulp;
    /**
     * 排名
     */
    private Integer rank;
}
