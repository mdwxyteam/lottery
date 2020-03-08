package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.LuckPeo;
import com.md.luck.lottery.common.entity.LuckyRecord;

import java.util.List;

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

    /**
     * 微信服务号发送模板消息(助力活动)
     * @param  acticId 活动id
     * @return ResponMsg
     */
    ResponMsg sendCastTemplateMsg(Long acticId);

    /**
     * 微信服务号发送模板消息（抽奖活动）
     * @param  activId 活动ID
     * @param  records 活动参与者数据
     * @param  luckyPeples 活动中奖数据
     * @return ResponMsg
     */
    ResponMsg sendLuckyTemplateMsg(Long activId, List<LuckyRecord> records, List<LuckPeo> luckyPeples);
}
