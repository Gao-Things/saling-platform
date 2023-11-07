package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.StatisticsData;
import com.usyd.capstone.common.DTO.productAdmin;
import com.usyd.capstone.common.VO.ProductFilter;
import com.usyd.capstone.common.utils.pageUtil;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ExchangeRateUsdMapper;
import com.usyd.capstone.mapper.OfferMapper;
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
    OfferMapper offerMapper;

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

    @Override
    public double getMinWeight() {

        // 假设Product类中有一个price字段来表示价格
        // 使用QueryWrapper来构建查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("product_weight"); // 按照价格升序排序
        queryWrapper.last("LIMIT 1"); // 限制结果只有一个，即最低价格的商品

        // 执行查询，selectOne方法会返回查询结果中的第一个对象
        Product product = productMapper.selectOne(queryWrapper);

        // 检查product是否为null，以防数据库为空或查询出错
        if (product != null) {
            return product.getProductWeight(); // 返回找到的商品的价格
        } else {
            return 0; // 如果没有找到商品，则返回0或者抛出一个异常
        }
    }

    @Override
    public double getMaxWeight() {

        // 假设Product类中有一个price字段来表示价格
        // 使用QueryWrapper来构建查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("product_weight");
        queryWrapper.last("LIMIT 1"); // 限制结果只有一个，即最高重量的商品

        // 执行查询，selectOne方法会返回查询结果中的第一个对象
        Product product = productMapper.selectOne(queryWrapper);

        // 检查product是否为null，以防数据库为空或查询出错
        if (product != null) {
            return product.getProductWeight(); // 返回找到的商品的价格
        } else {
            return 0; // 如果没有找到商品，则返回0或者抛出一个异常
        }
    }

    @Override
    public Page<Product> getProductListAndOffer( Integer pageNum,
                                                 Integer pageSize,
                                                 String searchValue) {

        Page<Product> page = new Page<>(pageNum, pageSize);
        return productMapper.selectProductsWithOffers(page, searchValue);
    }

    @Override
    public List<Product> getProductListByUserID(Integer userId, boolean isSelling) {

        return productMapper.getProductListByUserID(userId, isSelling);
    }

    @Override
    public Page<Offer> getOfferListAdmin(Integer pageNum,
                                         Integer pageSize,
                                         Integer productId,
                                         String searchValue) {

        Page<Offer> page = new Page<>(pageNum, pageSize);

        return offerMapper.PageOffersByProductId(page, productId, searchValue);
    }

    @Override
    public List<StatisticsData> productStatistic(Integer category) {

        return productMapper.productStatistic(category);
    }

    @Override
    public List<StatisticsData> getHotProductStatistic() {
        return productMapper.getHotProductStatistic();
    }


}
