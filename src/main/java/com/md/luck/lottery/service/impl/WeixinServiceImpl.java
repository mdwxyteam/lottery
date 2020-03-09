package com.md.luck.lottery.service.impl;

import cn.hutool.crypto.CryptoException;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.JoinAttributes;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.util.MaDateUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.common.util.URIEncoder;
import com.md.luck.lottery.common.util.WechatMpConfig;
import com.md.luck.lottery.config.authority.JwtTokenUtil;
import com.md.luck.lottery.config.authority.SecurityUserDetails;
import com.md.luck.lottery.mapper.CustomerMapper;
import com.md.luck.lottery.mapper.RoleMapper;
import com.md.luck.lottery.service.WexinService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


@Service
public class WeixinServiceImpl implements WexinService {
    private static final Log log = LogFactory.getLog(WeixinServiceImpl.class);

    @Value("${weixin.redirecturi}")
    private String redirectUri;
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.returnurl}")
    private String returnUrl;
    @Autowired
    private WechatMpConfig wechatMpConfig;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisServiceImpl redisService;

    @Override
    public ResponMsg getCode() {
        WxMpService wxMpService = wechatMpConfig.wxMpService();
        String str = null;
        try {
            str = wxMpService.oauth2buildAuthorizationUrl(redirectUri, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponMsg.newSuccess(str);
    }

    @Transactional(rollbackFor = {SqlSessionException.class, WxErrorException.class})
    @Override
    public ResponMsg getToken(String state, String code) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        ResponMsg responMsg = null;
        try {
            wxMpOAuth2AccessToken = wechatMpConfig.wxMpService().oauth2getAccessToken(code);
            WxMpUser wxMpUser = wechatMpConfig.wxMpService().oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            if (!MaObjUtil.isEmpty(wxMpUser) || !MaObjUtil.isEmpty(wxMpUser.getOpenId())) {
                Customer customer = customerMapper.queryByOpenid(wxMpUser.getOpenId());
                if (MaObjUtil.isEmpty(customer)) {
                    //插入数据
                    Customer customerData = new Customer();
                    customerData.setOpenid(wxMpUser.getOpenId());
                    customerData.setSex(wxMpUser.getSex());
                    customerData.setUuserid(wxMpUser.getUnionId());
                    customerData.setIcon(wxMpUser.getHeadImgUrl());
                    customerData.setNickName(wxMpUser.getNickname());
                    customerMapper.add(customerData);
                    roleMapper.insertUserAuth(wxMpUser.getOpenId(), Cont.WEIXIN_USER_ROLE);
                }

                //生成token
                SecurityUserDetails securityUserDetails = new SecurityUserDetails(wxMpUser.getNickname(), null);
                securityUserDetails.setOpenid(wxMpUser.getOpenId());
                securityUserDetails.setNickName(wxMpUser.getNickname());
                securityUserDetails.setIcon(wxMpUser.getHeadImgUrl());
                String token = jwtTokenUtil.generateOpenidToken(securityUserDetails);
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("openid", wxMpUser.getOpenId());
                reMap.put("token", token);
                responMsg = ResponMsg.newSuccess(reMap);
            } else {
                return ResponMsg.newFail(null).setMsg("缺省参数");
            }
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        } catch (WxErrorException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg getCustomer(String openid) {
        if (MaObjUtil.isEmpty(openid)) {
            return ResponMsg.newFail(null);
        }
        ResponMsg responMsg = null;
        try {
            Customer customer = customerMapper.queryByOpenid(openid);
            responMsg = ResponMsg.newSuccess(customer);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg getRoleByOpenid(String openid) {
        if (MaObjUtil.isEmpty(openid)) {
            return ResponMsg.newFail(null);
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<JRole> roles = roleMapper.queryByOpenid(openid);
        String nickName = null;
        for (JRole jRole: roles) {
            if (MaObjUtil.isEmpty(nickName)) {
                nickName = jRole.getNickName();
            }
            authorityList.add(new SimpleGrantedAuthority(jRole.getRoleName()));
        }
        return ResponMsg.newSuccess(new SecurityUserDetails(nickName,authorityList));
    }

    @Override
    public ResponMsg sendCastTemplateMsg(Long activId) {
        if (MaObjUtil.isEmpty(activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        String rKey = Cont.RANK_PRE + activId;
        Long zlength = redisTemplate.opsForZSet().zCard(rKey);
        if (0L == zlength) {
            return ResponMsg.newFail(null).setMsg("没有数据");
        }
        Set<String> openidSet = redisTemplate.opsForZSet().reverseRange(rKey, 0, - 1);
        for (String openid: openidSet) {
            String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
            JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
            Long recordId = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getId());
            Long rank =  redisTemplate.opsForZSet().reverseRank(rKey, openid) + 1;
            String nickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getNickName());
            // 通过查询中奖者，判断是否中奖
            String rlKey = Cont.RANL_LUCKY_PRE + activId;
            String fieldLp = openid;
            boolean isLuck = redisTemplate.opsForHash().hasKey(rlKey, fieldLp);
            String link = "http://smchat.cn/lottery/#/grabDetail?activId=" + activId + "&recordId=" + recordId +"&sopenid=" + openid;
            String encodeLink = URIEncoder.encodeURIComponent(link);
            String url = "http://smchat.cn/lottery//redirect.html?app3Redirect=" + encodeLink;
            String prizeStr = null;
            if (isLuck) {

                LuckPeo luckPeo = (LuckPeo) redisTemplate.opsForHash().get(rlKey, fieldLp);
                // 中奖者，给中奖者发送消息
                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                        .toUser(openid)
                        .templateId(Cont.TEMPLATE_ID)
                        .url(url)
                        .build();
                prizeStr = luckPeo.getPrizeName();
                templateMessage.addData(new WxMpTemplateData("first", "您在Ai捡耙活参与的助力活动中获得了人气" + rank +"名", "red"));
                templateMessage.addData(new WxMpTemplateData("keyword1", "无效字段", "#D0D0D0"));
                templateMessage.addData(new WxMpTemplateData("keyword2", "无效字段", "#D0D0D0"));
                templateMessage.addData(new WxMpTemplateData("keyword3", prizeStr, "blue"));
                templateMessage.addData(new WxMpTemplateData("keyword4", MaDateUtil.getCurrentTime(), "blue"));
                templateMessage.addData(new WxMpTemplateData("remark", "收到通知后，3日内联系客服领取奖品，更多获奖情况，请点击查看", "blue"));
                sendTemplateMsg(templateMessage);
            }else {
                // 给没有中奖者发送模板消息
                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                        .toUser(openid)
                        .templateId(Cont.TEMPLATE_ID)
                        .url(url)
                        .build();
                prizeStr = "没有获得奖品";
                templateMessage.addData(new WxMpTemplateData("first", "您在Ai捡耙活参与的助力活动中获得了人气" + rank +"名", "red"));
                templateMessage.addData(new WxMpTemplateData("keyword1", "无效字段", "#D0D0D0"));
                templateMessage.addData(new WxMpTemplateData("keyword2", "无效字段", "#D0D0D0"));
                templateMessage.addData(new WxMpTemplateData("keyword3", prizeStr, "blue"));
                templateMessage.addData(new WxMpTemplateData("keyword4", MaDateUtil.getCurrentTime(), "blue"));
                templateMessage.addData(new WxMpTemplateData("remark", "人气可能不够哦，下次继续努力吧！更多获奖情况，请点击查看", "blue"));
                sendTemplateMsg(templateMessage);
            }


            // 给助力人员发送模板信息
            String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + recordId;
            if (!redisService.hasData(gKey)) {
                continue;
            }
            Map<String, CastCulp> castCulpMap = redisService.getAllCastCulpToMap(activId, recordId);
            for (Map.Entry<String, CastCulp> entry : castCulpMap.entrySet()) {
                // 助力人员openid
                String culpOpenid = entry.getKey();
                CastCulp castCulp = entry.getValue();
                // 给助力人员发送消息
                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                        .toUser(culpOpenid)
                        .templateId(Cont.TEMPLATE_ID)
                        .url(url)
                        .build();
                templateMessage.addData(new WxMpTemplateData("first", "您在Ai捡耙活给了好友" + nickName + castCulp.getCastCulp() + "个助力；" +
                        nickName + "最终获得了人气" + rank +"名", "red"));
                templateMessage.addData(new WxMpTemplateData("keyword1", "无效字段", "#D0D0D0"));
                templateMessage.addData(new WxMpTemplateData("keyword2", "无效字段", "#D0D0D0"));
                templateMessage.addData(new WxMpTemplateData("keyword3", prizeStr, "blue"));
                templateMessage.addData(new WxMpTemplateData("keyword4", MaDateUtil.getCurrentTime(), "blue"));
                templateMessage.addData(new WxMpTemplateData("remark", "更多获奖情况，请点击查看", "blue"));
                sendTemplateMsg(templateMessage);
            }
        }

        return null;
    }

    @Override
    public ResponMsg sendLuckyTemplateMsg(Long activId, List<LuckyRecord> records, List<LuckPeo> luckyPeples) {
        System.out.println("---------sendLuckyTemplateMsg--------");
        if (MaObjUtil.hasEmpty(records, luckyPeples)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        System.out.println(activId);
        System.out.println(records.size());
        System.out.println(luckyPeples.size());
        String link = "http://smchat.cn/lottery/#/luckyDetail?activId=" + activId;
        System.out.println(link);
        for (LuckyRecord luckyRecord: records) {
            boolean islucky = false;
            for (LuckPeo luckPeo: luckyPeples) {
                if (luckyRecord.getOpenid().equals(luckPeo.getOpenid())) {
                    islucky = true;
                    // 发送中奖的用户模板信息
                    WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                            .toUser(luckPeo.getOpenid())
                            .templateId(Cont.TEMPLATE_ID)
                            .url(link)
                            .build();
                    templateMessage.addData(new WxMpTemplateData("first", "您在Ai捡耙活抽奖活动中获得了" + luckPeo.getRank() + "等奖", "red"));
                    templateMessage.addData(new WxMpTemplateData("keyword1", "无效字段", "#D0D0D0"));
                    templateMessage.addData(new WxMpTemplateData("keyword2", "无效字段", "#D0D0D0"));
                    templateMessage.addData(new WxMpTemplateData("keyword3", luckPeo.getPrizeName(), "blue"));
                    templateMessage.addData(new WxMpTemplateData("keyword4", MaDateUtil.getCurrentTime(), "blue"));
                    templateMessage.addData(new WxMpTemplateData("remark", "收到通知后，3日内联系客服领取奖品，更多获奖情况，请点击查看", "blue"));
                    sendTemplateMsg(templateMessage);
                    System.out.println("------------ok----------" + luckPeo.getOpenid());
                    break;
                }
            }
            if (islucky) {
                continue;
            }
            // 发送没有中奖的模板信息
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(luckyRecord.getOpenid())
                    .templateId(Cont.TEMPLATE_ID)
                    .url(link)
                    .build();
            templateMessage.addData(new WxMpTemplateData("first", "您在Ai捡耙活抽奖活动中运气不佳，没有中奖", "red"));
            templateMessage.addData(new WxMpTemplateData("keyword1", "无效字段", "#D0D0D0"));
            templateMessage.addData(new WxMpTemplateData("keyword2", "无效字段", "#D0D0D0"));
            templateMessage.addData(new WxMpTemplateData("keyword3", "没有获得奖品", "blue"));
            templateMessage.addData(new WxMpTemplateData("keyword4", MaDateUtil.getCurrentTime(), "blue"));
            templateMessage.addData(new WxMpTemplateData("remark", "下次再接再厉，更多获奖情况，请点击查看", "blue"));
            sendTemplateMsg(templateMessage);
        }
        return null;
    }

    private String sendTemplateMsg(WxMpTemplateMessage templateMessage) {
        WxMpService wxMpService = wechatMpConfig.wxMpService();

        String reStr = null;
        try {
             reStr = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error(e.getError());
//            e.printStackTrace();
        }
        return reStr;
    }

}
