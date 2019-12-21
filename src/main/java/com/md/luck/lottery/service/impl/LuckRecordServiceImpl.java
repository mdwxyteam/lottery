package com.md.luck.lottery.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.Customer;
import com.md.luck.lottery.common.entity.LuckyRecord;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.mapper.CustomerMapper;
import com.md.luck.lottery.mapper.LuckRecordMapper;
import com.md.luck.lottery.service.LuckRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LuckRecordServiceImpl implements LuckRecordService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    LuckRecordMapper luckRecordMapper;
    @Autowired
    ActivMapper activMapper;
    @Autowired
    CustomerMapper customerMapper;

    @Override
    public ResponMsg queryByActivIdAndOpenid(String openid, Long activId) {
        return null;
    }

    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public ResponMsg commitActiv(String openid, Long activId) {
        if (MaObjUtil.hasEmpty(openid, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            LuckyRecord luckyRecord = luckRecordMapper.queryByActivIdAndOpenid(openid, activId);
            // 是否参与过
            if (MaObjUtil.isEmpty(luckyRecord)) {
                Activ activ = activMapper.activById(activId);
                if (activ.getState() == Cont.ZERO) {
                    // 活动结束
                    return ResponMsg.newFail(null).setMsg("活动已结束");
                }
                Customer customer = customerMapper.queryByOpenid(openid);
                LuckyRecord luckyRecord1 = new LuckyRecord();
                luckyRecord1.setActivId(activId);
                luckyRecord1.setIcon(customer.getIcon());
                luckyRecord1.setLuck(0);
                luckyRecord1.setNickName(customer.getNickName());
                luckyRecord1.setOpenid(customer.getOpenid());
                luckRecordMapper.insert(luckyRecord1);
                int newPopularity = activ.getPopularity() + 1;
                if (newPopularity == Integer.parseInt(activ.getCondition())) {
                    //活动结束，更新state
                    activMapper.updatePopularityAndState(newPopularity, Cont.ZERO, activId);
                    // 抽奖   1、查出所有抽奖人员id;2、随机抽取奖品数量人数，即为奖品或得者；3、更新信息
                    // todo 抽奖
                } else {
                    activMapper.updatePopularity(newPopularity, activId);
                }

                responMsg = ResponMsg.newSuccess(null);
            }
        } catch (
                SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryAllCommitActiv(String openId, Integer pageNum, Integer pageSize, Long activId) {
        if (MaObjUtil.hasEmpty(pageNum, pageSize, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        pageSize = pageSize > Cont.MAX_PAGE_SIZE + 200 ? Cont.MAX_PAGE_SIZE + 200: pageSize;
        ResponMsg responMsg = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<LuckyRecord> luckyRecords = luckRecordMapper.queryIconByActivId(activId);
            PageInfo<LuckyRecord> pageInfo = new PageInfo<LuckyRecord>(luckyRecords);
            responMsg = ResponMsg.newSuccess(pageInfo);
        }catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
