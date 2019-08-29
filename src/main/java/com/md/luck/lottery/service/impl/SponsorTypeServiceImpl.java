package com.md.luck.lottery.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.SponsorType;
import com.md.luck.lottery.mapper.SponsorTypeMapper;
import com.md.luck.lottery.service.SponsorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: SponsorTypeServiceImpl
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/8/29 11:10
 */
@Service
public class SponsorTypeServiceImpl implements SponsorTypeService {
    @Autowired
    private SponsorTypeMapper sponsorTypeMapper;

    @Override
    public ResponMsg add(String type) {
        if (StrUtil.isEmpty(type)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        SponsorType sponsorType = new SponsorType();
        sponsorType.setTypeName(type);
        sponsorTypeMapper.add(sponsorType);
        return ResponMsg.newSuccess(null);
    }

    @Override
    public ResponMsg<List<Page<SponsorType>>> page(int pageNum, int pageSize) {
        Page<SponsorType> page = PageHelper.startPage(pageNum, pageSize);
        sponsorTypeMapper.all();
        return ResponMsg.newSuccess(page);
    }
}
