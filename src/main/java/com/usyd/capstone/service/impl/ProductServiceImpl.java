package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.productAdmin;
import com.usyd.capstone.common.utils.pageUtil;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ExchangeRateUsdMapper;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import com.usyd.capstone.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    ProductPriceRecordMapper productPriceRecordMapper;

    @Override
    public IPage getProductListByCurrency(String targetCurrency,int pageNum, int pageSize) {
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());

        double exchangeRate = 1;

        if (!targetCurrency.equals("USD")){
            QueryWrapper<ExchangeRateUsd> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("exchange_name", targetCurrency)
                    .orderByDesc("update_time")
                    .last("LIMIT 1");
            ExchangeRateUsd exchangeRateUsd = exchangeRateUsdMapper.selectOne(queryWrapper);
            exchangeRate = exchangeRateUsd.getExchangePrice();
        }

        List<productAdmin> productListWithUpdates = new ArrayList<>();
        // modify the price by exchanged rate
        for (Product pp : productList) {
            productAdmin productAdmin = new productAdmin();

            List<Long> priceUpdateTime =new ArrayList<>();
            List<Double> priceUpdateRecord = new ArrayList<>();
            Long productID = pp.getId();
            // get the price record
            List<ProductPriceRecord> record =  productPriceRecordMapper.selectList(new QueryWrapper<ProductPriceRecord>().eq("product_id", productID));

            for (ProductPriceRecord aa : record){
                priceUpdateTime.add(aa.getRecordTimestamp());
                priceUpdateRecord.add(aa.getProductPrice());
            }

            double originalPrice = pp.getProductPrice();
            double exchangePrice = exchangeRate * originalPrice;
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            String formatted = decimalFormat.format(exchangePrice);
            pp.setProductExchangePrice(Double.parseDouble(formatted));

            productAdmin.setProduct(pp);
            productAdmin.setPriceUpdateRecord(priceUpdateRecord);
            productAdmin.setPriceUpdateTime(priceUpdateTime);
            productListWithUpdates.add(productAdmin);
        }
        IPage iPage = pageUtil.listToPage(productListWithUpdates, pageNum, pageSize);

        return iPage;
    }

}
