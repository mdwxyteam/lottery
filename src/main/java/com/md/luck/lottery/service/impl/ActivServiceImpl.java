package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.service.ActivService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivServiceImpl implements ActivService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ActivMapper activMapper;

    @Override
    public ResponMsg add(Activ activ) {
        if (ObjectUtil.hasEmpty(activ) || activ.isEmpty()) {
            return ResponMsg.newFail(null).setMsg("参数异常");
        }
        boolean ie = false;
        try {
            activMapper.add(activ);
        } catch (SqlSessionException e) {
            ie = true;
            log.error(e.getMessage());
        }
        if (ie) {
            return ResponMsg.newFail(null).setMsg("数据库操作异常");
        }
        return ResponMsg.newSuccess(activ);
    }

    @Override
    public ResponMsg conditionPage(int pageNum, int pageSize, int conditionType, String sponsorName) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activ> activs = activMapper.conditionPage(conditionType, sponsorName);
        PageInfo<Activ> pageInfo = new PageInfo<Activ>(activs);
        return ResponMsg.newSuccess(pageInfo);
    }

    @Override
    public ResponMsg updateDelState(long id, int delState) {
        boolean bl = false;
        try {
            int i = activMapper.updateDelState(id, delState);
            if (i == Cont.ZERO) {
                return ResponMsg.newFail(null).setMsg("修改失败！");
            }
        }catch (SqlSessionException e) {
            bl = true;
            log.error(e.getMessage());
        }
        if (bl) {
            return ResponMsg.newFail(null).setMsg("修改失败！");
        }
        return ResponMsg.newSuccess(null);
    }
}
