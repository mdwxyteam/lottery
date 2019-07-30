package com.md.luck.lottery.controller;

import com.alibaba.fastjson.JSONObject;
import com.md.luck.lottery.service.SchedulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: TestController
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/7/30 14:10
 */

@RestController
@RequestMapping("/quartz")
@Slf4j

public class TestController {
    @Autowired
    private SchedulService schedulService;

    @PostMapping("/add")
    public void startJob() {
        String taskStr = "com.md.luck.lottery.quartz.task.TestTask";
        schedulService.addSchedul("jobName1", "jobGroupName1", "triggerName1",  "triggerGroupName1", taskStr, "0/5 * * * * ?", new JSONObject());
    }
    @PostMapping("/del")
    public void deleteJob() {
        schedulService.deleteJob("jobName1", "jobGroupName1");
    }
}
