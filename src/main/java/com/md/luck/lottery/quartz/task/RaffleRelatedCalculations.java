package com.md.luck.lottery.quartz.task;

import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.ActivityAddRecord;
import com.md.luck.lottery.common.entity.CastCulp;
import com.md.luck.lottery.common.entity.LuckPeo;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.mapper.ActivityAddRecordMapper;
import com.md.luck.lottery.mapper.CastCulpMapper;
import com.md.luck.lottery.mapper.LuckProMapper;
import com.md.luck.lottery.service.impl.RedisServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 每天凌晨将redis备份数据到mysql
 */
@Component
public class RaffleRelatedCalculations extends QuartzJobBean {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    ActivMapper activMapper;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ActivityAddRecordMapper activityAddRecordMapper;
    @Autowired
    CastCulpMapper castCulpMapper;
    @Autowired
    LuckProMapper luckProMapper;

    /**
     * 1、是否结束助力活动
     * 2、结束的助力活动将redis中的数据备份到mysql,然后清除redis中的缓存
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Transactional(rollbackFor = SqlSessionException.class)
    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//        String param = dataMap.getString("params");
//        JSONObject jsonObject = JSONObject.parseObject(param);

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
