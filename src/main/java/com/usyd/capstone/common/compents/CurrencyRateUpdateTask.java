package com.usyd.capstone.common.compents;

import com.usyd.capstone.common.DTO.ExchangeRateResponse;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.usyd.capstone.mapper.ExchangeRateUsdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CurrencyRateUpdateTask {

    @Autowired
    private ExchangeRateUsdMapper exchangeRateUsdMapper;

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = "http://api.currencylayer.com/live?access_key=c2823ae761b7da333b49c4a628786a0b&source=USD&currencies=";
    // 因为实时汇率接口高频率访问要收钱
    // 所以设置定时任务半小时一次同步进我们自己的数据库
//    @Scheduled(fixedRate = 1800000) // 每半小时执行一次，单位为毫秒
    public void updateCurrencyRates() {

        StringBuilder exchangeCurrency = new StringBuilder();
        exchangeCurrency.append("AUD,");
        exchangeCurrency.append("CHF,");
        exchangeCurrency.append("EUR,");
        exchangeCurrency.append("GBP,");
        exchangeCurrency.append("PLN");

        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentTime);
        String exchangeRateApiUrl = apiUrl+ exchangeCurrency;
        ExchangeRateResponse response = restTemplate.getForObject(exchangeRateApiUrl, ExchangeRateResponse.class);

        if (response == null){
            return;
        }
        Map<String, Double> rates = response.getQuotes();

        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            String currencyCode = entry.getKey();
            Double exchangeRate = entry.getValue();
            ExchangeRateUsd exchangeRateUsd = new ExchangeRateUsd();

            String targetCurrency = currencyCode.substring(3); // 从索引为3的位置开始截取

            exchangeRateUsd.setExchangeName(targetCurrency);
            exchangeRateUsd.setExchangePrice(exchangeRate);
            exchangeRateUsd.setUpdateTime(timestamp);
            exchangeRateUsdMapper.insert(exchangeRateUsd);
        }

    }
}
