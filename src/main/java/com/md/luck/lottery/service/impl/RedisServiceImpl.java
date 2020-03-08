package com.md.luck.lottery.service.impl;

import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.JoinAttributes;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.ActivityAddRecord;
import com.md.luck.lottery.common.entity.CastCulp;
import com.md.luck.lottery.common.entity.LuckPeo;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.mapper.ActivityAddRecordMapper;
import com.md.luck.lottery.mapper.CastCulpMapper;
import com.md.luck.lottery.mapper.LuckProMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * redis 相关操作
 */
@Service
public class RedisServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    ActivMapper activMapper;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    ActivityAddRecordMapper activityAddRecordMapper;
    @Autowired
    CastCulpMapper castCulpMapper;
    @Autowired
    LuckProMapper luckProMapper;


    /**
     * 排名跟新
     * @param activId 活動id
     */
    public void rank(Long activId) {
        String rKey = Cont.RANK_PRE + activId;
        // 所有排名
        Set<String> openidSet = redisTemplate.opsForZSet().reverseRange(rKey, 0, - 1);
        for (String openid: openidSet) {
            String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
            JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
            Long rank =  redisTemplate.opsForZSet().reverseRank(rKey, openid) + 1;
            redisTemplate.opsForHash().put(joinKey, joinAttributes.getRank(), rank);
        }
    }
    /**
     * 将用户参与的活动保存
     * @param teamPlayerOpenid 用户openid
     * @param valueTag 参与的活动id和参与时间
     */
    public void addRecordTag(String teamPlayerOpenid, String valueTag) {
        String actKey = Cont.ADD_ACTIV_TAG_PRE + teamPlayerOpenid;
        redisTemplate.opsForList().leftPush(actKey, valueTag);
    }
    /**
     * 将用户参与的活动取出
     * @param teamPlayerOpenid 用户openid
     */
    public List<String> getRecordTag(String teamPlayerOpenid) {
        String actKey = Cont.ADD_ACTIV_TAG_PRE + teamPlayerOpenid;
//        redisTemplate.opsForList().leftPush(actKey, activId);
        boolean hasData = redisTemplate.opsForList().getOperations().hasKey(actKey);
        if (hasData) {
            List<String> activIds = redisTemplate.opsForList().range(actKey, 0, -1);
            return activIds;
        }
        return null;
    }
    /**
     * 從redis中获取所有助力活动参与者
     * @param activId 活动id
     * @return List<ActivityAddRecord>
     */
    public List<ActivityAddRecord> getActivityAddRecord(Long activId) {
        // 先从排行榜中有序查询出参与者的openid
        String rKey = Cont.RANK_PRE + activId;
        Long zlength = redisTemplate.opsForZSet().zCard(rKey);
        if (0L == zlength) {
            return null;
        }
        Set<String> openidSet = redisTemplate.opsForZSet().reverseRange(rKey, 0, - 1);

        List<ActivityAddRecord> activityAddRecords = new ArrayList<>();

        for (String openid: openidSet) {
            String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
            JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
            ActivityAddRecord activityAddRecord = new ActivityAddRecord();
            Long id = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getId());
            activityAddRecord.setId(id);
            activityAddRecord.setActivId(activId);
            Integer culp = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getCulp());
            activityAddRecord.setCulp(culp);
            String icon = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getIcon());
            activityAddRecord.setIcon(icon);
            String nickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getNickName());
            activityAddRecord.setNickName(nickName);
            activityAddRecord.setOpenid(openid);
            Long rank =  redisTemplate.opsForZSet().reverseRank(rKey, openid) + 1;
            activityAddRecord.setRank(rank);
            Integer teamMateCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
            activityAddRecord.setTeamMateCount(teamMateCount);
            String addTime = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getAddTime());
            activityAddRecord.setAddTime(addTime);
            activityAddRecords.add(activityAddRecord);

        }

        return activityAddRecords;
    }
    /**
     * 從redis中获取助力活动参与者
     * @param teamPlayerOpenid 当前用户openid
     * @param activId 活动id
     * @param pageNum 开始索引
     * @param pageSize 结束索引
     * @return List<ActivityAddRecord>
     */
    public PageInfo<ActivityAddRecord> getActivityAddRecordByRank(String teamPlayerOpenid, Long activId, Integer pageNum, Integer pageSize) {
        // 先从排行榜中有序查询出参与者的openid
        String rKey = Cont.RANK_PRE + activId;
        Long zlength = redisTemplate.opsForZSet().zCard(rKey);
        if (0L == zlength) {
            return null;
        }
        Set<String> openidSet = redisTemplate.opsForZSet().reverseRange(rKey, pageSize * (pageNum - 1), (pageSize * pageNum ) - 1);
        List<String> openidList=new ArrayList<>(openidSet);
        List<ActivityAddRecord> activityAddRecords = new ArrayList<>();

        for (String openid: openidSet) {
            String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
            JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
            ActivityAddRecord activityAddRecord = new ActivityAddRecord();
            Long id = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getId());
            activityAddRecord.setId(id);
            activityAddRecord.setActivId(activId);
            Integer culp = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getCulp());
            activityAddRecord.setCulp(culp);
            String icon = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getIcon());
            activityAddRecord.setIcon(icon);
            String nickName = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getNickName());
            activityAddRecord.setNickName(nickName);
            activityAddRecord.setOpenid(openid);
            Long rank =  redisTemplate.opsForZSet().reverseRank(rKey, openid) + 1;
            activityAddRecord.setRank(rank);
            Integer teamMateCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
            activityAddRecord.setTeamMateCount(teamMateCount);
            String addTime = (String) redisTemplate.opsForHash().get(joinKey, joinAttributes.getAddTime());
            activityAddRecord.setAddTime(addTime);
            activityAddRecords.add(activityAddRecord);

        }
        PageInfo<ActivityAddRecord> pageInfo = new PageInfo();
        pageInfo.setTotal(zlength); // 总量
        pageInfo.setSize(pageSize); // 每页条数
        pageInfo.setPrePage(pageNum - 1 == 0 ? 1 : pageNum - 1); // 当前页
        int pageNums = zlength % pageSize == 0 ? (int) (zlength / pageSize) : Double.valueOf(zlength / pageSize).intValue() + 1; // 总页数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPages(pageNums); // 总页数
        pageInfo.setNextPage(pageNums == pageNum ? pageNums : pageNum + 1); // 下一页
        pageInfo.setList(activityAddRecords);
        return pageInfo;
    }

    /**
     * 查询所有助力人员
     * @param activId 活动id
     * @param recordId 参与者id
     * @return PageInfo<CastCulp>
     */
    public List<CastCulp> getAllCastCulp(Long activId, Long recordId) {
        //
        String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + recordId;
        if (!hasData(gKey)) {
            return null;
        }
        Map<String, CastCulp> entries = redisTemplate.opsForHash().entries(gKey);
        List<CastCulp> castCulpList = new ArrayList<>();
        for (Map.Entry<String, CastCulp> entry : entries.entrySet()) {
            castCulpList.add(entry.getValue());
        }
        return castCulpList;
    }
    /**
     * 查询所有助力人员
     * @param activId 活动id
     * @param recordId 参与者id
     * @return PageInfo<CastCulp>
     */
    public Map<String, CastCulp> getAllCastCulpToMap(Long activId, Long recordId) {
        //
        String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + recordId;
        if (!hasData(gKey)) {
            return null;
        }
        Map<String, CastCulp> entries = redisTemplate.opsForHash().entries(gKey);

        return entries;
    }
    /**
     * 分页查询助力人员
     * @param teamPlayerOpenid 当前用户id
     * @param activId 活动id
     * @param recordId 参与者id
     * @param pageNum 开始索引
     * @param pageSize 结束索引
     * @return PageInfo<CastCulp>
     */
    public PageInfo<CastCulp> getCastCulpPage(String teamPlayerOpenid, Long activId, Long recordId, Integer pageNum, Integer pageSize) {
        //
        String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + recordId;
        if (!hasData(gKey)) {
            return null;
        }
//        if (pageNum == Cont.ONE) {
//            pageSize += 29;
//        } else {
            pageSize += 30;
//        }
        Map<String, CastCulp> entries = redisTemplate.opsForHash().entries(gKey);
        List<CastCulp> castCulpList = new ArrayList<>();
        boolean isMine = false;
        int i = 1;
        for (Map.Entry<String, CastCulp> entry : entries.entrySet()) {
            int ia = pageNum * pageSize - pageSize + 1;
            if (ia != i) {
                i++;
                continue;
            }
//            if (pageNum == Cont.ONE && entry.getKey().equals(teamPlayerOpenid)) {
//                isMine = true;
//            }
            castCulpList.add(entry.getValue());
            i++;
        }
//        if (!isMine) {
//            CastCulp castCulp = (CastCulp) redisTemplate.opsForHash().get(gKey, teamPlayerOpenid);
//            if (!MaObjUtil.isEmpty(castCulp)) {
//                castCulpList.add(castCulp);
//            }
//
//        }
        PageInfo<CastCulp> pageInfo = new PageInfo();
        Integer zlength = entries.size();
        pageInfo.setTotal(zlength); // 总量
        pageInfo.setSize(pageSize); // 每页条数
        pageInfo.setPrePage(pageNum - 1 == 0 ? 1 : pageNum - 1); // 当前页
        int pageNums = zlength % pageSize == 0 ? (int) (zlength / pageSize) : Double.valueOf(zlength / pageSize).intValue() + 1; // 总页数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPages(pageNums); // 总页数
        pageInfo.setNextPage(pageNums == pageNum ? pageNums : pageNum + 1); // 下一页
        pageInfo.setList(castCulpList);
        return pageInfo;
    }

    /**
     * 判断hash中是否有数据
     * @param key key hash key
     * @return boolean
     */
    public boolean hasData(String key) {
        long size = redisTemplate.opsForHash().size(key);
        if (size == 0) {
            return false;
        }
        return true;
    }
    public boolean delAddRecord(Long activId, String openid) {
        String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
//        String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(29L);
//        String openid1 = "ot6_Xvt80Txu5TWtgH7dklajeZ0s";
        if (redisTemplate.opsForHash().getOperations().hasKey(joinKey)) {
            System.out.println(true);
        }
        JoinAttributes joinAttributes = JoinAttributes.getInstance(openid);
        redisTemplate.opsForHash().getOperations().delete(joinKey);

        if (hasData(joinKey)) {
            return false;
        }
        return true;
    }

    /**
     * 活动id获取所有中奖名单
     * @param activId 活动id
     * @return List<LuckPeo>
     */
    public List<LuckPeo> getLuckPro(Long activId) {
        String rlKey = Cont.RANL_LUCKY_PRE + activId;
       boolean has =  hasData(rlKey);
       if (has) {
           Map<String, LuckPeo> luckPeoMap = redisTemplate.opsForHash().entries(rlKey);
           Collection<LuckPeo> valueCollection = luckPeoMap.values();
           List<LuckPeo> valueList = new ArrayList<LuckPeo>(valueCollection);
           return valueList;
       }
       return null;
    }



    public void backUpData() {
        boolean except = false;
        // 查询所有助力活动
        List<Activ> activList = activMapper.queryByActivType(Cont.ONE);
        try {


            for (Activ activ : activList) {
                Long activId = activ.getId();
                String activityEndKey = Cont.ACTIVITY_END_PRE + activId;
                String isActivityEnd = (String) redisTemplate.opsForHash().get(activityEndKey, activityEndKey);
                if (!MaObjUtil.isEmpty(isActivityEnd)) {
                    // 活动结束
                    // 1、备份参与记录；2、备份助力；3、备份结果
                    List<ActivityAddRecord> activityAddRecords = redisService.getActivityAddRecord(activId);
                    // 判断此活动是否有参与活动人员数据
                    if (MaObjUtil.isEmpty(activityAddRecords) || activityAddRecords.size() == Cont.ZERO) {
                        continue;
                    }
                    // 将助力活动参与记录备份到mysql
                    activityAddRecordMapper.batchInsert(activityAddRecords);
                    // 将助力保存到mysql
                    for (ActivityAddRecord activityAddRecord : activityAddRecords) {
                        String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + activityAddRecord.getId();
                        boolean isCast = redisTemplate.opsForHash().getOperations().hasKey(gKey);
                        // 判断此参与者是否有助力
                        if (isCast) {
                            List<CastCulp> castCulpList = redisService.getAllCastCulp(activId, activityAddRecord.getId());
                            // 判断是否有助力
                            if (MaObjUtil.isEmpty(castCulpList) || castCulpList.size() == Cont.ZERO) {
                                continue;
                            }
                            castCulpMapper.batchInsert(castCulpList);
                        }
                    }
                    // 备份活动结果
                    List<LuckPeo> luckPeos = redisService.getLuckPro(activId);
                    // 判断是活动否出结果
                    if (MaObjUtil.isEmpty(luckPeos) || luckPeos.size() == Cont.ZERO) {
                        continue;
                    }
                    luckProMapper.batchInsert(luckPeos);
                }
            }
        } catch (SqlSessionException e) {
            except = true;

            log.error(e.getMessage());
        } catch (Exception e) {
            except = true;
            log.error(e.getMessage());
        }
        if (!except) {
            // 删除备份
            delDataInRedis();
        }
    }
    // 删除备份
    public void delDataInRedis() {
        // 查询所有助力活动
        List<Activ> activList = activMapper.queryByActivType(Cont.ONE);
        for (Activ activ : activList) {
            Long activId = activ.getId();
            String activityEndKey = Cont.ACTIVITY_END_PRE + activId;
            String isActivityEnd = (String) redisTemplate.opsForHash().get(activityEndKey, activityEndKey);
            if (!MaObjUtil.isEmpty(isActivityEnd)) {
                // 删除活动结束标志
                redisTemplate.opsForHash().getOperations().delete(activityEndKey);
                // 1、删除参与记录；2、删除助力；3、删除结果

                List<ActivityAddRecord> activityAddRecords = redisService.getActivityAddRecord(activId);
                // 判断此活动是否有参与活动人员数据
                if (MaObjUtil.isEmpty(activityAddRecords) || activityAddRecords.size() == Cont.ZERO) {
                    continue;
                }

                for (ActivityAddRecord activityAddRecord : activityAddRecords) {
                    // 将助力删除
                    String gKey = Cont.ACTIV_RESDIS_GRAB_PRE + activId + "_" + activityAddRecord.getId();
                    redisTemplate.opsForHash().getOperations().delete(gKey);
                    // 删除参与tag
                    String openid = activityAddRecord.getOpenid();
                    String actKey = Cont.ADD_ACTIV_TAG_PRE + openid;
                    redisTemplate.opsForList().getOperations().delete(actKey);
                }
                // 删除助力标志
                String jKey = Cont.ACTIV_RESDIS_J_PRE + activId;
                redisTemplate.opsForHash().getOperations().delete(jKey);
                // 删除助力活动参与记录
                String joinKey = Cont.ACTIV_RESDIS_KEY_PRE + String.valueOf(activId);
                redisTemplate.opsForHash().getOperations().delete(joinKey);

                // 删除排名
                String rKey = Cont.RANK_PRE + activId;
                redisTemplate.opsForZSet().getOperations().delete(rKey);

                // 删除中奖结果
                String rlKey = Cont.RANL_LUCKY_PRE + activId;
                redisTemplate.opsForHash().getOperations().delete(rlKey);
            }
        }
    }
}
