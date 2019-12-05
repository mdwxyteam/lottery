package com.md.luck.lottery.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.md.luck.lottery.common.Cont;
import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.PayCode;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.config.MaSecurity;
import com.md.luck.lottery.mapper.PayCodeMapper;
import com.md.luck.lottery.service.PayCodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayCodeServiceImpl implements PayCodeService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private MaSecurity maSecurity;
    @Autowired
    private PayCodeMapper payCodeMapper;
    @Override
    public ResponMsg creatPayCode(Integer price) {
        if(MaObjUtil.isEmpty(price)) {
            return ResponMsg.newFail(null).setMsg("缺省参数!");
        }
        String cryptPrice = maSecurity.encrypt(price + "");
        String cocode = IdUtil.objectId();
        String pcode = maSecurity.encrypt(cocode);
        PayCode payCode = new PayCode();
        payCode.setActualPrice(cryptPrice);
        payCode.setPayCode(pcode);
        payCode.setStatus(Cont.ZERO);
        ResponMsg responMsg = null;
        try {
            payCodeMapper.insert(payCode);
            responMsg = ResponMsg.newSuccess(cocode);
        } catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作失败!");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
