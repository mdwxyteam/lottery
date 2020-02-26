package com.md.luck.lottery.service.impl;

import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.JoinAttributes;
import com.md.luck.lottery.common.entity.ActivityAddRecord;
import com.md.luck.lottery.common.entity.CastCulp;
import com.md.luck.lottery.common.util.MaObjUtil;
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
            Long rank = (Long) redisTemplate.opsForHash().get(joinKey, joinAttributes.getRank());
            activityAddRecord.setRank(rank);
            Integer teamMateCount = (Integer) redisTemplate.opsForHash().get(joinKey, joinAttributes.getTeamMateCount());
            activityAddRecord.setTeamMateCount(teamMateCount);
            Date addTime = (Date) redisTemplate.opsForHash().get(joinKey, joinAttributes.getAddTime());
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
}
