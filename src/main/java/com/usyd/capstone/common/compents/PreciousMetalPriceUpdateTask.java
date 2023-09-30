package com.usyd.capstone.common.compents;

import com.alibaba.fastjson.JSONObject;
import com.usyd.capstone.common.DTO.MetalApiResponse;
import com.usyd.capstone.common.Enums.CATEGORY;
import com.usyd.capstone.common.Enums.PreciousMetalType;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.ProductPriceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.Map;


@Component
public class PreciousMetalPriceUpdateTask extends BaseTask {

    @Autowired
    private ProductPriceRecordMapper productPriceRecordMapper;

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = "https://metals-api.com/api/latest";

    @PostConstruct
    public void CryptoCurrencyPriceUpdateTask()
    {
        category = CATEGORY.PRECIOUS_METAL;
        initProductMap();
    }

    @Async
    @Scheduled(fixedRate = 1800000) // 每半小时执行一次，单位为毫秒
    public void updateCurrencyRates() throws IllegalAccessException {

        StringBuilder preciousMetalNames = new StringBuilder();
        if (!productMap.isEmpty()) {
            for(Map.Entry<String, Product> entry : productMap.entrySet())
            {
                preciousMetalNames.append(entry.getKey()).append(",");
            }
        }
        else
        {
            // 枚举获得需要拉取的值
            PreciousMetalType[] preciousMetalTypesUse = PreciousMetalType.values();
            for (PreciousMetalType metal : preciousMetalTypesUse) {
                preciousMetalNames.append(metal).append(",");
            }
        }

        preciousMetalNames.deleteCharAt(preciousMetalNames.length() - 1);
        String preciousMetalPriceUrl =
                apiUrl + "?access_key=" + SYSTEM_SECURITY_KEY.METALS_API_KEY.getValue();

        preciousMetalPriceUrl += "&base=USD";
        preciousMetalPriceUrl += "&symbols=" + preciousMetalNames;

        MetalApiResponse response = restTemplate.getForObject(preciousMetalPriceUrl, MetalApiResponse.class);
        // 将MetalApiResponse对象的rates字段转换为JSONObject
        JSONObject ratesObject = response.getRates();


        if(!productMap.isEmpty()) {
            ratesObject.keySet().forEach(key -> {
                if(key.length() >= 4) {
                    Product product = productMap.get(key.replace("USD", ""));

                    if (product.getProductUpdateTime() != response.getTimestamp() * 1000) {
                        insertARecord(product);
                        double priceNew = (Double) ratesObject.get(key);
                        updateProductInfo(priceNew, product, response.getTimestamp() * 1000);
                        productMapper.updateById(product);
                    }
                }
            });
        }
        else
        {
            // 枚举获得需要筛选存入数据库的值
            PreciousMetalType[] preciousMetalTypes = PreciousMetalType.values();
            for (PreciousMetalType metal : preciousMetalTypes) {
                // 从ratesObject中提取所需字段
                if (ratesObject.containsKey(metal.getTranerName())) {
                    Product product = new Product();
                    product.setCategory(2);
                    product.setCurrentTurnOfRecord(0);
                    product.setInResettingProcess(false);
                    product.setPriceStatus(2);
                    product.setProductCreateTime(response.getTimestamp() * 1000);
                    product.setProductDescription("This is " + metal.getExplain());
                    product.setProductName(metal.getExplain());
                    product.setProductPrice((Double) ratesObject.get(metal.getTranerName()));
                    product.setProductUpdateTime(response.getTimestamp() * 1000);
                    product.setNameForUpdateAPI(metal.name());

                    productMap.put(product.getNameForUpdateAPI(),product);
                    productMapper.insert(product);
                }
            }
        }
    }
}
