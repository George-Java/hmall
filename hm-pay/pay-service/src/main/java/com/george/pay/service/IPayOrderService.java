package com.george.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.pay.domain.dto.PayApplyDTO;
import com.george.pay.domain.dto.PayOrderFormDTO;
import com.george.pay.domain.po.PayOrder;

public interface IPayOrderService extends IService<PayOrder> {

    String applyPayOrder(PayApplyDTO applyDTO);

    void tryPayOrderByBalance(PayOrderFormDTO payOrderFormDTO);
}
