package com.george.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.user.domain.po.Address;
import com.george.user.mapper.AddressMapper;
import com.george.user.service.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
