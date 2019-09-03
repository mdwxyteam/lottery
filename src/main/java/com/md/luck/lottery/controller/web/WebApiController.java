package com.md.luck.lottery.controller.web;

import com.github.pagehelper.Page;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.SponsorType;
import com.md.luck.lottery.controller.BaseController;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: WebApiController
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/8/29 10:25
 */
@RestController
@RequestMapping("/web/api")
public class WebApiController extends BaseController {

    /**
     * 新增商户种类
     * @param type
     * @return
     */
    @PostMapping("/add/type")
    public ResponMsg addType(@RequestParam("type") String type) {
        return sponsorTypeService.add(type);
    }

    /**
     * 分页查询商户
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return ResponMsg<List<Page<SponsorType>>>
     */
    @GetMapping("/page/type")
    public ResponMsg<List<Page<SponsorType>>> pageType(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return sponsorTypeService.page(pageNum, pageSize);
    }

    /**
     * 新增商户
     * @param type 商户类型id
     * @param sponsor 商户名称
     * @param position 商户位置
     * @param detalis 商户详细信息
     * @return ResponMsg
     */
    @PostMapping("/add/sponsor")
    public ResponMsg addSponsor(@RequestParam("type") long type, @RequestParam("sponsor") String sponsor, @RequestParam("position") String position, @RequestParam("detalis") String detalis) {
        return sponsorService.add(sponsor, position, detalis, type);
    }

    /**
     * 新增奖品
     * @param prizeDescription 奖品，描述
     * @param iconUrl 奖品图片
     * @param prizeCount 奖品数量
     * @return ResponMsg
     */
    @PostMapping("/add/prize")
    public ResponMsg addPrize(@RequestParam("prizeDescription") String prizeDescription, @RequestParam("iconUrl") String iconUrl, @RequestParam("prizeCount") int prizeCount) {
        return prizeService.add(prizeDescription, iconUrl, prizeCount);
    }
}
