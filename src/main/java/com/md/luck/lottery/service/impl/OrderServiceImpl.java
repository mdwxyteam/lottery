package com.md.luck.lottery.service.impl;

import cn.hutool.crypto.CryptoException;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Goods;
import com.md.luck.lottery.common.entity.PayCode;
import com.md.luck.lottery.common.entity.RequestBodyOrder;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.config.MaSecurity;
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

@Service
public class OrderServiceImpl implements OrderService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private MaSecurity maSecurity;
    @Autowired
    private PayCodeMapper payCodeMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Transactional(rollbackFor = {SqlSessionException.class, CryptoException.class})
    @Override
    public ResponMsg creatOrder(RequestBodyOrder requestBodyOrder) {
        if(MaObjUtil.hasEmpty(requestBodyOrder.getId(), requestBodyOrder.getPayCode())) {
            return ResponMsg.newFail(null).setMsg("缺省参数!");
        }
        ResponMsg responMsg = null;
        try{
            // 加密paycode
            String paycodeCrypt = maSecurity.encrypt(requestBodyOrder.getPayCode());
            PayCode payCode = payCodeMapper.queryByPayCode(Cont.ZERO, paycodeCrypt);
            if (MaObjUtil.isEmpty(payCode)) {
                //无效的支付码
                responMsg = ResponMsg.newFail(null).setMsg("无效的拼go码");
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
                    // todo 生成订单记录
                    //判断商品状态，更新商品；
                    Goods goods1 = new Goods();
                    goods1.setId(goods.getId());
                    int p = goods.getPrice();
                    int c = goods.getPayNum() + price;
                    goods1.setPayNum(c);
                    if (p == c) {
                        //商品停止售卖，改变状态为售卖结束
                        goods1.setState(Cont.SELL_END);
                    }
                    goodsMapper.edit(goods1);

                    //更新拼go码状态
                    payCodeMapper.updatePayCodeStatus(Cont.NEGATIVE_ONE, payCode.getId());
                    responMsg = ResponMsg.newSuccess(null);
                } else {
                    //拼go码与商品价格不匹配
                    responMsg = ResponMsg.newFail(null).setMsg("该拼go码不能够买此商品");
                    return responMsg;
                }
            }
        } catch (SqlSessionException e) {
            log.error(e.getMessage());
        }catch (CryptoException e) {
            //解码失败
            responMsg = ResponMsg.newFail(null).setMsg("服务器异常");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
