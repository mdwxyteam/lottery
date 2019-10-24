package com.md.luck.lottery.quartz.task;

import com.alibaba.fastjson.JSONObject;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 活动倒计时任务
 */
@Component
public class ActivityCountdownTask extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String param = dataMap.getString("params");
        JSONObject jsonObject = JSONObject.parseObject(param);
        System.out.println(jsonObject.get("activName").toString() + jsonObject.get("huodong").toString());
    }

    public static void main(String[] args) {
        double a = 0.0000011;
        long i = 30000;
        double d = 0.00;
        long x = (long) (d + 0.5);
        System.out.println(i * a);
        System.out.println(x);
    }
}
