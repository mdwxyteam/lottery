package com.md.luck.lottery.service.impl;

import com.md.luck.lottery.common.ResponMsg;
import com.md.luck.lottery.common.entity.Customer;
import com.md.luck.lottery.common.util.MaObjUtil;
import com.md.luck.lottery.mapper.CustomerMapper;
import com.md.luck.lottery.service.CustomerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public ResponMsg getUserInfo(String openid) {
        if (MaObjUtil.isEmpty(openid)) {
            return ResponMsg.newFail(null).setMsg("缺省参数");
        }
        ResponMsg responMsg = null;
        try {
            Customer customer = customerMapper.queryByOpenid(openid);
            Customer cu = new Customer();
            cu.setNickName(customer.getNickName());
            cu.setIcon(customer.getIcon());
            responMsg = ResponMsg.newSuccess(cu);
        }catch (SqlSessionException e) {
            responMsg = ResponMsg.newFail(null).setMsg("操作异常");
            log.error(e.getMessage());
        }
        return responMsg;
    }
}
