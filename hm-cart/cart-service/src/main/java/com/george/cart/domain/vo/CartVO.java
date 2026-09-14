package com.george.cart.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartVO {
    private Long id;
    private Long itemId;
    private Integer num;
    private String name;
    private String spec;
    private Integer price;
    private Integer newPrice;
    private Integer status = 1;
    private Integer stock = 10;
    private String image;
    private LocalDateTime createTime;

}

