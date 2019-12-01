package com.md.luck.lottery.controller.weixin;

import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
