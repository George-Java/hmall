package com.george.item.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.george.item.domain.po.Item;
import com.george.item.service.IItemService;
import com.george.itemapi.dto.ItemDTO;
import com.george.itemapi.dto.OrderDetailDTO;
import com.hmall.common.domain.PageDTO;
import com.hmall.common.domain.PageQuery;
import com.hmall.common.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @GetMapping("/page")
    public PageDTO<ItemDTO> queryItemByPage(PageQuery query) {
        Page<Item> result = itemService.page(query.toMpPage("update_time", false));
        return PageDTO.of(result, ItemDTO.class);
    }

    @GetMapping
    public List<ItemDTO> queryItemByIds(@RequestParam("ids") List<Long> ids) {
        return itemService.queryItemByIds(ids);
    }

    @GetMapping("{id}")
    public ItemDTO queryItemById(@PathVariable("id") Long id) {
        return BeanUtils.copyBean(itemService.getById(id), ItemDTO.class);
    }

    @PostMapping
    public void saveItem(@RequestBody ItemDTO item) {
        // 鏂板
        itemService.save(BeanUtils.copyBean(item, Item.class));
    }

    @PutMapping("/status/{id}/{status}")
    public void updateItemStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        Item item = new Item();
        item.setId(id);
        item.setStatus(status);
        itemService.updateById(item);
    }

    @PutMapping
    public void updateItem(@RequestBody ItemDTO item) {
        // 涓嶅厑璁镐慨鏀瑰晢鍝佺姸鎬侊紝鎵€浠ュ己鍒惰缃负null锛屾洿鏂版椂锛屽氨浼氬拷鐣ヨ瀛楁
        item.setStatus(null);
        // 鏇存柊
        itemService.updateById(BeanUtils.copyBean(item, Item.class));
    }

    @DeleteMapping("{id}")
    public void deleteItemById(@PathVariable("id") Long id) {
        itemService.removeById(id);
    }

    @PutMapping("/stock/deduct")
    public void deductStock(@RequestBody List<OrderDetailDTO> items) {
        itemService.deductStock(items);
    }
}

