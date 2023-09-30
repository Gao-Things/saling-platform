package com.usyd.capstone.common.compents;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.CryptoCurrencyInfo;
import com.usyd.capstone.common.Enums.CATEGORY;
import com.usyd.capstone.common.Enums.CryptoCurrencyType;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.entity.PriceThreshold;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CryptoCurrencyPriceUpdateTask extends BaseTask {


    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = "https://data.mifengcha.com/api/v3/price?slug=";

    @PostConstruct
    public void CryptoCurrencyPriceUpdateTask()
    {
        category = CATEGORY.CRYPTOCURRENCY;
        initProductMap();
    }

    @Async
//    @Scheduled(fixedRate = 1800000) // 每半小时执行一次，单位为毫秒
    public void updateCurrencyRates() {

        StringBuilder cryptocurrencyNames = new StringBuilder();
        if (!productMap.isEmpty()){
            for(Map.Entry<String, Product> entry : productMap.entrySet())
            {
                cryptocurrencyNames.append(entry.getKey()).append(",");
            }
        }else {
            // 枚举获得需要拉取的值
            CryptoCurrencyType[] currencies = CryptoCurrencyType.values();
            for (CryptoCurrencyType currency : currencies) {
                cryptocurrencyNames.append(currency).append(",");
            }
        }

        cryptocurrencyNames.deleteCharAt(cryptocurrencyNames.length() - 1);
        String cryptoCurrencyPriceUrl = apiUrl + cryptocurrencyNames + "&api_key=" + SYSTEM_SECURITY_KEY.CRYPTO_CURRENCY_API_KEY.getValue();
        ResponseEntity<List<CryptoCurrencyInfo>> response = restTemplate.exchange(
                cryptoCurrencyPriceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CryptoCurrencyInfo>>() {}
        );


        List<CryptoCurrencyInfo> cryptoCurrencyInfoList = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            cryptoCurrencyInfoList = response.getBody();
        } else {
            // 处理错误
            return;
        }

        if (!productMap.isEmpty()){
            for(CryptoCurrencyInfo cryptoCurrencyInfo : cryptoCurrencyInfoList)
            {
                Product product = productMap.get(cryptoCurrencyInfo.getName());
                insertARecord(product);
                double priceNew = cryptoCurrencyInfo.getPrice();
                updateProductInfo(priceNew, product, cryptoCurrencyInfo.getTimestamp());
                productMapper.updateById(product);
                priceMap.put(product.getId(), product.getProductPrice());
            }
        }
        else
        {
            for(CryptoCurrencyInfo cryptoCurrencyInfo : cryptoCurrencyInfoList)
            {
                Product product = createProduct(cryptoCurrencyInfo);
                productMap.put(product.getNameForUpdateAPI(), product);
                productMapper.insert(product);
                priceMap.put(product.getId(), product.getProductPrice());
            }
        }

        List<PriceThreshold> priceThresholdList = priceThresholdMapper.selectList(new QueryWrapper<PriceThreshold>()
                .in("product_id", priceMap.keySet()));

        for(PriceThreshold priceThreshold : priceThresholdList)
        {
            if (priceThreshold.isMinimum()
                    && priceThreshold.getThreshold() >= priceMap.get(priceThreshold.getProductId())
                    && !priceThreshold.isReached())
            {
                priceThreshold.setReached(true);
                priceThresholdMapper.updateById(priceThreshold);
            }
            else if (priceThreshold.isMinimum()
                    && priceThreshold.getThreshold() < priceMap.get(priceThreshold.getProductId())
                    && priceThreshold.isReached())
            {
                priceThreshold.setReached(false);
                priceThresholdMapper.updateById(priceThreshold);
            }
            else if (!priceThreshold.isMinimum()
                    && priceThreshold.getThreshold() <= priceMap.get(priceThreshold.getProductId())
                    && !priceThreshold.isReached())
            {
                priceThreshold.setReached(true);
                priceThresholdMapper.updateById(priceThreshold);
            }
            else if (!priceThreshold.isMinimum()
                    && priceThreshold.getThreshold() < priceMap.get(priceThreshold.getProductId())
                    && priceThreshold.isReached())
            {
                priceThreshold.setReached(false);
                priceThresholdMapper.updateById(priceThreshold);
            }
        }
    }

    private static Product createProduct(CryptoCurrencyInfo cryptoCurrencyInfo) {
        double priceNew = cryptoCurrencyInfo.getPrice();
        Product product = new Product();
        product.setCategory(CATEGORY.CRYPTOCURRENCY.getValue());
        product.setCurrentTurnOfRecord(0);
        product.setInResettingProcess(false);
        product.setPriceStatus(2);
        product.setProductCreateTime(cryptoCurrencyInfo.getTimestamp());
        product.setProductDescription("This is " + cryptoCurrencyInfo.getName());
        product.setProductName(cryptoCurrencyInfo.getName());
        product.setProductPrice(priceNew);
        product.setProductUpdateTime(cryptoCurrencyInfo.getTimestamp());
        product.setNameForUpdateAPI(cryptoCurrencyInfo.getName());
        return product;
    }
}
