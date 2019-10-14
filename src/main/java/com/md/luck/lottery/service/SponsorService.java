package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Sponsor;

import java.util.List;

public interface SponsorService {
    /**
     * 通过id获取赞助商详情
     * @param id id
     * @return ResponMsg<Sponsor>
     */
    ResponMsg<Sponsor> detailById(Long id);
    /**
     * 通过类型查询赞助商
     *
     * @param typeId 类型id
     * @return List<Sponsor>
     */
    ResponMsg<List<Sponsor>> byType(Long typeId);

    /**
     * 分页查询赞助商
     *
     * @param pageNum  页码
     * @param pageSize 每页数据长度
     * @return ResponMsg<List < Sponsor>>
     */
    ResponMsg<List<Sponsor>> page(int pageNum, int pageSize);

    /**
     * 添加商户
     *
     * @param sponsor  商户名称
     * @param location 地理坐标
     * @param address  地理位置
     * @param detalis  详细信息
     * @param type     商户类型
     * @return ResponMsg<Sponsor>
     */
    ResponMsg<Sponsor> add(String sponsor, String location, String address, String detalis, long typeId, String type);

    /**
     * 修改商户
     *
     * @param id  商户id
     * @param sponsor  商户名称
     * @param location 地理坐标
     * @param address  地理位置
     * @param detalis  详细信息
     * @param type     商户类型
     * @return ResponMsg<Sponsor>
     */
    ResponMsg<Sponsor> update(long id, String sponsor, String location, String address, String detalis, long typeId, String type);
}
