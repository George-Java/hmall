package com.george.user.controller;


import com.george.user.domain.dto.AddressDTO;
import com.george.user.domain.po.Address;
import com.george.user.service.IAddressService;
import com.hmall.common.exception.BadRequestException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.common.utils.CollUtils;
import com.hmall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @GetMapping("{addressId}")
    public AddressDTO findAddressById(@PathVariable("addressId") Long id) {
        // 1.鏍规嵁id鏌ヨ
        Address address = addressService.getById(id);
        // 2.鍒ゆ柇褰撳墠鐢ㄦ埛
        Long userId = UserContext.getUser();
        if (!address.getUserId().equals(userId)) {
            throw new BadRequestException("address does not belong to current user");
        }
        return BeanUtils.copyBean(address, AddressDTO.class);
    }

    @GetMapping
    public List<AddressDTO> findMyAddresses() {
        // 1.鏌ヨ鍒楄〃
        List<Address> list = addressService.query().eq("user_id", UserContext.getUser()).list();
        // 2.鍒ょ┖
        if (CollUtils.isEmpty(list)) {
            return CollUtils.emptyList();
        }
        // 3.杞瑅o
        return BeanUtils.copyList(list, AddressDTO.class);
    }
}

