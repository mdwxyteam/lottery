package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.util.MaDateUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.*;
import com.md.luck.lottery.service.LuckRecordService;
import com.md.luck.lottery.service.WexinService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LuckRecordServiceImpl implements LuckRecordService {
    private static final Log log = LogFactory.getLog(LuckRecordServiceImpl.class);
    @Autowired
    WexinService weixinService;
    @Autowired
    LuckRecordMapper luckRecordMapper;
    @Autowired
    ActivMapper activMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AtivPrizeMapper ativPrizeMapper;
    @Autowired
    LuckProMapper luckProMapper;

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
                String creatTime = MaDateUtil.getCurrentTime();
                luckyRecord1.setAddTime(creatTime);
                luckyRecord1.setOpenid(customer.getOpenid());
                luckRecordMapper.insert(luckyRecord1);
                int newPopularity = activ.getPopularity() + 1;
                int countNum = activ.getCountNum();
                countNum++;
                activMapper.updatePopularityAndCountNum(newPopularity, countNum, activId);
                if (countNum == Integer.parseInt(activ.getCondition())) {
                    //活动结束，更新state
                    activMapper.updateState(Cont.ZERO, activId);
                    // 抽奖   1、查出所有抽奖人员id;2、随机抽取奖品数量人数，即为奖品或得者；3、更新信息
                    List<LuckyRecord> records = luckRecordMapper.queryByActivId(activId);
                    // 中奖人员容器
                    List<LuckPeo> luckyPeples = new ArrayList<>();
                    List<LuckyRecord> recordList = new ArrayList<>(records);
                    List<AtivPrize> ativPrizes = ativPrizeMapper.queryByAtivId(activId);
                    // 奖品对应的或得者
                    for (AtivPrize ativPrize: ativPrizes) {
                        int ran = RandomUtil.randomInt(1, records.size());
                        LuckyRecord luckyRecordLucky = records.get(ran);
                        records.remove(luckyRecordLucky);
                        LuckPeo luckPeo = new LuckPeo();
                        luckPeo.setIcon(luckyRecordLucky.getIcon());
                        luckPeo.setNickName(luckyRecordLucky.getNickName());
                        luckPeo.setOpenid(luckyRecordLucky.getOpenid());
                        luckPeo.setRank(Integer.parseInt(ativPrize.getRanking()));
                        luckPeo.setRecordId(luckyRecordLucky.getId());
                        luckPeo.setPrizeName(ativPrize.getPrizeDescription());
                        luckPeo.setActivId(activId);
                        luckProMapper.insert(luckPeo);
                        luckRecordMapper.updateLuck(Cont.ONE, luckyRecordLucky.getId());
                        luckyPeples.add(luckPeo);
                    }
                    // 发送模板消息通知参与抽奖活动的用户
                    weixinService.sendLuckyTemplateMsg(activId, recordList, luckyPeples);
                } else {

//                    activMapper.updatePopularity(newPopularity, activId);
                }

                responMsg = ResponMsg.newSuccess(newPopularity);
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
