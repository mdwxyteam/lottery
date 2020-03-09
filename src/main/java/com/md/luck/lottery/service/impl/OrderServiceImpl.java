package com.md.luck.lottery.service.impl;

import cn.hutool.crypto.CryptoException;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.*;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.common.util.RequestBodyCheckOrder;
import com.md.luck.lottery.config.MaSecurity;
import com.md.luck.lottery.mapper.CustomerMapper;
import com.md.luck.lottery.mapper.GoodsMapper;
import com.md.luck.lottery.mapper.OrderMapper;
import com.md.luck.lottery.mapper.PayCodeMapper;
import com.md.luck.lottery.service.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Log log = LogFactory.getLog(OrderServiceImpl.class);
    @Autowired
    private MaSecurity maSecurity;
    @Autowired
    private PayCodeMapper payCodeMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Transactional(rollbackFor = {SqlSessionException.class, CryptoException.class})
    @Override
    public ResponMsg creatOrder(String oepnid, RequestBodyOrder requestBodyOrder) {
        if (MaObjUtil.hasEmpty(oepnid, requestBodyOrder.getId(), requestBodyOrder.getPayCode())) {
            return ResponMsg.newFail(null).setMsg("缺省参数!");
        }
        ResponMsg responMsg = null;
        try {
            // 加密paycode
            String paycodeCrypt = maSecurity.encrypt(requestBodyOrder.getPayCode());
            PayCode payCode = payCodeMapper.queryByPayCode(Cont.ZERO, paycodeCrypt);
            if (MaObjUtil.isEmpty(payCode)) {
                //无效的支付码
                responMsg = ResponMsg.newFail(null).setMsg("无效的拼go码");
                return responMsg;
            }
            LotteryOrder ordered = orderMapper.queryByOpenidAndGoodsId(requestBodyOrder.getId(), oepnid);
            if (!MaObjUtil.isEmpty(ordered)) {
                // 一个人只能购买一份同一件商品
                responMsg = ResponMsg.newFail(null).setMsg("订单已存在");
                return responMsg;
            }
            //查询商品情况，生成订单
            Goods goods = goodsMapper.queryByStateAndId(Cont.SELL, requestBodyOrder.getId());
            if (MaObjUtil.isEmpty(goods)) {
                //商品搜完
                responMsg = ResponMsg.newFail(null).setMsg("商品已售罄");
                return responMsg;
            }
            //1、匹配价格；2、更新商品和拼购码状态
            String strPrice = maSecurity.decrypt(payCode.getActualPrice());
            //检查strPrice是否数字
            if (maSecurity.isDigit(strPrice)) {
                int price = Integer.valueOf(strPrice);
                int goodsPrice = goods.getActualPrice();
                if (price == goodsPrice) {
                    //购买成功
                    //1、生成订单记录；2、判断商品状态，更新商品；3、更新拼go码状态
                    //  生成订单记录
                    Customer customer = customerMapper.queryByOpenid(oepnid);
                    LotteryOrder order = new LotteryOrder();
                    order.setGoodsName(goods.getGoodsName());
                    order.setGoodsId(requestBodyOrder.getId());
                    order.setNickName(customer.getNickName());
                    order.setOpenid(oepnid);
                    order.setOrderPrice(price);
                    order.setPayCodeId(payCode.getId());
                    order.setPayCode(payCode.getPayCode());
                    orderMapper.creatOrder(order);
                    //判断商品状态，更新商品；
                    Goods goods1 = new Goods();
                    goods1.setId(goods.getId());
                    int p = goods.getPrice();
                    int c = goods.getPayNum() + 1;
//                    int c = goods.getPayNum() + price;
                    goods1.setPayNum(c);
                    if (p <= c) {
                        //商品停止售卖，改变状态为售卖结束
                        goods1.setState(Cont.SELL_END);
                    }
                    goodsMapper.edit(goods1);

                    //更新拼go码状态
                    payCodeMapper.updatePayCodeStatus(Cont.NEGATIVE_ONE, payCode.getId());
                    Map<String, Object> reMap = new HashMap<>();
                    reMap.put("id", goods.getId());
                    reMap.put("payNum", goods1.getPayNum());
                    reMap.put("groupIcon", goods.getGroupIcon());
                    responMsg = ResponMsg.newSuccess(reMap);
                } else {
                    //拼go码与商品价格不匹配
                    responMsg = ResponMsg.newFail(null).setMsg("该拼go码不能够买此商品");
                    return responMsg;
                }
            }
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("服务器异常");
            log.error(e.getMessage());
        } catch (CryptoException e) {
            //解码失败
            responMsg = ResponMsg.newFail(null).setMsg("服务器异常");
            log.error(e.getMessage());
        }
        return responMsg;
    }

    @Override
    public ResponMsg checkOrder(String oepnid, RequestBodyCheckOrder requestBodyCheckOrder) {
        if (MaObjUtil.hasEmpty(requestBodyCheckOrder.getGoodsId(), oepnid)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            LotteryOrder lotteryOrder = orderMapper.queryByOpenidAndGoodsId(requestBodyCheckOrder.getGoodsId(), oepnid);
            Map<String, Object> reMap = new HashMap<>();
            if (MaObjUtil.isEmpty(lotteryOrder)) {
                reMap.put("checkResult", 0);
            } else {
                reMap.put("checkResult", 1);
                Goods goods = goodsMapper.queryById(requestBodyCheckOrder.getGoodsId());
                if (!MaObjUtil.isEmpty(goods)) {
                    reMap.put("id", goods.getId());
                    reMap.put("payNum", goods.getPayNum());
                    reMap.put("groupIcon", goods.getGroupIcon());
                }
            }
            responMsg = ResponMsg.newSuccess(reMap);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("服务器异常");
            log.error(e.getMessage());
        }
        return responMsg;
    }

}
