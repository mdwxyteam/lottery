package com.md.luck.lottery.service.impl;


import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.SponsorType;
import com.md.luck.lottery.mapper.SponsorTypeMapper;
import com.md.luck.lottery.service.SponsorTypeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private Log log = LogFactory.getLog(this.getClass());

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
    public ResponMsg<PageInfo<SponsorType>> page(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SponsorType> sponsorTypeList = sponsorTypeMapper.all();
        PageInfo<SponsorType> pageInfo = new PageInfo<SponsorType>(sponsorTypeList);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg status(Long typeId, int isStatus) {
        if(StrUtil.hasEmpty(String.valueOf(typeId))) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        SponsorType sponsorType = new SponsorType();
        sponsorType.setId(typeId);
        sponsorType.setIsStatus(isStatus);
        boolean isSponsorType = false;
        try {
            sponsorTypeMapper.updateStatus(sponsorType);
        } catch (SqlSessionException e) {
            isSponsorType = true;
            log.error(e.getMessage());
        }
        if(isSponsorType){
            return ResponMsg.newFail(null).setMsg("数据库操作正常！");
        }
        return ResponMsg.newSuccess(isSponsorType);
    }
}
