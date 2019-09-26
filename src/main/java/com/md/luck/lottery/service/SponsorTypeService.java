package com.md.luck.lottery.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.SponsorType;

import java.util.List;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: SponsorTypeService
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/8/29 11:09
 */
public interface SponsorTypeService {
    /**
     * 保存商户类型信息
     * @param type
     * @return ResponMsg
     */
    ResponMsg add(String type);

    /**
     * 分页查询所有商户类型信息
     * @param pageNum 页码
     * @param pageSize 每页数据长度
     * @return ResponMsg<List<Page<SponsorType>>>
     */
    ResponMsg<PageInfo<SponsorType>> page(int pageNum, int pageSize);

    /**
     *  商户类型状态改变
     * @param typeId
     * @param  isStatus
     * @return
     */
    ResponMsg status(Long typeId, int isStatus);
}
