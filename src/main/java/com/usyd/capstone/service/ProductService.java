package com.usyd.capstone.service;

import com.usyd.capstone.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
public interface ProductService extends IService<Product> {


    List<Product> getProductListByCurrency(String targetCurrency);
}
