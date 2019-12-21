package com.md.luck.lottery.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.ActivityAddRecord;
import com.md.luck.lottery.common.entity.CastCulp;
import com.md.luck.lottery.common.entity.Customer;
import com.md.luck.lottery.common.entity.Sponsor;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.mapper.ActivityAddRecordMapper;
import com.md.luck.lottery.mapper.CastCulpMapper;
import com.md.luck.lottery.mapper.CustomerMapper;
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
            int countNum = activMapper.queryCountNum(activId);
            countNum ++;
//          todo 如果结束，就出结果  if (countNum == )
            activMapper.updateCountNum(countNum, activId);
            responMsg = ResponMsg.newSuccess(activityAddRecord);
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
}
