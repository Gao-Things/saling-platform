package com.usyd.capstone.common.compents;

import com.usyd.capstone.common.DTO.CryptoCurrencyInfo;
import com.usyd.capstone.common.Enums.CATEGORY;
import com.usyd.capstone.common.Enums.CryptoCurrency;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CryptoCurrencyPriceUpdateTask extends BaseTask {

    @Autowired
    private ProductPriceRecordMapper productPriceRecordMapper;

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
    @Scheduled(fixedRate = 1800000) // 每半小时执行一次，单位为毫秒
    public void updateCurrencyRates() {

        StringBuilder cryptocurrency = new StringBuilder();
        if (!productMap.isEmpty()){
            for(Map.Entry<String, Product> entry : productMap.entrySet())
            {
                cryptocurrency.append(entry.getKey()).append(",");
            }
        }else {
            // 枚举获得需要拉取的值
            CryptoCurrency[] currencies = CryptoCurrency.values();
            for (CryptoCurrency currency : currencies) {
                cryptocurrency.append(currency).append(",");
            }
        }

        cryptocurrency.deleteCharAt(cryptocurrency.length() - 1);
        String cryptoCurrencyPriceUrl = apiUrl + cryptocurrency + "&api_key=" + SYSTEM_SECURITY_KEY.CRYPTO_CURRENCY_API_KEY.getValue();
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

        for(CryptoCurrencyInfo cryptoCurrencyInfo : cryptoCurrencyInfoList)
        {
            if (!productMap.isEmpty()){
                Product product = productMap.get(cryptoCurrencyInfo.getName());
                ProductPriceRecord productPriceRecord = new ProductPriceRecord();

                double priceOld = product.getProductPrice();
                productPriceRecord.setProductId(product.getId());
                productPriceRecord.setProductPrice(priceOld);
                productPriceRecord.setTurnOfRecord(product.getCurrentTurnOfRecord());
                productPriceRecord.setRecordTimestamp(product.getProductUpdateTime());
                productPriceRecordMapper.insert(productPriceRecord);

                double priceNew = cryptoCurrencyInfo.getPrice();
                if(priceOld < priceNew)
                    product.setPriceStatus(0);
                else if(priceOld > priceNew)
                    product.setPriceStatus(1);
                else
                    product.setPriceStatus(2);
                product.setProductPrice(priceNew);
                // 设置为当前的时间戳
                product.setProductUpdateTime(System.currentTimeMillis());
                product.setCurrentTurnOfRecord(product.getCurrentTurnOfRecord() + 1);
                productMapper.updateById(product);
            }else {
                // insert new product record
                double priceNew = cryptoCurrencyInfo.getPrice();
                Product product = new Product();
                product.setCategory(1);
                product.setCurrentTurnOfRecord(0);
                product.setInResettingProcess(false);
                product.setPriceStatus(2);
                product.setProductCreateTime(cryptoCurrencyInfo.getTimestamp());
                product.setProductDescription("This is " + cryptoCurrencyInfo.getName());
                product.setProductName(cryptoCurrencyInfo.getName());
                product.setProductPrice(priceNew);
                product.setProductUpdateTime(cryptoCurrencyInfo.getTimestamp());

                productMapper.insert(product);
            }


        }
    }
}
