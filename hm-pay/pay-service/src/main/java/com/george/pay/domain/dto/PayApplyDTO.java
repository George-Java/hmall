package com.george.pay.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayApplyDTO {
    private Long bizOrderNo;
    private Integer amount;
    private String payChannelCode;
    private Integer payType;
    private String orderInfo;
}

