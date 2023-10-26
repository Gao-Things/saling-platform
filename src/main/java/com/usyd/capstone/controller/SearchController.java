package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.service.ProductService;
import com.usyd.capstone.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mengting
 * @since 2023年10月25日
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    ProductService productService;

    @Resource
    SearchService searchService;


    @GetMapping("/getSearchResultByInput")
    public Result getSearchResultByInput(@RequestParam String userInput){

        List<Product> productList =  productService.list(
                new QueryWrapper<Product>().like("product_name", userInput)
                        .or()
                        .like("product_description", userInput)
        );

        return Result.suc(productList);
    }


}

