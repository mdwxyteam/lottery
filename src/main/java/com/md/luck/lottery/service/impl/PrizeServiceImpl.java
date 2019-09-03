package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Prize;
import com.md.luck.lottery.mapper.PrizeMapper;
import com.md.luck.lottery.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrizeServiceImpl implements PrizeService {
    @Autowired
    private PrizeMapper prizeMapper;

    @Override
    public ResponMsg<Prize> add(String prizeDescription, String iconUrl, int prizeCount) {
        if (StrUtil.hasEmpty(prizeDescription, iconUrl)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Prize prize = new Prize();
        prize.setIconUrl(iconUrl);
        prize.setPrizeDescription(prizeDescription);
        prize.setPrizeCount(prizeCount);
        int i = prizeMapper.add(prize);
        if (i == Cont.ZERO) {
            return ResponMsg.newFail(null).setMsg("新增失败！");
        }
        return ResponMsg.newSuccess(null);
    }
}
