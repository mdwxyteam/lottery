package com.md.luck.lottery.controller.web;

import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.RequestBodyObJ;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.controller.BaseController;
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
     * 查询所有商户类型
     * @return ResponMsg
     */
    @GetMapping("/allType")
    public ResponMsg allType() {
        return sponsorTypeService.allType();
    }
    /**
     * 修改商户种类
     * @param typeId
     * @return
     */
    @PostMapping("/edit/type")
    public ResponMsg statusType(@RequestParam("typeId") Long typeId,@RequestParam("typeName") String typeName ) {
        return sponsorTypeService.edit(typeId,typeName);
    }

    /**
     * 商户状态改变
     * @param typeId
     * @param isStatus
     * @return
     */
    @PostMapping("/status/type")
    public ResponMsg statusType(@RequestParam("typeId") Long typeId,@RequestParam("isStatus") int isStatus ) {
        return sponsorTypeService.status(typeId,isStatus);
    }


    /**
     * 分页查询奖品
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return ResponMsg<List < Page < SponsorType>>>
     */
    @GetMapping("/page/prize")
    public ResponMsg<PageInfo<Prize>> pagePrize(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return prizeService.page(pageNum, pageSize);
    }

    /**
     * 分页查询商户类型
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return ResponMsg<List < Page < SponsorType>>>
     */
    @GetMapping("/page/type")
    public ResponMsg<PageInfo<SponsorType>> pageType(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return sponsorTypeService.page(pageNum, pageSize);
    }

    /**
     * 新增商户
     * @param requestBodyObJ {* @param typeId   商户类型id
     *      * @param sponsor  商户名称
     *      * @param location 商户坐标
     *      * @param address  商户位置
     *      * @param detalis  商户详细信息}
     * @return ResponMsg
     */
    @PostMapping("/add/sponsor")
    public ResponMsg addSponsor(@RequestBody RequestBodyObJ requestBodyObJ) {
        return sponsorService.add(requestBodyObJ.getSponsor(), requestBodyObJ.getLocation(), requestBodyObJ.getAddress(), requestBodyObJ.getDetalis(), requestBodyObJ.getTypeId(), requestBodyObJ.getType());
    }

    /**
     * 通过赞助商id查询
     * @param sponsorId sponsorId
     * @return ResponMsg
     */
    @GetMapping("/detail/sponsor")
    public ResponMsg detailSponsor(@RequestParam("sponsorId") long sponsorId) {
        return sponsorService.detailById(sponsorId);
    }

    /**
     * 修改赞助商
     * @param requestBodyChild requestBodyChild
     * @return ResponMsg
     */
    @PostMapping("/update/sponsor")
    public ResponMsg updateSponsor(@RequestBody RequestBodyChild requestBodyChild) {
        return sponsorService.update(requestBodyChild.getId(), requestBodyChild.getSponsor(), requestBodyChild.getLocation(), requestBodyChild.getAddress(), requestBodyChild.getDetalis(), requestBodyChild.getTypeId(), requestBodyChild.getType());
    }

    /**
     * 分页查询赞助商
     * @param pageNum 页面
     * @param pageSize 页大小
     * @return Response
     */
    @GetMapping("/sponsor/page")
    public ResponMsg<List<Sponsor>> pageSponsor(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return sponsorService.page(pageNum, pageSize);
    }

    /**
     * 通过类型查询赞助商
     *
     * @param typeId 类型id
     * @return List<Sponsor>
     */
    @GetMapping("/sponsor/byType")
    public ResponMsg<List<Sponsor>> byTypeSponsor(@RequestParam("typeId") Long typeId) {
        return sponsorService.byType(typeId);
    }
    /**
     * 新增奖品
     * @param prizeDescription 奖品，描述
     * @param iconUrl          奖品图片
     * @param prizeCount       奖品数量
     * @return ResponMsg
     */
    @PostMapping("/add/prize")
    public ResponMsg addPrize(@RequestParam("prizeDescription") String prizeDescription, @RequestParam("iconUrl") String iconUrl, @RequestParam("prizeCount") int prizeCount) {
        return prizeService.add(prizeDescription, iconUrl, prizeCount);
    }

    /**
     * 更新奖品
     * @param prizeId          奖品Id
     * @param prizeDescription 奖品，描述
     * @param iconUrl          奖品图片
     * @param prizeCount       奖品数量
     * @return ResponMsg
     */
    @PostMapping("/edit/prize")
    public ResponMsg editPrize(@RequestParam("prizeId") Long prizeId, @RequestParam("prizeDescription") String prizeDescription, @RequestParam("iconUrl") String iconUrl, @RequestParam("prizeCount") int prizeCount) {
        return prizeService.edit(prizeId, prizeDescription, iconUrl, prizeCount);
    }

    /**
     * 状态删除奖品
     * @param prizeId 奖品Id
     * @return
     */
    @PostMapping("/del/prize")
    public ResponMsg delPrize(@RequestParam("prizeId") Long prizeId) {
        return prizeService.del(prizeId);
    }

    /**
     * 新增赞助商与奖品关联
     * @param sponsorId  赞助商id
     * @param prizeid    奖品id
     * @param prizeCount 奖品数量
     * @return ResponMsg
     */
    @PostMapping("/add/sponsor/prize")
    public ResponMsg addSponsorPrize(@RequestParam("sponsorId") Long sponsorId, @RequestParam("prizeid") Long prizeid, @RequestParam("prizeCount") int prizeCount) {
        return sponsorPrizeService.add(sponsorId, prizeid, prizeCount);
    }

    /**
     * 新增活动与奖品关联
     * @param ativId     活动id
     * @param prizeId    奖品id
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
