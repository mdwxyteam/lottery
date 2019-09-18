package com.md.luck.lottery.service;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Sponsor;

public interface SponsorService {
    /**
     * 添加商户
     * @param sponsor 商户名称
     * @param location 地理坐标
     * @param address 地理位置
     * @param detalis 详细信息
     * @param type 商户类型
     * @return ResponMsg<Sponsor>
     */
    ResponMsg<Sponsor> add(String sponsor,  String location, String address, String detalis, long typeId, String type);
}
