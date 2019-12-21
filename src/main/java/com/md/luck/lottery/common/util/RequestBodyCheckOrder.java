package com.md.luck.lottery.common.util;

import com.md.luck.lottery.common.entity.BaseEntity;
import lombok.Data;

/**
 * 检查用户订单请求参数实体类
 */
@Data
public class RequestBodyCheckOrder extends BaseEntity {
    private Long goodsId;
}
