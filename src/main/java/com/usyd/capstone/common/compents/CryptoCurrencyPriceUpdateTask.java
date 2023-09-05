package com.usyd.capstone.common.compents;

import com.usyd.capstone.common.DTO.CryptoCurrencyInfo;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CryptoCurrencyPriceUpdateTask {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductPriceRecordMapper productPriceRecordMapper;

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = "https://data.mifengcha.com/api/v3/price?slug=";

    private HashMap<String, Product> productMap = new HashMap<>();
    @PostConstruct
    public void initProductMap()
    {
        List<Product> tempList = productMapper.selectList(null);
        for(Product temp : tempList)
            productMap.put(temp.getProductName().toLowerCase(), temp);
    }
    @Scheduled(fixedRate = 1800000) // 每半小时执行一次，单位为毫秒
    public void updateCurrencyRates() {

        StringBuilder exchangeCurrency = new StringBuilder();
        for(Map.Entry<String, Product> entry : productMap.entrySet())
        {
            exchangeCurrency.append(entry.getKey() + ",");
        }
        exchangeCurrency.deleteCharAt(exchangeCurrency.length() - 1);
        String cryptoCurrencyPriceUrl = apiUrl + exchangeCurrency + "&api_key=" + SYSTEM_SECURITY_KEY.CRYPTO_CURRENCY_API_KEY.getValue();
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
            product.setProductUpdateTime(cryptoCurrencyInfo.getTimestamp());
            product.setCurrentTurnOfRecord(product.getCurrentTurnOfRecord() + 1);
            productMapper.updateById(product);
        }
    }
}
