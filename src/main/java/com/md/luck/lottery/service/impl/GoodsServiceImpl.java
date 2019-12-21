package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.common.entity.Goods;
import com.md.luck.lottery.mapper.GoodsMapper;
import com.md.luck.lottery.service.GoodsService;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public ResponMsg add(Goods goods) {
        if (goods.isEmpty()) {
            return ResponMsg.newFail(null).setMsg("缺省必要参数");
        }
        ResponMsg responMsg = null;
        try {
            goods.setState(Cont.SELL_READY);
            goods.setPayNum(0);
            goodsMapper.insert(goods);
            responMsg = ResponMsg.newSuccess(null);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("数据库操作失败");
        }
        return responMsg;
    }

    @Override
    public ResponMsg page(int pageNum, int pageSize, String goodsName, int state) {
        PageHelper.startPage(pageNum, pageSize);
        if (StrUtil.hasEmpty(goodsName)) {
            goodsName = null;
        }
        List<Goods> goods = goodsMapper.conditionPage(state, goodsName);
        PageInfo<Goods> pageInfo = new PageInfo<Goods>(goods);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg edit(Goods goods) {
        if (ObjectUtil.hasEmpty(goods) || ObjectUtil.hasEmpty(goods.getId())) {
            return ResponMsg.newFail(null).setMsg("缺省参数!");
        }
        ResponMsg responMsg = null;
        try {
            goodsMapper.edit(goods);
            responMsg = ResponMsg.newSuccess(null);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("数据库操作失败");
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryByState(int state) {
        if(ObjectUtil.hasEmpty(state)) {
            return ResponMsg.newFail(null).setMsg("缺少参数!");
        }
        ResponMsg responMsg = null;
        try {
            List<Goods> goods = goodsMapper.queryByState(state);
            responMsg = ResponMsg.newSuccess(goods);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("数据库操作失败");
        }
        return responMsg;
    }

    @Override
    public ResponMsg queryByStateWeixin() {
        ResponMsg responMsg = null;
        try {
            List<Goods> goods = goodsMapper.queryByStateWeixin(Cont.SELL);
            responMsg = ResponMsg.newSuccess(goods);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("数据库操作失败");
        }
        return responMsg;
    }

}
