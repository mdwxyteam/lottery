package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Prize;
import com.md.luck.lottery.mapper.PrizeMapper;
import com.md.luck.lottery.service.PrizeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private PrizeMapper prizeMapper;

    @Override
    public ResponMsg<Prize> add(String prizeDescription, String iconUrl, int prizeCount) {
        if (StrUtil.hasEmpty(prizeDescription, iconUrl)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Prize prize = new Prize();
        prize.setPrizeDescription(prizeDescription);
        prize.setIconUrl(iconUrl);
        prize.setPrizeCount(prizeCount);
        boolean isPrize = false;
        try {
            prizeMapper.add(prize);
        } catch (SqlSessionException e) {
            isPrize = true;
            log.error(e.getMessage());
        }
        if(isPrize){
            return ResponMsg.newFail(null).setMsg("数据库操作正常！");
        }
           return ResponMsg.newSuccess(isPrize);
    }

    @Override
    public ResponMsg<Prize> edit(Long prizeId, String prizeDescription, String iconUrl, int prizeCount) {
        if (StrUtil.hasEmpty(prizeDescription, iconUrl)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Prize prize = new Prize();
        prize.setId(prizeId);
        prize.setPrizeDescription(prizeDescription);
        prize.setIconUrl(iconUrl);
        prize.setPrizeCount(prizeCount);
        boolean isPrize = false;
        try {
            prizeMapper.update(prize);
        } catch (SqlSessionException e) {
            isPrize = true;
            log.error(e.getMessage());
        }
        if(isPrize){
            return ResponMsg.newFail(null).setMsg("数据库操作正常！");
        }
        return ResponMsg.newSuccess(isPrize);
    }

    @Override
    public ResponMsg<PageInfo<Prize>> page(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Prize> sponsorTypeList = prizeMapper.all();
        PageInfo<Prize> pageInfo = new PageInfo<Prize>(sponsorTypeList);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg<Prize> del(Long prizeId) {
       if(StrUtil.hasEmpty(String.valueOf(prizeId))) {
           return ResponMsg.newFail(null).setMsg("参数异常!");
       }
        Prize prize = new Prize();
        prize.setId(prizeId);
        prize.setIsDelete(1);
        boolean isPrize = false;
        try {
            prizeMapper.updateDel(prize);
        } catch (SqlSessionException e) {
            isPrize = true;
            log.error(e.getMessage());
        }
         if(isPrize){
            return ResponMsg.newFail(null).setMsg("数据库操作正常！");
         }
        return ResponMsg.newSuccess(isPrize);
    }
}
