package com.md.luck.lottery.controller.weixin;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.BaseEntity;
import com.md.luck.lottery.common.util.WechatMpConfig;
import com.md.luck.lottery.controller.BaseController;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/weixin/api")
public class RedrectController extends BaseController {

    @Autowired
    private WechatMpConfig wechatMpConfig;

    @Value("${weixin.loginurl}")
    private String loginurl;
    @RequestMapping("/userInfo")
    public String getUserInfo(@RequestParam("code") String code,
                              @RequestParam("state") String state) {

        Map<String, Object> map = (Map<String, Object>) weixinService.getToken(state, code).getData();
        return "redirect:" + state + "?openid=" + map.get("openid") + "&token=" + map.get("token");
    }
    @RequestMapping("/login")
    public String login() {
        return "redirect:" + loginurl;
    }

    /**
     * 分享H5相关页码
     * @param request request
     * @param url
     * @return
     */
    @RequestMapping(value = "/share/config", method = RequestMethod.GET)
    @ResponseBody
    public ResponMsg wxJsSdkConfig(HttpServletRequest request, String url) {
        ResponMsg responMsg = null;
        try { // 直接调用wxMpServer 接口
            WxMpService wxMpService = wechatMpConfig.wxMpService();
            // todo AccessToken过期刷新
//            wechatMpConfig.
//            wxMpService.g();
//            wxMpService.oauth2getAccessToken()
//            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
//            WxMpDefaultConfigImpl wxMpConfigStorage = (WxMpDefaultConfigImpl) wechatMpConfig.wxMpConfigStorage();
            WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);
            responMsg = ResponMsg.newSuccess(wxJsapiSignature);
        } catch (WxErrorException e) {
            responMsg = ResponMsg.newFail(e).setMsg("异常..");
        }
        return responMsg;
    }
}
