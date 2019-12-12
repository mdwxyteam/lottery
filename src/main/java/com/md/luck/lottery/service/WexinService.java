package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface WexinService {
    /**
     * 微信授权获取code组装链接
     * @return ResponMsg
     */
    ResponMsg getCode();

    /**
     * 授权成功
     * @param state
     * @param code
     * @return
     */
    ResponMsg getToken(String state, String code);

    /**
     * 通过openid查询数据
     * @param openid openid
     * @return ResponMsg
     */
    ResponMsg getCustomer(String openid);

    /**
     * 通过openid查询权限
     * @param openid openid
     * @return ResponMsg
     */
    ResponMsg getRoleByOpenid(String openid);
}
