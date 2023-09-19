package com.usyd.capstone.common.compents;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.Enums.CATEGORY;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.PriceThresholdMapper;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public abstract class BaseTask {

    @Autowired
    protected ProductMapper productMapper;

    @Autowired
    private ProductPriceRecordMapper productPriceRecordMapper;

    @Autowired
    protected PriceThresholdMapper priceThresholdMapper;

    protected HashMap<String, Product> productMap = new HashMap<>();

    protected HashMap<Long, Double> priceMap = new HashMap<>();

    protected CATEGORY category;
    public void initProductMap()
    {
        System.out.println(category);
        List<Product> tempList = productMapper.selectList(new QueryWrapper<Product>()
                .eq("category", category.getValue()));
        if(tempList != null) {
            for (Product temp : tempList)
                productMap.put(temp.getNameForUpdateAPI(), temp);
        }
    }
    public void modifyProductMap(int caseNum, Product product)
    {
        switch (caseNum){
            case 1:
                product.getProductName().toLowerCase();
        }
    }

    protected void insertARecord(Product product){

        ProductPriceRecord productPriceRecord = new ProductPriceRecord();
        productPriceRecord.setProductId(product.getId());
        productPriceRecord.setProductPrice(product.getProductPrice());
        productPriceRecord.setTurnOfRecord(product.getCurrentTurnOfRecord());
        productPriceRecord.setRecordTimestamp(product.getProductUpdateTime());
        productPriceRecordMapper.insert(productPriceRecord);
    }

    protected void updateProductInfo(double priceNew, Product product, long timestampNew)
    {
        double priceOld = product.getProductPrice();
        if(priceOld < priceNew)
            product.setPriceStatus(0);
        else if(priceOld > priceNew)
            product.setPriceStatus(1);
        else
            product.setPriceStatus(2);
        product.setProductPrice(priceNew);
        product.setProductUpdateTime(timestampNew);
        product.setCurrentTurnOfRecord(product.getCurrentTurnOfRecord() + 1);

    }
}
