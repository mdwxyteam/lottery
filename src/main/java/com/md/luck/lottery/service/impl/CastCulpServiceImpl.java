package com.md.luck.lottery.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.JoinAttributes;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.entity.request.HelpGrab;
import com.md.luck.lottery.common.entity.respons.CastCulpChild;
import com.md.luck.lottery.common.util.MaMathUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.*;
import com.md.luck.lottery.service.CastCulpService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CastCulpServiceImpl implements CastCulpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    ActivMapper activMapper;
    @Autowired
    CastCulpMapper castCulpMapper;
    @Autowired
    ActivityAddRecordMapper activityAddRecordMapper;
    @Autowired
    AtivPrizeMapper ativPrizeMapper;
    @Autowired
    LuckProMapper luckProMapper;

    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public ResponMsg culp(String teamPlayerOpenid, HelpGrab helpGrab) {
        if (MaObjUtil.hasEmpty(teamPlayerOpenid, helpGrab) && MaObjUtil.hasEmpty(helpGrab.getActivId(), helpGrab.getOepnid())) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        if (teamPlayerOpenid.equals(helpGrab.getOepnid())) {
            return ResponMsg.newFail(null).setMsg("已经参与过此活动");
        }
        //检查是否参与过助力
        CastCulp castCulp = castCulpMapper.queryByOpenidAndActivId(teamPlayerOpenid, helpGrab.getActivId());
        if (!MaObjUtil.isEmpty(castCulp)) {
            return ResponMsg.newFail(null).setMsg("已经助力过此活动");
        }
        ResponMsg responMsg = null;
        try {
            Customer customer = customerMapper.queryByOpenid(teamPlayerOpenid);
            if (MaObjUtil.isEmpty(customer)) {
                return ResponMsg.newFail(null).setMsg("用户信息异常");
            }
            // 助力数量
            int ran = RandomUtil.randomEle(Cont.RANDOM_LIMIT);
            // 修改活动人气数
            Activ activ = activMapper.activById(helpGrab.getActivId());
            int popularity = activ.getPopularity();
            popularity++;
            if (popularity == Integer.parseInt(activ.getCondition())) {
                activMapper.updatePopularityAndState(popularity, Cont.ZERO, helpGrab.getActivId());
                List<ActivityAddRecord> activityAddRecordList = activityAddRecordMapper.queryByActivId(helpGrab.getActivId());
                List<AtivPrize> ativPrizes = ativPrizeMapper.queryByAtivId(helpGrab.getActivId());
                // 奖品对应的或得者
                for (AtivPrize ativPrize : ativPrizes) {
                    int ran1 = RandomUtil.randomInt(1, activityAddRecordList.size());
                    ActivityAddRecord addRecord = activityAddRecordList.get(ran1);
                    activityAddRecordList.remove(addRecord);
                    LuckPeo luckPeo = new LuckPeo();
                    luckPeo.setIcon(addRecord.getIcon());
                    luckPeo.setNickName(addRecord.getNickName());
                    luckPeo.setOpenid(addRecord.getOpenid());
                    luckPeo.setRank(Integer.parseInt(ativPrize.getRanking()));
                    luckPeo.setRecordId(addRecord.getId());
                    luckPeo.setPrizeName(ativPrize.getPrizeDescription());
                    luckPeo.setActivId(helpGrab.getActivId());
                    luckProMapper.insert(luckPeo);
//                    luckRecordMapper.updateLuck(Cont.ZERO, luckyRecordLucky.getId());
                }
            } else {
                activMapper.updatePopularity(popularity, helpGrab.getActivId());
            }
            // 更新参与记录中助力数量，助力人数
            ActivityAddRecord activityAddRecordYs = activityAddRecordMapper.queryTeamMateCountAndCulp(helpGrab.getGrabRecordId(), helpGrab.getOepnid());
            int teamMateCount = activityAddRecordYs.getTeamMateCount() + 1;
            int culpNum = activityAddRecordYs.getCulp() + ran;
            activityAddRecordMapper.updateCulpAndTeamMateCount(culpNum, teamMateCount, helpGrab.getGrabRecordId(), helpGrab.getOepnid());
            //更新排名
            ActivityAddRecord activityAddRecord = activityAddRecordMapper.queryTeamMateCountAndRank(helpGrab.getGrabRecordId(), helpGrab.getOepnid());
            activityAddRecordMapper.updateCulpRank(activityAddRecord.getRank(), helpGrab.getGrabRecordId(), helpGrab.getOepnid());
            // 新增助力记录
            CastCulpChild castCulpChild = new CastCulpChild();
            castCulpChild.setActAddRecordId(helpGrab.getGrabRecordId());
            castCulpChild.setActivid(helpGrab.getActivId());
            castCulpChild.setCastCulp(ran);
            castCulpChild.setNickName(customer.getNickName());
            castCulpChild.setIcon(customer.getIcon());
            castCulpChild.setOpenid(teamPlayerOpenid);
            castCulpMapper.insert(castCulpChild);
            castCulpChild.setTotalCulp(activityAddRecord.getCulp());
//            castCulpChild.setRank(activityAddRecord.getRank());
            responMsg = ResponMsg.newSuccess(castCulpChild);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg culpByRedis(String teamPlayerOpenid, HelpGrab helpGrab) {
        if (MaObjUtil.hasEmpty(teamPlayerOpenid, helpGrab) && MaObjUtil.hasEmpty(helpGrab.getActivId(), helpGrab.getOepnid())) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        if (teamPlayerOpenid.equals(helpGrab.getOepnid())) {
            return ResponMsg.newFail(null).setMsg("已经参与过此活动");
        }
        // 活动结束判断
        String activityEndKey = Cont.ACTIVITY_END_PRE + helpGrab.getActivId();
        String isActivityEnd = (String) redisTemplate.opsForHash().get(activityEndKey, activityEndKey);
        if (!MaObjUtil.isEmpty(isActivityEnd)) {
            return ResponMsg.newFail(null).setMsg("活动已结束不能再助力");
        }
        Customer customer = customerMapper.queryByOpenid(teamPlayerOpenid);
        // 通过活动id和当前用户openid判断是否参与过当前活动
        // 1、查询活动所有参与者判断是否有参与；2、判断用户是否助力过这个活动
        String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + helpGrab.getActivId();
        JoinAttributes joinAttributes = JoinAttributes.getInstance(teamPlayerOpenid);
        boolean isJoinActivity = redisTemplate.opsForHash().hasKey(joinKey, joinAttributes.getOpenid());
        if (isJoinActivity) {
            return ResponMsg.newFail(null).setMsg("已经参与过此活动");
        }
        String jKey = Cont.ACTIV_RESDIS_J_PRE + helpGrab.getActivId();
        String jFeild = Cont.OPENID + teamPlayerOpenid;
        boolean isGrab = redisTemplate.opsForHash().hasKey(jKey, jFeild);
        if (isGrab) {
            return ResponMsg.newFail(null).setMsg("已经助力过此活动");
        }
        // 生成助力
        // 助力数量
        Integer grabNum = RandomUtil.randomEle(Cont.RANDOM_LIMIT);
        //助力标志
        redisTemplate.opsForHash().put(jKey, jFeild, helpGrab.getGrabRecordId());
        //保存助力记录
        String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + helpGrab.getActivId() + "_" + helpGrab.getGrabRecordId();
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long cid = snowflake.nextId();

       //增加助力记录
        CastCulp castCulp = new CastCulp();
        castCulp.setOpenid(teamPlayerOpenid);
        castCulp.setId(cid);
        castCulp.setIcon(customer.getIcon());
        castCulp.setNickName(customer.getNickName());
        castCulp.setCastCulp(grabNum);
        castCulp.setActivid(helpGrab.getActivId());
        castCulp.setActAddRecordId(helpGrab.getGrabRecordId());
        redisTemplate.opsForHash().put(gKey, teamPlayerOpenid, castCulp);


        //修改排名  //修改排行榜
        String rKey = Cont.RANK_PRE + helpGrab.getActivId();
        redisTemplate.opsForZSet().incrementScore(rKey, helpGrab.getOepnid(), grabNum);

        //修改基本数据（排名，助力数， 助力人数）
        Long rank = redisTemplate.opsForZSet().rank(rKey, helpGrab.getGrabRecordId());
        //排名
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getRank(), rank);
        //助力数
        Integer culp = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getCulp());
        culp = culp + grabNum;
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getCulp(), culp);
        //助力人数
        Integer teamCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
        teamCount++;
        redisTemplate.opsForHash().put(joinKey, joinAttributes.getTeamMateCount(), teamCount);

        //修改活动人气
        Activ activ = activMapper.activById(helpGrab.getActivId());
        int popularity = activ.getPopularity();
        popularity++;
        activMapper.updatePopularity(popularity, helpGrab.getActivId());
        //活动是否结束
        if (popularity == Integer.parseInt(activ.getCondition())) {
            // 结束
            // 修改活动状态为结束
            activMapper.updatePopularityAndState(popularity, Cont.ZERO, helpGrab.getActivId());
            //活动所有奖品
            List<AtivPrize> ativPrizes = ativPrizeMapper.queryByAtivId(helpGrab.getActivId());
            //活动前五参与者
            Set<String> preFiveAdder = redisTemplate.opsForZSet().range(rKey, Cont.ZERO, Cont.FIVE);
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
                luckPeo.setActivId(helpGrab.getActivId());
                luckPeo.setId(MaMathUtil.creatId());
                //保存奖品获得者记录
                String rlKey = Cont.RANL_LUCKY_PRE + helpGrab.getActivId();
                redisTemplate.opsForHash().put(rlKey, luckPeo.getId(), luckPeo);
                //redis添加活动结束标志
                String aendKey = Cont.ACTIVITY_END_PRE + helpGrab.getActivId();
                redisTemplate.opsForHash().put(aendKey, aendKey, Cont.END);
            }
        }
        CastCulpChild castCulpChild = new CastCulpChild();
        castCulpChild.setActAddRecordId(helpGrab.getGrabRecordId());
        castCulpChild.setActivid(helpGrab.getActivId());
        castCulpChild.setCastCulp(grabNum);
