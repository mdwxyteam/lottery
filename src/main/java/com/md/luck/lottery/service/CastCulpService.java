package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.request.HelpGrab;

public interface CastCulpService {

    /**
     * 助力
     * @param teamPlayerOpenid  助力用户openid
     * @param helpGrab 包含活动id，被助力用户的openid
     * @return ResponMsg
     */
    ResponMsg culp(String teamPlayerOpenid, HelpGrab helpGrab);
    /**
     * 助力 (redis版)
     * @param teamPlayerOpenid  助力用户openid
     * @param helpGrab 包含活动id，被助力用户的openid
     * @return ResponMsg
     */
    ResponMsg culpByRedis(String teamPlayerOpenid, HelpGrab helpGrab);

    /**
     * 分页查询助力记录
     * @param activId 活动id
     * @param teamPlayerOpenid 当前用户openid
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param recordId 参与记录id
     * @return ResponMsg
     */
    ResponMsg queryPage(Long activId, String teamPlayerOpenid, Integer pageNum, Integer pageSize, Long recordId);
}
