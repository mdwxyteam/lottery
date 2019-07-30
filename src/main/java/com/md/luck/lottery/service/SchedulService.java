package com.md.luck.lottery.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: SchedulService
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/7/30 9:44
 */
public interface SchedulService {
    void addSchedul(String jobName, String jobGroupName,
                    String triggerName, String triggerGroupName, String jobClassName, String cron, JSONObject param);

    void deleteJob(String jobName, String jobGroupName);
}
