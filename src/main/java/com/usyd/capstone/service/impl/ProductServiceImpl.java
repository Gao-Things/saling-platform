package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.ExchangeRateResponse;
import com.usyd.capstone.common.DTO.productDetailWithPrice;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.mapper.ExchangeRateUsdMapper;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ExchangeRateUsdMapper exchangeRateUsdMapper;

    @Override
    public List<Product> getProductListByCurrency(String targetCurrency) {
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
        QueryWrapper<ExchangeRateUsd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exchange_name", targetCurrency)
                .orderByDesc("update_time")
                .last("LIMIT 1");

        ExchangeRateUsd exchangeRateUsd =  exchangeRateUsdMapper.selectOne(queryWrapper);

        for (Product pp: productList){
            double originalPrice =  pp.getProductPrice();
            pp.setProductPrice(exchangeRateUsd.getExchangePrice() * originalPrice);
        }
        return productList;
    }
}
