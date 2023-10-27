package com.usyd.capstone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.productAdmin;
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


    IPage getProductListByCurrency(String targetCurrency,int pageNum, int pageSize);

    Result getProductById(Integer productID);

    List<ProductUserDTO> listProduct();
}
