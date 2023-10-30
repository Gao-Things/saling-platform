package com.usyd.capstone.mapper;

import com.usyd.capstone.common.DTO.ProductUserDTO;
import com.usyd.capstone.common.VO.ProductFilter;
import com.usyd.capstone.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
