package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.ProductVO;
import com.usyd.capstone.common.VO.ProductFilter;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.service.ExchangeRateUsdService;
import com.usyd.capstone.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p >
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

    @Autowired
    private S3Client s3Client;

    @Value("#{s3Bucket}")
    private String s3Bucket;

    @GetMapping("/currencyList")
    public Result getCurrencyList(){
        return exchangeRateUsdService.getNewestCurrency();
    }

    @GetMapping("/productList")
    public Result getProductsWithPrices(@ModelAttribute ProductFilter productFilter) {
        List<ProductUserDTO> productList = productService.listProduct(productFilter);
        return Result.suc(productList);
    }



    @GetMapping("/getMinMaxWeight")
    public Result getMinMaxWeight() {
        Map<String, Double> resultMap = new HashMap<>();
        double minWeight = productService.getMinWeight();
        double maxWeight = productService.getMaxWeight();

        resultMap.put("minWeight", minWeight);
            resultMap.put("maxWeight", maxWeight);
        return Result.suc(resultMap);
    }


    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("Please select a file to upload.");
        }

        try {
            // 获取文件的原始名字
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

            // 1. 使用LocalDateTime获取当前时间戳
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 2. 将时间戳添加到文件名中
            String key = timestamp + "_" + originalFilename;

            // 3. 获取文件的MIME类型
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream"; // default
            }

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(key)
                    .contentType(contentType)
                    .build();

            software.amazon.awssdk.core.sync.RequestBody requestBody = software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes());
            s3Client.putObject(objectRequest, requestBody);

            return Result.suc(key);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }


    @PostMapping("/uploadProduct")
    public Result uploadProduct(@RequestBody ProductVO productVO){
        Product product;

        if (productVO.getProductId()!=null){
            product = productService.getById(productVO.getProductId());
        }
        else
        {
            product = new Product();
            product.setCurrentTurnOfRecord(0);
        }
        product.setCategory(productVO.getCategory());

        if(product.getCategoryEnum() == null)
        {
            return Result.fail("invalid category");
        }
        product.setInResettingProcess(false);

        product.setPriceStatus(productVO.getHiddenPrice());
        product.setCategory(productVO.getCategory());
        product.setProductCreateTime(System.currentTimeMillis());
        product.setProductDescription(productVO.getItemDescription());
        product.setProductImage(productVO.getImageUrl());
        product.setProductName(productVO.getItemTitle());
        product.setProductPrice(productVO.getItemPrice());
        product.setProductUpdateTime(System.currentTimeMillis());
        product.setProductWeight(productVO.getItemWeight());
        product.setPurity(productVO.getItemPurity());
        if(product.getCategoryEnum() == null)
        {
            return Result.fail("invalid purity");
        }
        product.setOwnerId(Long.valueOf(productVO.getUserId()));
        product.setSearchCount(0);
        if (productService.saveOrUpdate(product)){
            return Result.suc(product.getId());
        }else {
            return Result.fail();
        }

    }

    @GetMapping("/getProductDetail")
    public Result getProductDetail(@RequestParam("productID") Integer productID ){

        if (productService.getById(productID) !=null){
            return Result.suc(productService.getById(productID));
        }
        return Result.fail();
    }

    @GetMapping("/getProductList/BySingleFilter")
    public Result getProductListAfterFiltered(@RequestParam("filter") String filter, @RequestParam("value") Integer value){


        return productService.getProductListAfterFiltered(filter, value,null,null);
    }

    @GetMapping("/getProductList/ByDoubleFilter")
    public Result getProductListAfterFiltered(@RequestParam("filter1") String filter1, @RequestParam("value1") Integer value1,
                                              @RequestParam("filter2") String filter2, @RequestParam("value2") String value2){

        return productService.getProductListAfterFiltered(filter1, value1, filter2, value2);
    }

    @GetMapping("/getProductList/top10Products")
    public Result getTop10Products(){

        Result result =  productService.getTop10Products();
        return result;
    }


}