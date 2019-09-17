package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Sponsor;
import com.md.luck.lottery.mapper.SponsorMapper;
import com.md.luck.lottery.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SponsorServiceImpl implements SponsorService {

    @Autowired
    private SponsorMapper sponsorMapper;

    @Override
    public ResponMsg<Sponsor> add(String sponsor, String position, String detalis, long typeId, String type) {
        if (StrUtil.hasEmpty(sponsor, position, detalis)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Sponsor sponsorObj = new Sponsor();
        sponsorObj.setType(type);
        sponsorObj.setTypeId(typeId);
        sponsorObj.setDetalis(detalis);
        sponsorObj.setPosition(position);
        sponsorObj.setSponsor(sponsor);
        int i = sponsorMapper.add(sponsorObj);
        if (i == Cont.ZERO) {
            return ResponMsg.newFail(null).setMsg("添加是失败！");
        }
        return ResponMsg.newSuccess(sponsorObj);
    }
}
