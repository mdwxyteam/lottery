package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Sponsor;
import com.md.luck.lottery.common.util.MaMathUtil;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.SponsorMapper;
import com.md.luck.lottery.service.SponsorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorServiceImpl implements SponsorService {
    private static final Log log = LogFactory.getLog(SponsorServiceImpl.class);
    @Autowired
    private SponsorMapper sponsorMapper;

    @Override
    public ResponMsg<Sponsor> detailById(Long id) {
        return ResponMsg.newSuccess(sponsorMapper.bySponsorId(id));
    }

    @Override
    public ResponMsg<Sponsor> makdawnById(Long id) {
        if (MaObjUtil.isEmpty(id)) {
            return ResponMsg.newFail(null).setMsg("缺省必要参数");
        }
        String html = sponsorMapper.queryDetailbySponsorId(id);
        return ResponMsg.newSuccess(html);
    }

    @Override
    public ResponMsg<List<Sponsor>> byType(int pageNum, int pageSize, Long typeId, String sponsorName, Integer status) {
        if (ObjectUtil.hasEmpty(status)) {
            return ResponMsg.newFail(null).setMsg("缺省必要参数!");
        }
        if (StrUtil.hasEmpty(sponsorName)) {
            sponsorName = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Sponsor> sponsorTypeList = sponsorMapper.byType(typeId, sponsorName, status);
        PageInfo<Sponsor> pageInfo = new PageInfo<Sponsor>(sponsorTypeList);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg<List<Sponsor>> page(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Sponsor> sponsorTypeList = sponsorMapper.all();
        PageInfo<Sponsor> pageInfo = new PageInfo<Sponsor>(sponsorTypeList);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg<Sponsor> add(String sponsor, String location, String address,
                                  String detalis, long typeId, String type, String markDown,
                                  String principal, String contact, String cover) {
        if (StrUtil.hasEmpty(sponsor, location, detalis, principal, contact, cover)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Sponsor sponsorObj = creatSponsor(sponsor, location, address, detalis, typeId, type, markDown, principal, contact, cover);
        sponsorObj.setStatus(Cont.ONE);
        Long id = MaMathUtil.creatId();
        sponsorObj.setId(id);
        int i = sponsorMapper.add(sponsorObj);
        if (i == Cont.ZERO) {
            return ResponMsg.newFail(null).setMsg("添加是失败！");
        }
        return ResponMsg.newSuccess(sponsorObj);
    }

    @Override
    public ResponMsg<Sponsor> update(long id, String sponsor, String location,
                                     String address, String detalis, long typeId, String type, String markDown,
                                     String principal, String contact, String cover) {
        if (StrUtil.hasEmpty(sponsor, location, detalis, principal, contact, cover)) {
            return ResponMsg.newFail(null).setMsg("参数异常!");
        }
        Sponsor sponsorObj = creatSponsor(sponsor, location, address, detalis, typeId, type, markDown, principal, contact, cover);
        sponsorObj.setId(id);
        int i = sponsorMapper.update(sponsorObj);
        if (i == Cont.ZERO) {
            return ResponMsg.newFail(null).setMsg("修改是失败！");
        }
        return ResponMsg.newSuccess(sponsorObj);
    }

    @Override
    public ResponMsg delByStatus(long id, int status) {
        boolean bl = false;
        try {
            int i = sponsorMapper.delByStatus(id, status);
            if (i == Cont.ZERO) {
                return ResponMsg.newFail(null).setMsg("修改是失败！");
            }
        }catch (SqlSessionException e) {
            bl = true;
            log.error(e.getMessage());
        }
        if (bl) {
            return ResponMsg.newFail(null).setMsg("修改是失败！");
        }
        return ResponMsg.newSuccess(null);
    }

    @Override
    public ResponMsg query(Sponsor sponsor) {
        if (ObjectUtil.hasEmpty(sponsor)) {
            return ResponMsg.newFail(null).setMsg("缺省必要参数!");
        }
        List<Sponsor> sponsors = sponsorMapper.query(sponsor);
        return ResponMsg.newSuccess(sponsors);
    }

    private Sponsor creatSponsor(String sponsor, String location,
                                 String address, String detalis, long typeId, String type, String markDown,
                                 String principal, String contact, String cover) {
        Sponsor sponsorObj = new Sponsor();
        sponsorObj.setType(type);
        sponsorObj.setTypeId(typeId);
        sponsorObj.setDetalis(detalis);
        sponsorObj.setLocation(location);
        sponsorObj.setSponsorName(sponsor);
        sponsorObj.setAddress(address);
        sponsorObj.setMarkDown(markDown);
        sponsorObj.setPrincipal(principal);
        sponsorObj.setContact(contact);
        sponsorObj.setCover(cover);
        return sponsorObj;
    }
}
