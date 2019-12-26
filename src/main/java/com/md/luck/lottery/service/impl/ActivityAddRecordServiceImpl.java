package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.*;
import com.md.luck.lottery.service.ActivityAddRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityAddRecordServiceImpl implements ActivityAddRecordService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private ActivityAddRecordMapper activityAddRecordMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    CastCulpMapper castCulpMapper;
    @Autowired
    ActivMapper activMapper;
    @Autowired
    AtivPrizeMapper ativPrizeMapper;
    @Autowired
    LuckProMapper luckProMapper;

    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public ResponMsg addGrabRecord(String openid, Long activId) {
        if (MaObjUtil.hasEmpty(openid, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            ActivityAddRecord added = activityAddRecordMapper.queryByOpenidAndId(openid, activId);
            if (!MaObjUtil.isEmpty(added)) {
                return ResponMsg.newFail(null).setMsg("用户已经参与，不能再次参与此次活动");
            }
            Customer customer = customerMapper.queryByOpenid(openid);
            if (MaObjUtil.isEmpty(customer)) {
                return ResponMsg.newFail(null).setMsg("用户信息异常");
            }
            ActivityAddRecord activityAddRecord = new ActivityAddRecord();
            activityAddRecord.setActivId(activId);
            activityAddRecord.setCulp(Cont.ZERO);
            activityAddRecord.setIcon(customer.getIcon());
            activityAddRecord.setNickName(customer.getNickName());
            activityAddRecord.setOpenid(customer.getOpenid());
            activityAddRecord.setRank(Cont.ZERO);
            activityAddRecord.setTeamMateCount(0);
            activityAddRecordMapper.add(activityAddRecord);
            // 更新活动参与人员数量
            Activ activ = activMapper.activById(activId);
           int popularity =  activ.getPopularity();
            popularity ++;
//          todo 如果结束，就出结果  if (countNum == )

            Map<String, Object> map = new HashMap<>();
            if (popularity == Integer.parseInt(activ.getCondition())) {
                activMapper.updatePopularityAndState(popularity, Cont.ZERO, activId);
                List<ActivityAddRecord> activityAddRecordList = activityAddRecordMapper.queryByActivId(activId);
                List<AtivPrize> ativPrizes = ativPrizeMapper.queryByAtivId(activId);
                // 奖品对应的或得者
                for (AtivPrize ativPrize: ativPrizes) {
                    int ran = RandomUtil.randomInt(1, activityAddRecordList.size());
                    ActivityAddRecord addRecord = activityAddRecordList.get(ran);
                    activityAddRecordList.remove(addRecord);
                    LuckPeo luckPeo = new LuckPeo();
                    luckPeo.setIcon(addRecord.getIcon());
                    luckPeo.setNickName(addRecord.getNickName());
                    luckPeo.setOpenid(addRecord.getOpenid());
                    luckPeo.setRank(Integer.parseInt(ativPrize.getRanking()));
                    luckPeo.setRecordId(addRecord.getId());
                    luckPeo.setPrizeName(ativPrize.getPrizeDescription());
                    luckPeo.setActivId(activId);
                    luckProMapper.insert(luckPeo);
//                    luckRecordMapper.updateLuck(Cont.ZERO, luckyRecordLucky.getId());
                }
            } else {
                activMapper.updatePopularity(popularity, activId);
            }
            map.put("popularity", popularity);
            map.put("activityAddRecord", activityAddRecord);
            responMsg = ResponMsg.newSuccess(map);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryAllCommitGrab(String teamPlayerOpenid, Integer pageNum, Integer pageSize, Long activId) {
        if (MaObjUtil.hasEmpty(pageNum, pageSize, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        pageSize = pageSize > Cont.MAX_PAGE_SIZE ? Cont.MAX_PAGE_SIZE: pageSize;
        ResponMsg responMsg = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<ActivityAddRecord> activityAddRecords = activityAddRecordMapper.queryByActivId(activId);
            PageInfo<ActivityAddRecord> pageInfo = new PageInfo<ActivityAddRecord>(activityAddRecords);
            boolean isa = false;
            Map<String, Object> resMap = new HashMap<>();
            for (ActivityAddRecord activityAddRecord: activityAddRecords) {
                if (activityAddRecord.getOpenid().equals(teamPlayerOpenid)) {
                    isa = true;
                    break;
                }
            }
            if (isa) {
                resMap.put("castBool", true);
            } else {
                CastCulp castCulp = castCulpMapper.queryByOpenidAndActivId(teamPlayerOpenid, activId);
                if (!MaObjUtil.isEmpty(castCulp)) {
                    resMap.put("castBool", true);
                } else {
                    resMap.put("castBool", false);
                }
            }
            resMap.put("pageInfo", pageInfo);
            responMsg = ResponMsg.newSuccess(resMap);
        }catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryById(Long id) {
        if (MaObjUtil.isEmpty(id)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            ActivityAddRecord activityAddRecord = activityAddRecordMapper.queryById(id);
            responMsg = ResponMsg.newSuccess(activityAddRecord);
        }catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
