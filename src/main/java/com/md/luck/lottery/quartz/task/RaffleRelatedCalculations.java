package com.md.luck.lottery.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.ActivityAddRecord;
import com.md.luck.lottery.common.entity.CastCulp;
import com.md.luck.lottery.common.entity.WeixnActiv;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.mapper.ActivityAddRecordMapper;
import com.md.luck.lottery.mapper.CastCulpMapper;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RaffleRelatedCalculations extends QuartzJobBean {
    @Autowired
    ActivMapper activMapper;
    @Autowired
    ActivityAddRecordMapper activityAddRecordMapper;
    @Autowired
    CastCulpMapper castCulpMapper;

    /**
     * 1、结束活动
     * 2、分别统计活动人气
     * 3、分别统计每个活动参与人所获得的助力数量
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String param = dataMap.getString("params");
        JSONObject jsonObject = JSONObject.parseObject(param);

        // 查询所有未结束活动
        List<Activ> activList = activMapper.queryByActivTypeAndState(Cont.ONE, Cont.ONE);
        List<ActivityAddRecord> activityAddRecordList = activityAddRecordMapper.queryByActiv(Cont.ONE, Cont.ONE);
        List<CastCulp> castCulpList = castCulpMapper.queryByActiv(Cont.ONE, Cont.ONE);

//        for ()
    }
}
