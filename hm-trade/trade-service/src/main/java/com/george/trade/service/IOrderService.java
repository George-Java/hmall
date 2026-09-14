package com.george.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.trade.domain.dto.OrderFormDTO;
import com.george.trade.domain.po.Order;

public interface IOrderService extends IService<Order> {

    Long createOrder(OrderFormDTO orderFormDTO);

    void markOrderPaySuccess(Long orderId);
}
