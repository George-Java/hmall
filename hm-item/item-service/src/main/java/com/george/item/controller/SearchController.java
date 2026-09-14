package com.george.item.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.george.item.domain.po.Item;
import com.george.item.domain.query.ItemPageQuery;
import com.george.item.service.IItemService;
import com.george.itemapi.dto.ItemDTO;
import com.hmall.common.domain.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final IItemService itemService;

    @GetMapping("/list")
    public PageDTO<ItemDTO> search(ItemPageQuery query) {
        // 鍒嗛〉鏌ヨ
        Page<Item> result = itemService.lambdaQuery()
                .like(StrUtil.isNotBlank(query.getKey()), Item::getName, query.getKey())
                .eq(StrUtil.isNotBlank(query.getBrand()), Item::getBrand, query.getBrand())
                .eq(StrUtil.isNotBlank(query.getCategory()), Item::getCategory, query.getCategory())
                .eq(Item::getStatus, 1)
                .between(query.getMaxPrice() != null, Item::getPrice, query.getMinPrice(), query.getMaxPrice())
                .page(query.toMpPage("update_time", false));
        // 灏佽骞惰繑鍥?
        return PageDTO.of(result, ItemDTO.class);
    }
}

