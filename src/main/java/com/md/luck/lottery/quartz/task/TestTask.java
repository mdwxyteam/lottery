package com.md.luck.lottery.quartz.task;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: TestTask
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/7/30 10:17
 */
@Component
public class TestTask extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("--------");
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String param = dataMap.getString("params");
    }
}
