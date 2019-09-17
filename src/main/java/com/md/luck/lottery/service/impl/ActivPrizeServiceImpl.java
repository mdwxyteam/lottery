package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.AtivPrize;
import com.md.luck.lottery.service.ActivPrizeMapper;
import com.md.luck.lottery.service.ActivPrizeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ActivPrizeServiceImpl implements ActivPrizeService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ActivPrizeMapper activPrizeMapper;
    @Override
    public ResponMsg add(Long ativId, Long prizeId, int prizeCount) {
        if (ObjectUtil.hasEmpty(ativId, prizeId, prizeCount)) {
            return ResponMsg.newFail(null).setMsg("参数异常");
        }
        AtivPrize ativPrize = new AtivPrize();
        ativPrize.setAtivId(ativId);
        ativPrize.setPrizeId(prizeId);
        ativPrize.setPrizeCount(prizeCount);
        boolean isbc = false;
        try {
            activPrizeMapper.add(ativPrize);
        } catch (SqlSessionException e) {
            isbc = true;
            log.error(e.getMessage());
        }
        return ResponMsg.newSuccess(ativPrize);
    }
}
