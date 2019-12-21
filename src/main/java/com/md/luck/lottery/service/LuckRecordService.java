package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.WeixnActiv;

public interface LuckRecordService {
    /**
     * 通过活动id和用户唯一标识查询抽奖记录
     * @param openid openid
     * @param activId 活动id
     * @return ResponMsg
     */
    ResponMsg queryByActivIdAndOpenid(String openid, Long activId);

    /**
     * 参与抽奖活动
     * @param openid 用户唯一标识
     * @param activId 活动id
     * @return ResponMsg
     */
    ResponMsg commitActiv(String openid, Long activId);

    /**
     * 分页查询参与抽奖名单
     * @param openId openId
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param activId 活动id
     * @return ResponMsg
     */
    ResponMsg queryAllCommitActiv(String openId, Integer pageNum,Integer pageSize,Long activId);
}
