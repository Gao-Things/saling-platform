package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.productAdmin;
import com.usyd.capstone.common.VO.productVO;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.service.ExchangeRateUsdService;
import com.usyd.capstone.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

    @GetMapping("/currencyList")
    public Result getCurrencyList(){
        return exchangeRateUsdService.getNewestCurrency();
    }

    @GetMapping("/productList")
    public Result getProductsWithPrices(@RequestParam(required = false) String targetCurrency,
                                        @RequestParam int pageSize, @RequestParam int pageNum) {


        Page<productAdmin> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Map<String, Object> resultMap = new HashMap<>();
        String useCurrency;
        if (targetCurrency==null || targetCurrency.isEmpty()){
            useCurrency = "USD";
        }else {
            useCurrency = targetCurrency;
        }


        IPage productList = productService.getProductListByCurrency(useCurrency, pageNum, pageSize);

        resultMap.put("CurrencyUnit", targetCurrency);
        resultMap.put("ProductList", productList);
        return Result.suc(resultMap);
    }
    @Value("${upload.dir}") // 从配置文件获取上传目录
    private String uploadDir;

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
    public Result uploadProduct(@RequestBody productVO productVO){
        Product product = new Product();

        if (productVO.getProductId()!=null){
            product.setId(Long.valueOf(productVO.getProductId()));
        }
        product.setCategory(1);
        product.setCurrentTurnOfRecord(0);
        product.setInResettingProcess(false);
        product.setPriceStatus(1);
        product.setProductCreateTime(System.currentTimeMillis());
        product.setProductDescription(productVO.getItemDescription());
        product.setProductImage(productVO.getImageUrl());
        product.setProductName(productVO.getItemTitle());
        product.setProductPrice(400);
        product.setProductUpdateTime(System.currentTimeMillis());
        product.setProductWeight(productVO.getItemWeight());
        product.setOwnerId(Long.valueOf(productVO.getUserId()));
        if (productService.saveOrUpdate(product)){
            return Result.suc();
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


}