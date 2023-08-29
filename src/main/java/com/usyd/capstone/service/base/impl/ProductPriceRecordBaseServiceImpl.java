package com.usyd.capstone.service.base.impl;

import com.github.dreamyoung.mprelation.ServiceImpl;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import com.usyd.capstone.service.base.ProductPriceRecordBaseService;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceRecordBaseServiceImpl  extends ServiceImpl<ProductPriceRecordMapper, ProductPriceRecord> implements ProductPriceRecordBaseService {
}
