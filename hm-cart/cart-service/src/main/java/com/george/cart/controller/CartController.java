package com.george.cart.controller;

import com.george.cart.domain.dto.CartFormDTO;
import com.george.cart.domain.po.Cart;
import com.george.cart.domain.vo.CartVO;
import com.george.cart.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @PostMapping
    public void addItem2Cart(@Valid @RequestBody CartFormDTO cartFormDTO) {
        cartService.addItem2Cart(cartFormDTO);
    }

    @PutMapping
    public void updateCart(@RequestBody Cart cart) {
        cartService.updateById(cart);
    }

    @DeleteMapping("{id}")
    public void deleteCartItem(@Param("id") @PathVariable("id") Long id) {
        cartService.removeById(id);
    }

    @GetMapping
    public List<CartVO> queryMyCarts() {
        return cartService.queryMyCarts();
    }

    @DeleteMapping
    public void deleteCartItemByIds(@RequestParam("ids") List<Long> ids) {
        cartService.removeByItemIds(ids);
    }
}
