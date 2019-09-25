package com.md.luck.lottery.service;

import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Prize;

public interface PrizeService {
    /**
     * 新增奖品
     * @param prizeDescription 奖品描述
     * @param iconUrl 奖品图片地址
     * @param prizeCount 奖品数量
     * @return ResponMsg<Prize>
     */
    ResponMsg<Prize> add(String prizeDescription, String iconUrl, int prizeCount);

    /**
     * 更新奖品
     * @param prizeId 奖品Id
     * @param prizeDescription 奖品描述
     * @param iconUrl 奖品图片地址
     * @param prizeCount 奖品数量
     * @return ResponMsg<Prize>
     */
    ResponMsg<Prize> edit(Long prizeId, String prizeDescription, String iconUrl, int prizeCount);


    /**
     * 分页查询所有奖品信息
     * @param pageNum 页码
     * @param pageSize 每页数据长度
     * @return ResponMsg<List<Page<SponsorType>>>
     */
    ResponMsg<PageInfo<Prize>> page(int pageNum, int pageSize);

    /**
     * 状态删除奖品Id
     * @param prizeId
     * @return
     */
    ResponMsg<Prize> del(Long prizeId);
}
