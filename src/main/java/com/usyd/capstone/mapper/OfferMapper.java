package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usyd.capstone.common.DTO.OfferProductDTO;
import com.usyd.capstone.entity.Offer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OfferMapper extends BaseMapper<Offer>  {
    List<OfferProductDTO> getOfferListBySellerId(@Param("ownerId") Long ownerId);
}
