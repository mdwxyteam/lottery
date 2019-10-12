package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Sponsor;
import com.md.luck.lottery.mapper.SponsorMapper;
import com.md.luck.lottery.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorServiceImpl implements SponsorService {

    @Autowired
    private SponsorMapper sponsorMapper;

    @Override
    public ResponMsg<List<Sponsor>> byType(Long typeId) {
        return ResponMsg.newSuccess(sponsorMapper.byType(typeId));
    }

    @Override
    public ResponMsg<List<Sponsor>> page(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Sponsor> sponsorTypeList = sponsorMapper.all();
        PageInfo<Sponsor> pageInfo = new PageInfo<Sponsor>(sponsorTypeList);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg<Sponsor> add(String sponsor, String location, String address, String detalis, long typeId, String type) {
        if (StrUtil.hasEmpty(sponsor, location, detalis)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Sponsor sponsorObj = new Sponsor();
        sponsorObj.setType(type);
        sponsorObj.setTypeId(typeId);
        sponsorObj.setDetalis(detalis);
        sponsorObj.setLocation(location);
        sponsorObj.setSponsor(sponsor);
        sponsorObj.setAddress(address);
        int i = sponsorMapper.add(sponsorObj);
        if (i == Cont.ZERO) {
            return ResponMsg.newFail(null).setMsg("添加是失败！");
        }
        return ResponMsg.newSuccess(sponsorObj);
    }
}
