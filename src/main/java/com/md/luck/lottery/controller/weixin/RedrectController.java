package com.md.luck.lottery.controller.weixin;

import com.md.luck.lottery.common.entity.BaseEntity;
import com.md.luck.lottery.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/weixin/api")
public class RedrectController extends BaseController {

    @Value("${weixin.loginurl}")
    private String loginurl;
    @RequestMapping("/userInfo")
    public String getUserInfo(@RequestParam("code") String code,
                              @RequestParam("state") String state) {
        System.out.println(code);
        System.out.println(state);
        Map<String, Object> map = (Map<String, Object>) weixinService.getToken(state, code).getData();
        return "redirect:" + state + "?openid=" + map.get("openid") + "&token=" + map.get("token");
    }
    @RequestMapping("/login")
    public String login() {
        return "redirect:" + loginurl;
    }
}
