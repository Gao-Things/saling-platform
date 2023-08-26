package com.usyd.capstone.controller;


import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.productDetailWithPrice;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.service.ExchangeRateUsdService;
import com.usyd.capstone.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@RestController
@RequestMapping("/public/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ExchangeRateUsdService exchangeRateUsdService;

    @GetMapping("/currencyList")
    public Result getCurrencyList(){
        return exchangeRateUsdService.getNewestCurrency();
    }

    @GetMapping("/productList")
    public Result getProductsWithPrices(@RequestParam(required = false) String targetCurrency) {
        Map<String, Object> resultMap = new HashMap<>();
        if (targetCurrency==null){
            resultMap.put("CurrencyUnit", "USD");
            resultMap.put("ProductList", productService.list());
           return Result.suc(resultMap);
        }

        List<Product> productList =  productService.getProductListByCurrency(targetCurrency);
        resultMap.put("CurrencyUnit", targetCurrency);
        resultMap.put("ProductList", productList);
        return Result.suc(resultMap);
    }

}

