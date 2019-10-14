package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Activ;
import com.md.luck.lottery.mapper.ActivMapper;
import com.md.luck.lottery.service.ActivService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }
}
