package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.DTO.StatisticsData;
import com.usyd.capstone.common.VO.ProductFilter;
import com.usyd.capstone.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    List<ProductUserDTO> listProduct(ProductFilter productFilter);

    Page<Product> selectProductsWithOffers(Page<Product> page, String searchValue);

    List<Product> getProductListByUserID(Integer userId, boolean isSelling);

    @Select("SELECT purity AS name, COUNT(*) AS value FROM product WHERE category = #{category} GROUP BY purity")
    List<StatisticsData>  productStatistic(Integer category);

    @Select("SELECT product_name AS name, search_count AS value FROM product ORDER BY search_count DESC LIMIT 5")
    List<StatisticsData> getHotProductStatistic();

}
