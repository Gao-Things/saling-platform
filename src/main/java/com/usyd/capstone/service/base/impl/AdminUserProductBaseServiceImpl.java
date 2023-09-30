package com.usyd.capstone.service.base.impl;

import com.github.dreamyoung.mprelation.ServiceImpl;
import com.usyd.capstone.entity.AdminUserProduct;
import com.usyd.capstone.mapper.AdminUserProductMapper;
import com.usyd.capstone.service.base.AdminUserProductBaseService;
import org.springframework.stereotype.Service;

@Service
public class AdminUserProductBaseServiceImpl extends ServiceImpl<AdminUserProductMapper, AdminUserProduct> implements AdminUserProductBaseService {
}
