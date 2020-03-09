package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.SponsorPrize;
import com.md.luck.lottery.common.util.MaMathUtil;
import com.md.luck.lottery.mapper.SponsorPrizeMapper;
import com.md.luck.lottery.service.SponsorPrizeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SponsorPrizeServiceImpl implements SponsorPrizeService {

    private static final Log log = LogFactory.getLog(SponsorPrizeServiceImpl.class);

    @Autowired
    private SponsorPrizeMapper sponsorPrizeMapper;

    @Override
    public ResponMsg add(Long sponsorId, Long prizeid, int prizeCount) {
        if (ObjectUtil.hasEmpty(sponsorId, prizeid, prizeCount)) {
            return ResponMsg.newFail(null).setMsg("参数异常");
        }
        SponsorPrize sponsorPrize = new SponsorPrize();
        sponsorPrize.setPrizeCount(prizeCount);
        sponsorPrize.setSponid(sponsorId);
        sponsorPrize.setPrizeid(prizeid);
        Long id = MaMathUtil.creatId();
        sponsorPrize.setId(id);
        boolean ie = false;
        try {
            sponsorPrizeMapper.add(sponsorPrize);
        } catch (SqlSessionException e) {
            ie = true;
            log.error(e.getMessage());
        }
        if (ie) {
            return ResponMsg.newFail(null).setMsg("数据库操作异常");
        }
        return ResponMsg.newSuccess(sponsorPrize);
    }
}
