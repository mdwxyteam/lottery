package com.md.luck.lottery.common.util;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatMpConfig {

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.appsecret}")
    private String appsecret;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpConfigStorage wxMpConfigStorage = new WxMpDefaultConfigImpl();
        ((WxMpDefaultConfigImpl) wxMpConfigStorage).setAppId(appid);
        ((WxMpDefaultConfigImpl) wxMpConfigStorage).setSecret(appsecret);
        return wxMpConfigStorage;
    }
}