//        castCulpChild.setNickName(customer.getNickName());
//        castCulpChild.setIcon(customer.getIcon());
//        castCulpChild.setOpenid(teamPlayerOpenid);
        castCulpChild.setTotalCulp(culp);
        castCulpChild.setRank(rank);
        return ResponMsg.newSuccess(castCulpChild);
    }

    @Override
    public ResponMsg queryPage(Long activId, String teamPlayerOpenid, Integer pageNum, Integer pageSize, Long recordId) {
        if (MaObjUtil.hasEmpty(pageNum, pageSize, recordId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        pageSize = pageSize > Cont.MAX_PAGE_SIZE ? Cont.MAX_PAGE_SIZE : pageSize;
        ResponMsg responMsg = null;
        try {
            String jKey = Cont.ACTIV_RESDIS_J_PRE + activId;
            String jFeild = Cont.OPENID + teamPlayerOpenid;
            boolean isGrab = redisTemplate.opsForHash().hasKey(jKey, jFeild);
            if (isGrab) {
                // todo 如何查询助力人员数据
            }
            PageHelper.startPage(pageNum, pageSize);
            List<CastCulp> castCulpList = castCulpMapper.queryByRecordId(recordId);
            PageInfo<CastCulp> pageInfo = new PageInfo<CastCulp>(castCulpList);

            responMsg = ResponMsg.newSuccess(pageInfo);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    public static void main(String[] args) throws InterruptedException {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long id = snowflake.nextId();
        System.out.println(id);
        Thread.sleep(1000);
        System.out.println(snowflake.nextId());
        for (int i = 0; i < 30; i++) {
            Thread.sleep(900);
            System.out.println(snowflake.nextId());
//            System.out.println(RandomUtil.randomEle(Cont.RANDOM_LIMIT));
//            long id = snowflake.nextId();
//            System.out.println(id);
        }
    }
}
