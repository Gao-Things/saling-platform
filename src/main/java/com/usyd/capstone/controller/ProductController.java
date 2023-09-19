package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.productAdmin;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.service.ExchangeRateUsdService;
import com.usyd.capstone.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
    public Result getProductsWithPrices(@RequestParam(required = false) String targetCurrency,
                                        @RequestParam int pageSize,  @RequestParam int pageNum) {


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


        IPage productList =  productService.getProductListByCurrency(useCurrency, pageNum, pageSize);

        resultMap.put("CurrencyUnit", targetCurrency);
        resultMap.put("ProductList", productList);
        return Result.suc(resultMap);
    }
    @Value("${upload.dir}") // 从配置文件获取上传目录
    private String uploadDir;

    @Value("${image.Url}") // 从配置文件获取上传目录
    private String imageUrl;

    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("Please select a file to upload.");
        }

        try {
            // 确保上传目录存在，如果不存在则创建
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成新的文件名，将时间戳添加到原始文件名中
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String newFileName = fileNameWithoutExtension + System.currentTimeMillis() + fileExtension;
            String filePath = uploadDir + File.separator + newFileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            String imageUrlUse = imageUrl + newFileName;
            // 返回成功响应
            return Result.suc(imageUrlUse);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getProductDetail")
    public Result productDetail(@RequestParam Integer productID){
      return   productService.getProductById(productID);
    }


}

