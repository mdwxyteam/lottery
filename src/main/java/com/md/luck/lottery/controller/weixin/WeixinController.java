package com.md.luck.lottery.controller.weixin;

import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.BaseEntity;
import com.md.luck.lottery.common.entity.RequestBodyOrder;
import com.md.luck.lottery.common.entity.WeixnActiv;
import com.md.luck.lottery.common.entity.request.HelpGrab;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.common.util.RequestBodyCheckOrder;
import com.md.luck.lottery.controller.BaseController;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信端api
 */
@RestController
@RequestMapping("/weixin/api")
public class WeixinController extends BaseController {

    @GetMapping("/code")
    public ResponMsg getCode() {
        return weixinService.getCode();
    }
//    @GetMapping("/userInfo")
//    public ResponMsg getUserInfo(@RequestParam("code") String code,
//                              @RequestParam("state") String state) {
//        System.out.println(code);
//        System.out.println(state);
//        ResponMsg responMsg = weixinService.getToken(state, code);
//        return responMsg;
//    }

    /**
     * 获取所有正在售卖商品数据
     *
     * @return ResponMsg
     */
    @GetMapping("/goods/state")
    public ResponMsg queryAllGoods() {
        return goodsService.queryByStateWeixin();
    }

    /**
     * 获取活动信息
     * @param activType 活动类型 0:抽奖；1:表示抢
     * @param state     1:未结束；0结束；-1测试
     * @return ResponMsg
     */
    @GetMapping("/activ/grab")
    public ResponMsg queryWeixinActiv(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("activType") Integer activType, @RequestParam("state") Integer state) {
        return activService.queryWeixinActiv(pageNum, pageSize, activType, state);
    }

    /**
     * 查询上轮播的所有活动
     *
     * @return ResponMsg
     */
    @GetMapping("/activ/carousel")
    public ResponMsg queryByCarousel() {
        return activPrizeService.queryByCarousel(Cont.ONE);
    }

    /**
     * 通过活动id查询活动详情信息
     * @param activId activId
     * @return ResponMsg
     */
    @GetMapping("/activ/id")
    public ResponMsg queryByActivId(HttpServletRequest request, @RequestParam("activType") Integer activType, @RequestParam("state") Integer state, @RequestParam("activId") Long activId, @RequestParam("recordId") Long recordId) {
//        String openid = (String) request.getAttribute("openid");
        String openid = "ot6_Xvt80Txu5TWtgH7dklajeZ0s";
        return activService.queryByActivIdToWeixin(openid, activType, state, activId, recordId);
    }

    /**
     * 通过id查询
     * @param id 参与记录
     * @return ResponMsg
     */
    @GetMapping("/activityAddRecord/id")
    public ResponMsg queryActivityAddRecordById(@RequestParam("id") Long id) {
        return activityAddRecordService.queryById(id);
    }
    /**
     * 获奖者
     * @param activId
     * @return
     */
    @GetMapping("/lucker")
    public ResponMsg queryByLucker(@RequestParam("activId") Long activId) {
        return luckPeoService.queryLucker(activId);
    }
    /**
     * 参与抢 活动
     * @param request request获取用户openid
     * @param weixnActiv 活动
     * @return ResponMsg
     */
    @PostMapping("/commit/grab")
    public ResponMsg commitGrab(HttpServletRequest request, @RequestBody WeixnActiv weixnActiv){
//        String openid = (String) request.getAttribute("openid");
        String openid = "ot6_Xvt80Txu5TWtgH7dklajeZ0s";
        return activityAddRecordService.addGrabRecord(openid, weixnActiv.getId());
    }

    /**
     * 参与活动 抽奖
     * @param request request
     * @param weixnActiv weixnActiv
     * @return ResponMsg
     */
    @PostMapping("/commit/luck/activ")
    public ResponMsg commitActiv(HttpServletRequest request, @RequestBody WeixnActiv weixnActiv) {
//        String openid = (String) request.getAttribute("openid");
        String openid = "ot6_Xvt80Txu5TWtgH7dklajeZ0s";
        return luckRecordService.commitActiv(openid, weixnActiv.getId());
    }

    /**
     * 分页查询参与抽奖名单
     * @param request request
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param activId 活动id
     * @return ResponMsg
     */
    @GetMapping("/query/luckRecord/page")
    public ResponMsg queryAllCommitActiv(HttpServletRequest request, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("activId") Long activId) {
//        String openid = (String) request.getAttribute("openid");
        String openid = "ot6_Xvt80Txu5TWtgH7dklajeZ0s";
        return  luckRecordService.queryAllCommitActiv(openid, pageNum, pageSize, activId);
    }

    /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param activId 活动id
     * @return ResponMsg
     */
    @GetMapping("/query/commitGrabRecord")
    public ResponMsg queryAllCommitGrab(HttpServletRequest request, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("activId") Long activId) {
        String teamPlayerOpenid = (String) request.getAttribute("openid");
        return activityAddRecordService.queryAllCommitGrab(teamPlayerOpenid, pageNum, pageSize, activId);
    }

    /**
     * 分页查询助力记录
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param recordId 参与记录id
     * @return ResponMsg
     */
    @GetMapping("/culp/page")
    public ResponMsg queryCulpByPage( @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("recordId") Long recordId) {

        return castCulpService.queryPage(pageNum, pageSize, recordId);
    }

    /**
     * 助力
     * @return
     */
    @PostMapping("/culp")
    public ResponMsg help(HttpServletRequest request, @RequestBody HelpGrab helpGrab) {
//        String teamPlayerOpenid = (String) request.getAttribute("openid");
        String teamPlayerOpenid = "ot6_Xvl8aCuHNf8jQL9ljna6EFdo";
        return castCulpService.culp(teamPlayerOpenid, helpGrab);
    }
    /**
     * 通过openid获取用户数据
     * @param openid openid
     * @return ResponMsg
     */
    @GetMapping("/user/info")
    public ResponMsg getUserInfo(@RequestParam("openid") String openid) {
        return customerService.getUserInfo(openid);
    }

    /**
     * 创建订单前检测用户是否已经创建过
     * @param requestBodyCheckOrder requestBodyCheckOrder
     * @return ResponMsg
     */
    @PostMapping("/check/user/order")
    public ResponMsg checkOrder(HttpServletRequest request, @RequestBody RequestBodyCheckOrder requestBodyCheckOrder) {
        String openid = (String) request.getAttribute("openid");
        return orderService.checkOrder(openid, requestBodyCheckOrder);
    }
    /**
     * 创建订单
     *
     * @param requestBodyOrder 订单参数
     * @return ResponMsg
     */
    @PostMapping("/creat/order")
    public ResponMsg creatOrder(HttpServletRequest request, @RequestBody RequestBodyOrder requestBodyOrder) {
        String openid = (String) request.getAttribute("openid");
        return orderService.creatOrder(openid, requestBodyOrder);
    }


    /**
     * 查询参与记录
     * @param request request
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param activType activType
     * @param activType activType 0:抽奖；1:表示抢
     * @return ResponMsg
     */
    @GetMapping("/add/record/activ")
    public ResponMsg queryGrabRecord(HttpServletRequest request,@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("activType") Integer activType) {
        String openid = (String) request.getAttribute("openid");
//        String openid = "ot6_Xvt80Txu5TWtgH7dklajeZ0s";
        return activService.queryRecord(openid, pageNum, pageSize, activType);
    }
}
