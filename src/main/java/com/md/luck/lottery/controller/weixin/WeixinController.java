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
@RequestMapping("/weixin/api")
public class WeixinController extends BaseController {

    @GetMapping("/code")
    public ResponMsg getCode() {
        return weixinService.getCode();
    }
//    @GetMapping("/userInfo")
//    public ResponMsg getUserInfo(@RequestParam("code") String code,
//                              @RequestParam("state") String state) {
//        System.out.println(code);
//        System.out.println(state);
//        ResponMsg responMsg = weixinService.getToken(state, code);
//        return responMsg;
//    }

    /**
     * 获取所有正在售卖商品数据
     *
     * @return ResponMsg
     */
    @GetMapping("/goods/state")
    public ResponMsg queryAllGoods() {
        return goodsService.queryByState(Cont.SELL);
    }

    /**
     * 查询上轮播的所有活动
     *
     * @return ResponMsg
     */
    @GetMapping("/activ/carousel")
    public ResponMsg queryByCarousel() {
        return activPrizeService.queryByCarousel(Cont.ONE);
    }

    /**
     * 通过openid获取用户数据
     * @param openid openid
     * @return ResponMsg
     */
    @GetMapping("/user/info")
    public ResponMsg getUserInfo(@RequestParam("openid") String openid) {
        return customerService.getUserInfo(openid);
    }

    /**
     * 创建订单
     *
     * @param requestBodyOrder 订单参数
     * @return ResponMsg
     */
    @PostMapping("/creat/order")
    public ResponMsg creatOrder(@RequestBody RequestBodyOrder requestBodyOrder) {
        return orderService.creatOrder(requestBodyOrder);
    }
}
