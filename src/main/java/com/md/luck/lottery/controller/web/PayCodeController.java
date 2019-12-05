package com.md.luck.lottery.controller.web;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.controller.BaseController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lottery/pay")
public class PayCodeController extends BaseController {
    /**
     * 创建支付code
     * @param price 价格
     * @return ResponMsg
     */
    @PostMapping("/price/code")
    public ResponMsg creatPayCode(@RequestParam("price") int price) {
        return payCodeService.creatPayCode(price);
    }
}
