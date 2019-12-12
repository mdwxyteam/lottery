package com.md.luck.lottery.service.impl;

import cn.hutool.crypto.CryptoException;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Customer;
import com.md.luck.lottery.common.entity.JRole;
import com.md.luck.lottery.common.util.MaObjUtil;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WeixinServiceImpl implements WexinService {
    private Log log = LogFactory.getLog(this.getClass());

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
}
