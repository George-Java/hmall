package com.george.item.domain.query;

import com.hmall.common.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemPageQuery extends PageQuery {
    private String key;
    private String category;
    private String brand;
    private Integer minPrice;
    private Integer maxPrice;
}

