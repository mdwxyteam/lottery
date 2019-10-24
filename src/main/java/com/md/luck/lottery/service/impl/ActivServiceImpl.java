package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.PrizeChild;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.ActivRequestBody;
import com.md.luck.lottery.common.entity.AtivPrize;
import com.md.luck.lottery.common.util.ConUtil;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.mapper.AtivPrizeMapper;
import com.md.luck.lottery.service.ActivService;
import com.md.luck.lottery.service.SchedulService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivServiceImpl implements ActivService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ActivMapper activMapper;
    @Autowired
    private AtivPrizeMapper ativPrizeMapper;
    @Autowired
    private SchedulService schedulService;

    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public ResponMsg add(ActivRequestBody activ) {
        if (ObjectUtil.hasEmpty(activ) || activ.isEmpty()) {
            return ResponMsg.newFail(null).setMsg("参数异常");
        }
        boolean ie = false;
        try {

            long id = activMapper.add(activ);
            for (PrizeChild prizeChild: activ.getPrizeList()) {
                AtivPrize ativPrize = new AtivPrize();
                ativPrize.setPrizeCount(prizeChild.getPrizeCount());
                ativPrize.setPrizeId(prizeChild.getId());
                ativPrize.setAtivId(id);
                ativPrize.setRanking(prizeChild.getRanking());
                ativPrizeMapper.add(ativPrize);
            }
            if (activ.getConditionType() == Cont.ONE) {
                String taskStr = "com.md.luck.lottery.quartz.task.ActivityCountdownTask";
                String con = ConUtil.getCron(activ.getCondition(),"yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("activName", id);
                jsonObject.put("huodong", "--START ACTIV--");
                schedulService.addSchedul(Cont.quartz + id, Cont.quartz + id, Cont.quartz + id,  Cont.quartz + id, taskStr, con, jsonObject);
            }
        } catch (SqlSessionException e) {
            ie = true;
            log.error(e.getMessage());
        }
        if (ie) {
            return ResponMsg.newFail(null).setMsg("数据库操作异常");
        }
        return ResponMsg.newSuccess(activ);
    }

    @Override
    public ResponMsg conditionPage(int pageNum, int pageSize, int conditionType, String sponsorName) {
        PageHelper.startPage(pageNum, pageSize);
        if (StrUtil.hasEmpty(sponsorName)) {
            sponsorName = null;
        }
        List<Activ> activs = activMapper.conditionPage(conditionType, sponsorName);
        PageInfo<Activ> pageInfo = new PageInfo<Activ>(activs);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg updateDelState(long id, int delState) {
        boolean bl = false;
        try {
            int i = activMapper.updateDelState(id, delState);
            if (i == Cont.ZERO) {
                return ResponMsg.newFail(null).setMsg("修改失败！");
            }
        }catch (SqlSessionException e) {
            bl = true;
            log.error(e.getMessage());
        }
        if (bl) {
            return ResponMsg.newFail(null).setMsg("修改失败！");
        }
        return ResponMsg.newSuccess(null);
    }

    @Override
    public ResponMsg activById(long id) {
        return ResponMsg.newSuccess(activMapper.activById(id));
    }

    @Override
    public ResponMsg updateActiv(Activ activ) {
        boolean bl = false;
        if (ObjectUtil.hasEmpty(activ) || activ.getId() == 0l) {
            return ResponMsg.newFail(null).setMsg("缺省参数！");
        }
        try {
            int i = activMapper.updateActiv(activ);
            if (i == Cont.ZERO) {
                return ResponMsg.newFail(null).setMsg("修改失败！");
            }
        }catch (SqlSessionException e) {
            bl = true;
            log.error(e.getMessage());
        }
        if (bl) {
            return ResponMsg.newFail(null).setMsg("修改失败！");
        }
        return ResponMsg.newSuccess(null);
    }
}
