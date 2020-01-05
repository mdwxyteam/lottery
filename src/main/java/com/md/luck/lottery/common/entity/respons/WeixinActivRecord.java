package com.md.luck.lottery.common.entity.respons;

import com.md.luck.lottery.common.entity.WeixnActiv;
import lombok.Data;

import java.util.Date;

@Data
public class WeixinActivRecord  extends WeixnActiv {
    /**
     * 添加时间
     */
    private Date addTime;
}
