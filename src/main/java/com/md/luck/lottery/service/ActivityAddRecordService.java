package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;

public interface ActivityAddRecordService {
    /**
     * 新增抢 活动类型参与记录
     * @param openid openid
     * @param activId 活动id
     * @return ResponMsg
     */
    ResponMsg addGrabRecord(String openid, Long activId);

    /**
     * 新增抢助力活动类型参与记录
     * @param openid openid
     * @param activId 活动id
     * @return ResponMsg
     */
    ResponMsg addGrabRecordByRedis(String openid, Long activId);

    /**
     * 查询此活动所有参与人员
     * @param activId 活动id
     * @return ResponMsg
     */
    ResponMsg queryAllCommitGrab(String teamPlayerOpenid, Integer pageNum, Integer pageSize, Long activId);

    /**
     * 通过id查询活动记录
     * @param id id
     * @return ResponMsg
     */
    ResponMsg queryById(Long id);
}
