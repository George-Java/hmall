package com.george.pay.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayOrderFormDTO {
    private Long id;
    private String pw;
}

