package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.ActivRequestBody;

public interface ActivService {

    /**
     * 新增活动
     * @param activ activ
     * @return ResponMsg
     */
    ResponMsg add(ActivRequestBody activ);

    /**
     * 条件分页查询活动信息
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param conditionType 开奖条件类型：1： 时间限制；2：人数限制
     * @param sponsorName 赞助商名称
     * @return ResponMsg
     */
    ResponMsg conditionPage(int pageNum, int pageSize, int conditionType, String sponsorName);

    /**
     * 禁用启用
     * @param id 赞id
     * @param delState 赞助商状态 {0：禁用；1：启用}
     * @return ResponMsg
     */
    ResponMsg updateDelState(long id, int delState);

    /**
     * 通过id查询活动
     * @param id id
     * @return ResponMsg
     */
    ResponMsg activById(long id);

    /**
     * 更新活动
     * @param activRequestBody activRequestBody
     * @return ResponMsg
     */
    ResponMsg updateActiv(ActivRequestBody activRequestBody);
}
