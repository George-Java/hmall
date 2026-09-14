package com.george.trade.domain.dto;

import com.george.itemapi.dto.OrderDetailDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderFormDTO {
    private Long addressId;
    private Integer paymentType;
    private List<OrderDetailDTO> details;
}

