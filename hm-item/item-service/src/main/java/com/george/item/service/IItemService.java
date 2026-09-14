package com.george.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.item.domain.po.Item;
import com.george.itemapi.dto.ItemDTO;
import com.george.itemapi.dto.OrderDetailDTO;

import java.util.Collection;
import java.util.List;

public interface IItemService extends IService<Item> {

    void deductStock(List<OrderDetailDTO> items);

    List<ItemDTO> queryItemByIds(Collection<Long> ids);
}
