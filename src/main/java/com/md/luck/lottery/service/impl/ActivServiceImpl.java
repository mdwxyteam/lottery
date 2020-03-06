package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.JoinAttributes;
import com.md.luck.lottery.common.PrizeChild;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.entity.respons.WeixinActivRecord;
import com.md.luck.lottery.common.util.ConUtil;
import com.md.luck.lottery.common.util.MaMathUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.*;
import com.md.luck.lottery.service.ActivService;
import com.md.luck.lottery.service.SchedulService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivServiceImpl implements ActivService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private ActivMapper activMapper;
    @Autowired
    private AtivPrizeMapper ativPrizeMapper;
    @Autowired
    private SchedulService schedulService;
    @Autowired
    private ActivityAddRecordMapper activityAddRecordMapper;
    @Autowired
    private CastCulpMapper castCulpMapper;
    @Autowired
    private LuckRecordMapper luckRecordMapper;

    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public ResponMsg add(ActivRequestBody activ) {
        if (ObjectUtil.hasEmpty(activ) || activ.isEmpty()) {
            return ResponMsg.newFail(null).setMsg("参数异常");
        }
        boolean ie = false;
        try {
            activ.setState(1);
            activ.setDelState(1);
            Long aid = MaMathUtil.creatId();
            activ.setId(aid);
            activMapper.add(activ);
            long id = activ.getId();
            for (AtivPrize prizeChild : activ.getPrizeList()) {
                AtivPrize ativPrize = new AtivPrize();
                ativPrize.setPrizeCount(prizeChild.getPrizeCount());
                ativPrize.setPrizeId(prizeChild.getId());
                ativPrize.setAtivId(id);
                ativPrize.setRanking(prizeChild.getRanking());
                ativPrize.setIconUrl(prizeChild.getIconUrl());
                ativPrize.setPrizeDescription(prizeChild.getPrizeDescription());
                ativPrizeMapper.add(ativPrize);
            }
            if (activ.getConditionType() == Cont.ONE) {
                String taskStr = "com.md.luck.lottery.quartz.task.ActivityCountdownTask";
                String con = ConUtil.getCron(activ.getCondition(), "yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("activName", id);
                jsonObject.put("huodong", "--START ACTIV--");
                schedulService.addSchedul(Cont.quartz + id, Cont.quartz + id, Cont.quartz + id, Cont.quartz + id, taskStr, con, jsonObject);
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
        } catch (SqlSessionException e) {
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

    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public ResponMsg updateActiv(ActivRequestBody activ) {
        boolean bl = false;
        if (ObjectUtil.hasEmpty(activ) || activ.getId() == 0l) {
            return ResponMsg.newFail(null).setMsg("缺省参数！");
        }
        try {
            activ.setReleaseTime(null);
            int i = 0;
            if (activ.hasNotEmpty()) {
                i = activMapper.updateActiv(activ);
            }
            if (activ.isPrizeBool()) {
                List<AtivPrize> prizeChildren = new ArrayList<>();
                if (activ.getPrizeList() != null) {
                    for (AtivPrize prizeChild : activ.getPrizeList()) {

                        // 没有id的表示新的奖品
                        if (ObjectUtil.hasEmpty(prizeChild.getAtivId())) {
                            AtivPrize ativPrize = new AtivPrize();
                            ativPrize.setPrizeCount(prizeChild.getPrizeCount());
                            ativPrize.setPrizeId(prizeChild.getId());
                            ativPrize.setAtivId(activ.getId());
                            ativPrize.setRanking(prizeChild.getRanking());
                            ativPrize.setIconUrl(prizeChild.getIconUrl());
                            ativPrize.setPrizeDescription(prizeChild.getPrizeDescription());
                            ativPrizeMapper.add(ativPrize);
                            continue;
                        }
                        prizeChildren.add(prizeChild);
                    }
                }


                List<AtivPrize> ativPrizes = ativPrizeMapper.queryByAtivId(activ.getId());
                // 对比新的奖品与以前的奖品
                for (AtivPrize ativPrize : ativPrizes) {
                    boolean as = false;
                    for (AtivPrize prizeChild : prizeChildren) {
                        if (ativPrize.getId() == prizeChild.getId()) {
                            as = true;
                            continue;
                            //判断内容，跟新
                        }
                    }
                    if (!as) {
                        //删除
                        ativPrizeMapper.delAtivPrize(ativPrize.getId());
                    }
                }
            }

//            }
//            return ResponMsg.newFail(null).setMsg("修改失败！");
        } catch (SqlSessionException e) {
            bl = true;
            log.error(e.getMessage());
        }
        if (bl) {
            return ResponMsg.newFail(null).setMsg("修改失败！");
        }
        return ResponMsg.newSuccess(null);
    }

    @Override
    public ResponMsg queryByCarousel(Integer carousel) {
        if (ObjectUtil.hasEmpty(carousel)) {
            return ResponMsg.newFail(null).setMsg("缺省参数!");
        }
        ResponMsg responMsg = null;
        try {
            List<Activ> activs = activMapper.queryByCarousel(carousel);
            responMsg = ResponMsg.newSuccess(activs);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败!");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryWeixinActiv(Integer pageNum, Integer pageSize, Integer activType, Integer state) {
        if (MaObjUtil.hasEmpty(activType, state)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        pageSize = pageSize > Cont.MAX_PAGE_SIZE ? Cont.MAX_PAGE_SIZE : pageSize;
        ResponMsg responMsg = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<WeixnActiv> activs = activMapper.queryWeixinActiv(activType, state);
            PageInfo<WeixnActiv> pageInfo = new PageInfo<WeixnActiv>(activs);
            responMsg = ResponMsg.newSuccess(pageInfo);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryByActivIdToWeixin(String openid, Integer activType, Integer state, Long activId, String ropenid, Long recordId) {
        if (MaObjUtil.hasEmpty(openid, activType, state, activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
//            Long startTime = System.currentTimeMillis();
            WeixinActivChildChild weixinActivChildChild = activMapper.queryWeixinActivByIdAndActivType(activType, activId);
//            Long endTime = System.currentTimeMillis();
//            System.out.println(endTime - startTime);
            if (MaObjUtil.isEmpty(weixinActivChildChild)) {
                return ResponMsg.newFail(null).setMsg("没有数据");
            }
            ActivityAddRecord activityAddRecord = null;
            LuckyRecord luckyRecord = null;
            // 否参与过活动
            boolean isJoinActivity = false;
            if (activType == Cont.ONE) {
                // redis从redis中获取，否则从数据库中获取
                String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
                JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
                isJoinActivity = redisTemplate.opsForHash().hasKey(joinKey, joinAttributes.getId());
                // 查看是否参与过活动，查询出参与记录数据
                if (isJoinActivity) {
                    activityAddRecord = new ActivityAddRecord();
                    activityAddRecord.setActivId(activId);
                    Long rId = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getId());
                    activityAddRecord.setId(rId);
                    Integer rCulp = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getCulp());
                    activityAddRecord.setCulp(rCulp);
                    String rIcon = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getIcon());
                    activityAddRecord.setIcon(rIcon);
                    String rNickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getNickName());
                    activityAddRecord.setNickName(rNickName);
                    activityAddRecord.setOpenid(openid);
                    Integer rRankInteger = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getRank());
                    Long rRank = rRankInteger.longValue();
                    activityAddRecord.setRank(rRank);
                    Integer rTeamMateCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
                    activityAddRecord.setTeamMateCount(rTeamMateCount);
                } else {
                    activityAddRecord = activityAddRecordMapper.queryByOpenidAndId(openid, activId);
                }
            } else if (activType == Cont.ZERO) {
                luckyRecord = luckRecordMapper.queryByActivIdAndOpenid(openid, activId);
            }


            Map<String, Object> resMap = new HashMap<>();
            if (activType == Cont.ONE) {
                // 查看是否参与过活动，没有则查看是否助力过，并查询出数据
                if (MaObjUtil.isEmpty(activityAddRecord)) {
                    resMap.put("recordBool", false);
                    resMap.put("friend", false);

                    String jKey = Cont.ACTIV_RESDIS_J_PRE + activId;

                    String jFeild = Cont.OPENID + openid;
                    boolean isJCastCulp = redisTemplate.opsForHash().hasKey(jKey, jFeild);
                    CastCulp castCulp = null;
                    String grabRecordId = null;
                    // 如果redis中有数据则从redis中获取 ，否则从数据库中获取，判断是否给朋友助力过，查询出该朋友数据
                    if (isJCastCulp) {
                        castCulp = new CastCulp();
                        grabRecordId = String.valueOf(redisTemplate.opsForHash().get(jKey, jFeild));

                        String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + grabRecordId;
                        castCulp.setOpenid(openid);
                        castCulp = (CastCulp) redisTemplate.opsForHash().get(gKey, openid);

                    } else {
                        castCulp = castCulpMapper.queryByOpenidAndActivId(openid, activId);
                    }
                    // 判断是否已经助力，已经助力则显示自己助力的朋友数据，否则判断record是否是分享的url携带的参数
                    if (MaObjUtil.isEmpty(castCulp)) {
                        // 判断record是否是分享的url携带的参数，不携带默认-1l
                        if (recordId != -1l) {
                            ActivityAddRecord addRecord = null;
                            String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
                            JoinAttributes joinAttributes = JoinAttributes.getInstance(ropenid);
                            boolean isJoin = redisTemplate.opsForHash().hasKey(joinKey, joinAttributes.getOpenid());
                            // 判断redis中是否有数据
                            if (isJoin) {
                                addRecord = new ActivityAddRecord();
                                addRecord.setActivId(activId);
                                addRecord.setId(recordId);
                                Integer rCulp = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getCulp());
                                addRecord.setCulp(rCulp);
                                String rIcon = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getIcon());
                                addRecord.setIcon(rIcon);
                                String rNickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getNickName());
                                addRecord.setNickName(rNickName);
                                addRecord.setOpenid(openid);
                                Long rRank = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getRank());
                                addRecord.setRank(rRank);
                                Integer rTeamMateCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
                                addRecord.setTeamMateCount(rTeamMateCount);
                            } else {
                                addRecord = activityAddRecordMapper.queryById(recordId);
                            }
                            resMap.put("friend", true);
                            resMap.put("activityAddRecord", addRecord);
                        } else {

                            resMap.put("castBool", false);
                        }
                    } else {
                        resMap.put("castBool", true);
                        if (isJCastCulp) {
                            //
                            String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
                            JoinAttributes joinAttributes = JoinAttributes.getInstance(castCulp.getRecordOpenid());
                            ActivityAddRecord activityAddRecordJoed = new ActivityAddRecord();
                            activityAddRecordJoed.setActivId(activId);
                            Long rId = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getId());
                            activityAddRecordJoed.setId(rId);
                            Integer rCulp = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getCulp());
                            activityAddRecordJoed.setCulp(rCulp);
                            String rIcon = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getIcon());
                            activityAddRecordJoed.setIcon(rIcon);
                            String rNickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getNickName());
                            activityAddRecordJoed.setNickName(rNickName);
                            activityAddRecordJoed.setOpenid(openid);
                            Integer rRankInteger = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getRank());
                            Long rRank = rRankInteger.longValue();
                            activityAddRecordJoed.setRank(rRank);
                            Integer rTeamMateCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
                            activityAddRecordJoed.setTeamMateCount(rTeamMateCount);
                            resMap.put("recordBool", true);
                            resMap.put("activityAddRecord", activityAddRecordJoed);
                        } else {
                            ActivityAddRecord addRecord = activityAddRecordMapper.queryById(castCulp.getActAddRecordId());
                            resMap.put("recordBool", true);
                            resMap.put("activityAddRecord", addRecord);
                        }

                    }
                } else {
                    resMap.put("recordBool", true);
                    resMap.put("activityAddRecord", activityAddRecord);
                    resMap.put("castBool", false);
                }
            } else if (activType == Cont.ZERO) {
                if (MaObjUtil.isEmpty(luckyRecord)) {
                    resMap.put("recordBool", false);
                } else {
                    resMap.put("recordBool", true);
                }
            }
            resMap.put("weixinActivChildChild", weixinActivChildChild);
            responMsg = ResponMsg.newSuccess(resMap);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryRecord(String openid, Integer pageNum, Integer pageSize, Integer activType) {
        if (MaObjUtil.hasEmpty(openid, pageNum, pageSize, activType)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        pageSize = pageSize > Cont.MAX_PAGE_SIZE ? Cont.MAX_PAGE_SIZE : pageSize;
        ResponMsg responMsg = null;
        Map<String, Object> redata = new HashMap<>();
        try {
            // 助力活动把redis中的数据参与纪录获取
            if (Cont.ONE == activType) {
                List<String> activIdAndTimes = redisService.getRecordTag(openid);
                List<WeixinActivRecord> weixinActivRecordList = new ArrayList<>();
                if (!MaObjUtil.isEmpty(activIdAndTimes)) {

                    for (String idAndTime : activIdAndTimes) {
                        String[] idAndTimeStr = idAndTime.split("_");
                        Long id = Long.parseLong(idAndTimeStr[0]);
                        String createTime = idAndTimeStr[1];
                        WeixinActivRecord weixinActivRecord = activMapper.aueryGrabRecordById(id);
                        weixinActivRecord.setAddTime(createTime);
                        weixinActivRecordList.add(weixinActivRecord);
                    }
                }
                redata.put("rData", weixinActivRecordList);
            }

            PageHelper.startPage(pageNum, pageSize);
            if (Cont.ZERO == activType) {
                List<WeixinActivRecord> activs = activMapper.aueryLuckRecordByOpenid(openid);
                PageInfo<WeixinActivRecord> pageInfo = new PageInfo<WeixinActivRecord>(activs);
                redata.put("yData", pageInfo);
                responMsg = ResponMsg.newSuccess(redata);
            } else if (Cont.ONE == activType) {
                List<WeixinActivRecord> activs = activMapper.aueryGrabRecordByOpenid(openid);
                PageInfo<WeixinActivRecord> pageInfo = new PageInfo<WeixinActivRecord>(activs);
                redata.put("yData", pageInfo);
                responMsg = ResponMsg.newSuccess(redata);

            }

        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryDevByActivId(Long id) {
        if (MaObjUtil.isEmpty(id)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            String adv = activMapper.queryAdvById(id);
            responMsg = ResponMsg.newSuccess(adv);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
