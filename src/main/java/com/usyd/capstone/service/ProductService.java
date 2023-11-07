package com.usyd.capstone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.DTO.StatisticsData;
import com.usyd.capstone.common.VO.ProductFilter;
import com.usyd.capstone.entity.Offer;
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
    List<ProductUserDTO> listProduct(ProductFilter productFilter);
    Result getProductListAfterFiltered(String filter1, Integer value1, String filter2, String value2);
    Result getTop10Products();

    double getMinWeight();

    double getMaxWeight();

    Page<Product> getProductListAndOffer(Integer pageNum, Integer pageSize, String searchValue);

    List<Product> getProductListByUserID(Integer userId, boolean isSelling);

    Page<Offer> getOfferListAdmin(Integer pageNum, Integer pageSize, Integer productId, String searchValue);


    List<StatisticsData> productStatistic(Integer category);

    List<StatisticsData> getHotProductStatistic();
}
