package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.Search;
import com.usyd.capstone.service.ProductService;
import com.usyd.capstone.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                new QueryWrapper<Product>()
                        .ne("product_status", 3) // 添加不等于条件
                        .and(wrapper -> wrapper
                                .like("product_name", userInput)
                                .or()
                                .like("product_description", userInput)
                        )
        );

        return Result.suc(productList);
    }


    /**
     * 用户search历史
     */
    @PostMapping("/saveSearchHistory")
    public Result saveSearchHistory(@RequestBody Search search){

        Product product = productService.getById(search.getProductId());
        if(product != null) {
            product.setSearchCount(product.getSearchCount() + 1);

            productService.updateById(product);
            Search oldSearchResult = searchService.getOne(
                    new QueryWrapper<Search>().eq("search_content", search.getSearchContent())
            );

            if (oldSearchResult == null) {
                return Result.suc(searchService.save(search));
            } else {

                search.setSearchId(oldSearchResult.getSearchId());
                return Result.suc(searchService.saveOrUpdate(search));
            }
        }
        return Result.fail("The product isn't existed");
    }

    /**
     *
     * @param userId
     * @return 用户搜索历史
     */
    @GetMapping("/getSearchHistoryByUserId")
    public Result getSearchHistoryByUserId(@RequestParam Integer userId){

        List<Search> searchList = searchService.list(
                new QueryWrapper<Search>().eq("user_id", userId)
        );
        return Result.suc(searchList);
    }

    @GetMapping("/deleteHistoryByUserId")
    public Result deleteHistoryByUserId(@RequestParam Integer userId){

      Boolean result = searchService.remove(
         new QueryWrapper<Search>().eq("user_id", userId)
       );
        return Result.suc(result);
    }



}

