package com.md.luck.lottery.controller;

import com.md.luck.lottery.service.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: BaseController
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/8/29 11:21
 */
public class BaseController {
    @Autowired
    protected SponsorTypeService sponsorTypeService;
    @Autowired
    protected SponsorService sponsorService;
    @Autowired
    protected PrizeService prizeService;
    @Autowired
    protected SponsorPrizeService sponsorPrizeService;
    @Autowired
    protected ActivPrizeService activPrizeService;
    @Autowired
    protected ActivService activService;

}
