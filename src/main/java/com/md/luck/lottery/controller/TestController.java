package com.md.luck.lottery.controller;

import com.alibaba.fastjson.JSONObject;
import com.md.luck.lottery.common.util.ConUtil;
import com.md.luck.lottery.common.util.WechatMpConfig;
import com.md.luck.lottery.service.SchedulService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
//        String taskStr = "com.md.luck.lottery.quartz.task.TestTask";
        String taskStr = "com.md.luck.lottery.quartz.task.RaffleRelatedCalculations";
//        String con = ConUtil.getCron("2019-10-21 14:27:30","yyyy-MM-dd HH:mm:ss");
//        String con = "0 0/30 * * * ?";//每30分钟执行一次
        String con = "0/30  * * * ?";//每30秒钟执行一次
//        String con = "0 15 2 ? * *";//每天执行一次
//        schedulService.addSchedul("jobName2", "jobGroupName2", "triggerName2",  "triggerGroupName2", taskStr, "0/1 * * * * ?", new JSONObject());
//        schedulService.addSchedul("jobName2", "jobGroupName2", "triggerName2",  "triggerGroupName2", taskStr, con, new JSONObject());
        schedulService.addSchedul("lotteryCalculation", "lotteryCalculation", "lotteryCalculation", "lotteryCalculation", taskStr, con, new JSONObject());
    }

    @PostMapping("/del")
    public void deleteJob() {
        schedulService.deleteJob("jobName2", "jobGroupName2");
    }
    @Autowired
    private WechatMpConfig wechatMpConfig;
    @GetMapping("/sendWxMsg")
    public String sendWxMsg() {
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("oGGQw1EaleMewMz3cz-GvQzBfegc")
                .templateId("ERRkN7P_Q_sPHORsD1vzWUW45Jr66FI2jc5SxjTK1vM")
                .url("https://mp.weixin.qq.com/debug/cgi-bin/readtmpl?t=tmplmsg/faq_tmpl")
                .build();

        templateMessage.addData(new WxMpTemplateData("first", "你在Ai捡耙活参与的着力活动出结果了", "#FF00FF"));
        templateMessage.addData(new WxMpTemplateData("keyword3", "HUAWEI Mate 30 5G 全网通 8GB 256GB 麒麟990", "blue"));
        templateMessage.addData(new WxMpTemplateData("keyword4", "2020-3-9", "blue"));
        templateMessage.addData(new WxMpTemplateData("remark", "详细结果请查看详情", "blue"));
        WxMpService wxMpService = wechatMpConfig.wxMpService();
        try {
            String reStr = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            System.out.println("------------------");
            System.out.println(reStr);
            return reStr;
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "null";
    }


    public static void main(String[] args) {
        Long lon = 1134468L;
        System.out.println(lon / 100);
    }
}
