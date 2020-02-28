package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.JoinAttributes;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.util.MaDateUtil;
import com.md.luck.lottery.common.util.MaMathUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.common.util.NumberUtil;
import com.md.luck.lottery.mapper.*;
import com.md.luck.lottery.service.ActivityAddRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ActivityAddRecordServiceImpl implements ActivityAddRecordService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisServiceImpl redisService;
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
            activityAddRecord.setRank(Cont.ZEROL);
            activityAddRecord.setTeamMateCount(0);
            activityAddRecordMapper.add(activityAddRecord);
            // 更新活动参与人员数量
            Activ activ = activMapper.activById(activId);
           int popularity =  activ.getPopularity();
            popularity ++;
            int countNum = activ.getCountNum();
            countNum++;
//          todo 如果结束，就出结果  if (countNum == )

            // todo 更新参与人数

            Map<String, Object> map = new HashMap<>();
            if (popularity == Integer.parseInt(activ.getCondition())) {
                activMapper.updatePopularityAndCounNumAndState(popularity, Cont.ZERO, countNum, activId);
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
                activMapper.updatePopularityAndCountNum(popularity, countNum, activId);
            }
            map.put("popularity", popularity);
            map.put("countNum", countNum);
            map.put("activityAddRecord", activityAddRecord);
            responMsg = ResponMsg.newSuccess(map);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg addGrabRecordByRedis(String openid, Long activId) {
        if (MaObjUtil.hasEmpty(openid, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        // 活动结束判断
        String activityEndKey = Cont.ACTIVITY_END_PRE + activId;
        String isActivityEnd = (String) redisTemplate.opsForHash().get(activityEndKey, activityEndKey);
        if (!MaObjUtil.isEmpty(isActivityEnd)) {
            return ResponMsg.newFail(null).setMsg("活动已结束不能再助力");
        }
        ResponMsg responMsg = null;
        // 通过活动id和当前用户openid判断是否参与过当前活动
        // 1、查询活动所有参与者判断是否有参与；2、判断用户是否助力过这个活动
        String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
        JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
        boolean isJoinActivity = redisTemplate.opsForHash().hasKey(joinKey, joinAttributes.getOpenid());
        if (isJoinActivity) {
            return ResponMsg.newFail(null).setMsg("已经参与过此活动");
        }
        String jKey = Cont.ACTIV_RESDIS_J_PRE + activId;
        String jFeild = Cont.OPENID + openid;
        boolean isGrab = redisTemplate.opsForHash().hasKey(jKey, jFeild);
        if (isGrab) {
            return ResponMsg.newFail(null).setMsg("已经助力过此活动");
        }
        //判断此账号是否存在
        Customer customer = customerMapper.queryByOpenid(openid);
        if (MaObjUtil.isEmpty(customer)) {
            return ResponMsg.newFail(null).setMsg("用户信息异常");
        }
        // 添加到排行榜
        //修改排名  //修改排行榜
        String rKey = Cont.RANK_PRE + activId;
        Double dscore = null;
        try {
            dscore = NumberUtil.strTime2Double("0", "1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForZSet().incrementScore(rKey, openid, dscore);
        //获取排名
        Long rank = redisTemplate.opsForZSet().reverseRank(rKey, openid) + Cont.ONE;

        Long recordId = MaMathUtil.creatId();
        ActivityAddRecord activityAddRecord = new ActivityAddRecord();
        activityAddRecord.setActivId(activId);
        activityAddRecord.setCulp(Cont.ZERO);
        activityAddRecord.setIcon(customer.getIcon());
        activityAddRecord.setNickName(customer.getNickName());
        activityAddRecord.setOpenid(customer.getOpenid());
        activityAddRecord.setRank(rank);
        activityAddRecord.setTeamMateCount(Cont.ZERO);
        String creatTime = MaDateUtil.getCurrentTime();
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getId(), recordId);
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getActivId(), activId);
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getCulp(), Cont.ZERO);
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getIcon(), customer.getIcon());
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getNickName(), customer.getNickName());
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getOpenid(), customer.getOpenid());
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getRank(), rank);
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getTeamMateCount(), Cont.ZERO);
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getAddTime(), creatTime);

        String valueTag = activId + "_" + creatTime;
        // 用户参与活动标志用于查询参与记录
        redisService.addRecordTag(customer.getOpenid(), valueTag);

        // 更新活动参与人员数量和人气
        Activ activ = activMapper.activById(activId);
        int popularity =  activ.getPopularity();
        popularity ++;
        int countNum = activ.getCountNum();
        countNum++;
        activMapper.updatePopularityAndCountNum(popularity, countNum, activId);

        //判断活动是否结束
        //活动是否结束
        if (popularity == Integer.parseInt(activ.getCondition())) {
            // 结束
            // 修改活动状态为结束
            activMapper.updatePopularityAndState(popularity, Cont.ZERO, activId);
            //活动所有奖品
            List<AtivPrize> ativPrizes = ativPrizeMapper.queryByAtivId(activId);
            //活动前五参与者
            Set<String> preFiveAdder = redisTemplate.opsForZSet().reverseRange(rKey, Cont.ZERO, Cont.FIVE);
            for (AtivPrize ativPrize: ativPrizes) {
                int i = 0;
                String recordOpenid = null;
                for (String recordop : preFiveAdder) {
                    i++;
                    int prizeRank = Integer.valueOf(ativPrize.getRanking());
                    if (prizeRank == i) {
                        recordOpenid = recordop;
                        break;
                    }
                }
                JoinAttributes joinAttributesRecord = JoinAttributes.getInstance(recordOpenid);
                LuckPeo luckPeo = new LuckPeo();
                Long lpid = MaMathUtil.creatId();
                String icon = (String) redisTemplate.opsForHash().get(joinKey, joinAttributesRecord.getIcon());
                luckPeo.setIcon(icon);
                String nickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributesRecord.getNickName());
                luckPeo.setNickName(nickName);
                String luckyOpenid = (String) redisTemplate.opsForHash().get(joinKey, joinAttributesRecord.getOpenid());
                luckPeo.setOpenid(luckyOpenid);
                luckPeo.setRank(Integer.parseInt(ativPrize.getRanking()));
                Long luckyRecordId = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributesRecord.getId());
                luckPeo.setRecordId(luckyRecordId);
                luckPeo.setPrizeName(ativPrize.getPrizeDescription());
                luckPeo.setActivId(activId);
                luckPeo.setId(lpid);
                //保存奖品获得者记录
                String rlKey = Cont.RANL_LUCKY_PRE + activId;
                String fieldLp = luckyOpenid;
                redisTemplate.opsForHash().put(rlKey, fieldLp, luckPeo);
                //redis添加活动结束标志
                String aendKey = Cont.ACTIVITY_END_PRE + activId;
                redisTemplate.opsForHash().put(aendKey, aendKey, Cont.END);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("popularity", popularity);
        map.put("countNum", countNum);
        map.put("activityAddRecord", activityAddRecord);
        responMsg = ResponMsg.newSuccess(map);
        return responMsg;
    }

    @Override
    public ResponMsg queryAllCommitGrab(String teamPlayerOpenid, Integer pageNum, Integer pageSize, Long activId) {
        if (MaObjUtil.hasEmpty(pageNum, pageSize, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        pageSize = pageSize > Cont.MAX_PAGE_SIZE ? Cont.MAX_PAGE_SIZE: pageSize;
        ResponMsg responMsg = null;
        PageInfo<ActivityAddRecord> pageInfo = null;
        List<ActivityAddRecord> activityAddRecords = null;
        try {
            pageInfo = redisService.getActivityAddRecordByRank(teamPlayerOpenid, activId, pageNum, pageSize);
            // 尝试从redis中获取参与记录，没有则从数据库中获取
            if (!MaObjUtil.isEmpty(pageInfo)) {
                activityAddRecords = pageInfo.getList();
            } else {
                PageHelper.startPage(pageNum, pageSize);
                activityAddRecords = activityAddRecordMapper.queryByActivId(activId);
                pageInfo = new PageInfo<ActivityAddRecord>(activityAddRecords);
            }
            // 标志当前用户是否参与（助力或参与）此活动
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
                // 判断是否助力过此活动
                String jKey = Cont.ACTIV_RESDIS_J_PRE + activId;
                String jFeild = Cont.OPENID + teamPlayerOpenid;
                boolean isGrab = redisTemplate.opsForHash().hasKey(jKey, jFeild);
                // 现从redis中尝试判断是否助力过，没有则再从数据库判断
                if (isGrab) {
                    // 助力过
                    resMap.put("castBool", true);
                } else {
                    // 从数据库中判断
                    CastCulp castCulp = castCulpMapper.queryByOpenidAndActivId(teamPlayerOpenid, activId);
                    if (!MaObjUtil.isEmpty(castCulp)) {
                        resMap.put("castBool", true);
                    } else {
                        resMap.put("castBool", false);
                    }
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
