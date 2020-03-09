package com.md.luck.lottery.service.impl;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.LuckPeo;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.LuckProMapper;
import com.md.luck.lottery.service.LuckPeoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LuckPeoServiceImpl implements LuckPeoService {
    private static final Log log = LogFactory.getLog(LuckPeoServiceImpl.class);
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    LuckProMapper luckProMapper;

    @Override
    public ResponMsg queryLucker(Long activId) {
        if (MaObjUtil.isEmpty(activId)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            List<LuckPeo> luckPeos = redisService.getLuckPro(activId);
            if(MaObjUtil.isEmpty(luckPeos)) {
                luckPeos = luckProMapper.queryByActivId(activId);
            }
            responMsg = ResponMsg.newSuccess(luckPeos);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
