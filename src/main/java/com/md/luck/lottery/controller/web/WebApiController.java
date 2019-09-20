package com.md.luck.lottery.controller.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.Prize;
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
     * 分页查询奖品
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return ResponMsg<List<Page<SponsorType>>>
     */
    @GetMapping("/page/prize")
    public ResponMsg<PageInfo<Prize>> pagePrize(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return prizeService.page(pageNum, pageSize);
    }

    /**
     * 分页查询商户
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return ResponMsg<List<Page<SponsorType>>>
     */
    @GetMapping("/page/type")
    public ResponMsg<PageInfo<SponsorType>> pageType(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return sponsorTypeService.page(pageNum, pageSize);
    }

    /**
     * 新增商户
     * @param typeId 商户类型id
     * @param sponsor 商户名称
     * @param location 商户坐标
     * @param address 商户位置
     * @param detalis 商户详细信息
     * @return ResponMsg
     */
    @PostMapping("/add/sponsor")
    public ResponMsg addSponsor(@RequestParam("type") long typeId, @RequestParam("type") String type, @RequestParam("sponsor") String sponsor, @RequestParam("location") String location, @RequestParam("address") String address, @RequestParam("detalis") String detalis) {
        return sponsorService.add(sponsor, location, address, detalis, typeId, type);
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

    /**
     * 新增赞助商与奖品关联
     * @param sponsorId 赞助商id
     * @param prizeid 奖品id
     * @param prizeCount 奖品数量
     * @return ResponMsg
     */
    @PostMapping("/add/sponsor/prize")
    public ResponMsg addSponsorPrize(@RequestParam("sponsorId") Long sponsorId, @RequestParam("prizeid") Long prizeid, @RequestParam("prizeCount") int prizeCount) {
        return sponsorPrizeService.add(sponsorId, prizeid, prizeCount);
    }

    /**
     * 新增活动与奖品关联
     * @param ativId 活动id
     * @param prizeId 奖品id
     * @param prizeCount 奖品数量
     * @return ResponMsg
     */
    @PostMapping("/add/activ/prize")
    public ResponMsg addAtivPrize(@RequestParam("ativId") Long ativId, @RequestParam("prizeId") Long prizeId, @RequestParam("prizeCount") int prizeCount) {
        return activPrizeService.add(ativId, prizeId, prizeCount);
    }

    public ResponMsg addActiv(@RequestParam("sponsorid") Long sponsorid, @RequestParam("sponsor") String sponsor, @RequestParam("location") String location,
                              @RequestParam("address") String address, @RequestParam("conditionid") Long conditionid, @RequestParam("sponsorClaim") String sponsorClaim,
                              @RequestParam("state") int state, @RequestParam("adv") String adv) {
        return null;
    }

    /**
     * 新增活动信息
     * @param activ 活动信息
     * @return ResponMsg
     */
    @PostMapping("/add/activ")
    public ResponMsg addActiv(@RequestBody Activ activ) {
        return activService.add(activ);
    }
}
