package com.md.luck.lottery.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.md.luck.lottery.quartz.QuartzUtil;
import com.md.luck.lottery.service.SchedulService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: SchedulServiceImpl
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/7/30 9:45
 */
@Service
public class SchedulServiceImpl implements SchedulService {
    private static final Log log = LogFactory.getLog(SchedulServiceImpl.class);
    @Autowired
    private QuartzUtil quartzUtil;
    @Override
    public void addSchedul(String jobName, String jobGroupName,
                           String triggerName, String triggerGroupName, String jobClassName, String cron, JSONObject param) {
        try {
            quartzUtil.addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClassName, cron, param);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJob(String jobName, String jobGroupName) {
        quartzUtil.deleteJob(jobName, jobGroupName);
    }
}
