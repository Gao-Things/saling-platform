package com.usyd.capstone.common.compents;

import com.alibaba.fastjson.JSONObject;
import com.usyd.capstone.common.DTO.MetalApiResponse;
import com.usyd.capstone.common.Enums.CATEGORY;
import com.usyd.capstone.common.Enums.RareMetalType;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;


@Component
public class rareMetalPriceUpdateTask extends BaseTask {

    @Autowired
    private ProductPriceRecordMapper productPriceRecordMapper;

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = "https://metals-api.com/api/latest";

    @PostConstruct
    public void CryptoCurrencyPriceUpdateTask()
    {
        category = CATEGORY.RAREMETAL;
        initProductMap();
    }

    @Async
//    @Scheduled(fixedRate = 1800000) // 每半小时执行一次，单位为毫秒
    public void updateCurrencyRates() throws IllegalAccessException {

        StringBuilder cryptocurrency = new StringBuilder();
        // 枚举获得需要拉取的值
        RareMetalType[] rareMetalTypesUse = RareMetalType.values();
        for (RareMetalType metal : rareMetalTypesUse) {
            cryptocurrency.append(metal).append(",");
        }

        cryptocurrency.deleteCharAt(cryptocurrency.length() - 1);
        String cryptoCurrencyPriceUrl =
                apiUrl + "?access_key=" + SYSTEM_SECURITY_KEY.METALS_API_KEY.getValue();

        cryptoCurrencyPriceUrl += "&base=USD";
        cryptoCurrencyPriceUrl += "&symbols=" + cryptocurrency;

        MetalApiResponse response = restTemplate.getForObject(cryptoCurrencyPriceUrl, MetalApiResponse.class);
        // 将MetalApiResponse对象的rates字段转换为JSONObject
        JSONObject ratesObject = response.getRates();

        // 枚举获得需要筛选存入数据库的值
        RareMetalType[] rareMetalTypes = RareMetalType.values();
        for (RareMetalType metal : rareMetalTypes) {
            // 从ratesObject中提取所需字段
            if (ratesObject.containsKey(metal.getTranerName())) {

                if (productMap.isEmpty()){
                    Product product = new Product();
                    product.setCategory(2);
                    product.setCurrentTurnOfRecord(0);
                    product.setInResettingProcess(false);
                    product.setPriceStatus(2);
                    product.setProductCreateTime(response.getTimestamp());
                    product.setProductDescription("This is " + metal.getExplain());
                    product.setProductName(metal.getExplain());
                    product.setProductPrice((Double) ratesObject.get(metal.getTranerName()));
                    product.setProductUpdateTime(response.getTimestamp());

                    productMapper.insert(product);
                }else {
                    // get the product by product name
                    Product product = productMap.get(metal.getExplain());
                    ProductPriceRecord productPriceRecord = new ProductPriceRecord();
                    double priceOld = product.getProductPrice();
                    productPriceRecord.setProductId(product.getId());
                    productPriceRecord.setProductPrice(priceOld);
                    productPriceRecord.setTurnOfRecord(product.getCurrentTurnOfRecord());
                    productPriceRecord.setRecordTimestamp(product.getProductUpdateTime());
                    productPriceRecordMapper.insert(productPriceRecord);
                    double priceNew = (Double) ratesObject.get(metal.getTranerName());
                    if(priceOld < priceNew)
                        product.setPriceStatus(0);
                    else if(priceOld > priceNew)
                        product.setPriceStatus(1);
                    else
                        product.setPriceStatus(2);
                    product.setProductPrice(priceNew);
                    // 获取当前的时间戳
                    product.setProductUpdateTime(System.currentTimeMillis());
                    product.setCurrentTurnOfRecord(product.getCurrentTurnOfRecord() + 1);
                    productMapper.updateById(product);
                }

            }

        }

    }
}
