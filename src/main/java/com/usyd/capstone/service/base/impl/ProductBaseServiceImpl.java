package com.usyd.capstone.service.base.impl;

import com.github.dreamyoung.mprelation.ServiceImpl;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.service.base.ProductBaseService;
import org.springframework.stereotype.Service;

@Service
public class ProductBaseServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductBaseService {
}
