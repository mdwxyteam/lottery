package com.md.luck.lottery.controller;

import cn.hutool.db.sql.Order;
import com.md.luck.lottery.service.*;
import com.md.luck.lottery.service.impl.WeixinServiceImpl;
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
    @Autowired
    protected GoodsService goodsService;
    @Autowired
    protected PayCodeService payCodeService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected WexinService weixinService;
    @Autowired
    protected CustomerService customerService;
    @Autowired
    protected ActivityAddRecordService activityAddRecordService;
    @Autowired
    protected CastCulpService castCulpService;
    @Autowired
    protected LuckRecordService luckRecordService;
    @Autowired
    protected LuckPeoService luckPeoService;
}
