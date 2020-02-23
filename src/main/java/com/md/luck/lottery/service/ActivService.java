package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.ActivRequestBody;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface ActivService {

    /**
     * 新增活动
     *
     * @param activ activ
     * @return ResponMsg
     */
    ResponMsg add(ActivRequestBody activ);

    /**
     * 条件分页查询活动信息
     *
     * @param pageNum       页码
     * @param pageSize      页大小
     * @param conditionType 开奖条件类型：1： 时间限制；2：人数限制
     * @param sponsorName   赞助商名称
     * @return ResponMsg
     */
    ResponMsg conditionPage(int pageNum, int pageSize, int conditionType, String sponsorName);

    /**
     * 禁用启用
     *
     * @param id       赞id
     * @param delState 赞助商状态 {0：禁用；1：启用}
     * @return ResponMsg
     */
    ResponMsg updateDelState(long id, int delState);

    /**
     * 通过id查询活动
     *
     * @param id id
     * @return ResponMsg
     */
    ResponMsg activById(long id);

    /**
     * 更新活动
     *
     * @param activRequestBody activRequestBody
     * @return ResponMsg
     */
    ResponMsg updateActiv(ActivRequestBody activRequestBody);

    /**
     * 查询上轮播的活动信息
     *
     * @return ResponMsg
     */
    ResponMsg queryByCarousel(Integer carousel);

    /**
     * 微信端展示活动列表数据
     *
     * @param activType 活动类型 0:抽奖；1:表示抢
     * @param state     1:未结束；0结束；-1测试
     * @return ResponMsg
     */
    ResponMsg queryWeixinActiv(Integer pageNum, Integer pageSize, Integer activType, Integer state);

    /**
     * 通过活动id查询活动详情信息
     * openid 登录中获取的
     * @param activId activId
     * @param ropenid 分享者的openid
     * @param recordId 分享者的记录的id
     * @return ResponMsg
     */
    ResponMsg queryByActivIdToWeixin(String openid, Integer activType, Integer state, Long activId, String ropenid, Long recordId );

    /**
     * 查询助力活动参与记录
     * @param openid openid
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param activType activType 0:抽奖；1:表示助力
     * @return ResponMsg
     */
    ResponMsg queryRecord(String openid, Integer pageNum, Integer pageSize, Integer activType);

}
