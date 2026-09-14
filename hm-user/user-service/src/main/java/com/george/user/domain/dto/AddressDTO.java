package com.george.user.domain.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String province;
    private String city;
    private String town;
    private String mobile;
    private String street;
    private String contact;
    private Integer isDefault;
    private String notes;
}

