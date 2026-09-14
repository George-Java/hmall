package com.george.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.george.item.domain.po.Item;
import com.george.itemapi.dto.OrderDetailDTO;
import org.apache.ibatis.annotations.Update;

public interface ItemMapper extends BaseMapper<Item> {

    @Update("UPDATE item SET stock = stock - #{num} WHERE id = #{itemId}")
    void updateStock(OrderDetailDTO orderDetail);
}
