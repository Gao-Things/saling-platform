package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.productAdmin;
import com.usyd.capstone.common.VO.ProductFilter;
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
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

    @Override
    public Result getProductById(Integer productID) {
        productAdmin productAdmin = new productAdmin();

        List<Long> priceUpdateTime =new ArrayList<>();
        List<Double> priceUpdateRecord = new ArrayList<>();

        Product pp = productMapper.selectById(productID);
        // get the price record
        List<ProductPriceRecord> record =  productPriceRecordMapper.selectList(new QueryWrapper<ProductPriceRecord>().eq("product_id", productID));

        for (ProductPriceRecord aa : record){
            priceUpdateTime.add(aa.getRecordTimestamp());
            priceUpdateRecord.add(aa.getProductPrice());
        }

        productAdmin.setProduct(pp);
        productAdmin.setPriceUpdateTime(priceUpdateTime);
        productAdmin.setPriceUpdateRecord(priceUpdateRecord);
        return Result.suc(productAdmin);
    }

    @Override
    public List<ProductUserDTO> listProduct(ProductFilter productFilter) {
        return productMapper.listProduct(productFilter);
    }
    @Override
    public Result getProductListAfterFiltered(String filter1, Integer value1, String filter2, String value2) {
        List<Product> productListAfterFiltered = null;
        if(filter1 != null)
        {
            if(filter2 == null)
                productListAfterFiltered = productMapper.selectList(new QueryWrapper<Product>()
                        .eq(filter1, value1));
            else
                productListAfterFiltered = productMapper.selectList(new QueryWrapper<Product>()
                        .eq(filter1, value1)
                        .eq(filter2, value2));
        }

        if (productListAfterFiltered.isEmpty())
        {
            return Result.fail("invalid input or no product has been found");
        }
        else
        {
            return Result.suc(productListAfterFiltered);
        }
    }

    @Override
    public Result getTop10Products() {
        List<Product> top10Products = productMapper.selectList(new QueryWrapper<Product>()
                .eq("product_status", 0)
                .orderByDesc("search_count")
                .last("LIMIT 10"));
        return Result.suc(top10Products);
    }

}
