package com.md.luck.lottery.controller.weixin;

import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.RequestBodyOrder;
import com.md.luck.lottery.controller.BaseController;
import org.springframework.web.bind.annotation.*;

/**
 * 微信端api
 */
@RestController
@RequestMapping("/lottery/api")
public class WeixinController extends BaseController {
    /**
     * 获取所有正在售卖商品数据
     * @return ResponMsg
     */
    @GetMapping("/goods/state")
    public ResponMsg queryAllGoods() {
        return goodsService.queryByState(Cont.SELL);
    }

    /**
     * 查询上轮播的所有活动
     * @return ResponMsg
     */
    @GetMapping("/activ/carousel")
    public ResponMsg queryByCarousel() {
        return activPrizeService.queryByCarousel(Cont.ONE);
    }

    /**
     * 创建订单
     * @param requestBodyOrder 订单参数
     * @return ResponMsg
     */
    @PostMapping("/creat/order")
    public ResponMsg creatOrder(@RequestBody RequestBodyOrder requestBodyOrder) {
        return orderService.creatOrder(requestBodyOrder);
    }
}
