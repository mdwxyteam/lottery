package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.AtivPrize;
import com.md.luck.lottery.mapper.AtivPrizeMapper;
import com.md.luck.lottery.service.ActivPrizeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivPrizeServiceImpl implements ActivPrizeService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private AtivPrizeMapper ativPrizeMapper;

    @Override
    public ResponMsg add(Long ativId, Long prizeId, int prizeCount, String icouUrl) {
        if (ObjectUtil.hasEmpty(ativId, prizeId, prizeCount, icouUrl)) {
            return ResponMsg.newFail(null).setMsg("参数异常");
        }
        AtivPrize ativPrize = new AtivPrize();
        ativPrize.setAtivId(ativId);
        ativPrize.setPrizeId(prizeId);
        ativPrize.setPrizeCount(prizeCount);
        ativPrize.setIconUrl(icouUrl);
        boolean isbc = false;
        try {
            ativPrizeMapper.add(ativPrize);
        } catch (SqlSessionException e) {
            isbc = true;
            log.error(e.getMessage());
        }
        if (isbc) {
            return ResponMsg.newFail(null).setMsg("操作异常！");
        }
        return ResponMsg.newSuccess(ativPrize);
    }

    @Override
    public ResponMsg queryActivPrize(long ativId) {
        if (ativId == 0l) {
            return ResponMsg.newFail("缺省必要参数！");
        }
        boolean isbc = false;
        List<AtivPrize> ativPrizeList = null;
        try {
            ativPrizeList = ativPrizeMapper.queryByAtivId(ativId);
        } catch (SqlSessionException e) {
            isbc = true;
            log.error(e.getMessage());
        }
        if (isbc || ObjectUtil.hasEmpty(ativPrizeList)) {
            return ResponMsg.newFail(null).setMsg("操作失败！");
        }
        return ResponMsg.newSuccess(ativPrizeList);
    }
}
