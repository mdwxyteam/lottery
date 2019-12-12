package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface CustomerService {
    /**
     * 通过openid获取用户信息
     * @param openid openid
     * @return ResponMsg
     */
    ResponMsg getUserInfo(String openid);
}
