package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.ExchangeRateUsdMapper;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

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
    public IPage<Product> getProductListByCurrency(String targetCurrency, Page<Product> page) {
        IPage<Product> productListPage = productMapper.selectPage(page, new QueryWrapper<>());

        QueryWrapper<ExchangeRateUsd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exchange_name", targetCurrency)
                .orderByDesc("update_time")
                .last("LIMIT 1");
        ExchangeRateUsd exchangeRateUsd = exchangeRateUsdMapper.selectOne(queryWrapper);

        // modify the price by exchanged rate
        List<Product> productList = productListPage.getRecords();
        for (Product pp : productList) {
            double originalPrice = pp.getProductPrice();
            double exchangePrice = exchangeRateUsd.getExchangePrice() * originalPrice;
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            String formatted = decimalFormat.format(exchangePrice);
            pp.setProductExchangePrice(Double.parseDouble(formatted));
        }

        return productListPage;
    }
}
