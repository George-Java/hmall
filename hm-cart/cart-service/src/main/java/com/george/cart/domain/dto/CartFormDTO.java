package com.george.cart.domain.dto;

import lombok.Data;

@Data
public class CartFormDTO {
    private Long itemId;
    private String name;
    private String spec;
    private Integer price;
    private String image;
}
