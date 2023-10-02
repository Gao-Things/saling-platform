package com.usyd.capstone.service.impl;

import com.github.dreamyoung.mprelation.ServiceImpl;
import com.usyd.capstone.common.DTO.OfferProductDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.mapper.OfferMapper;
import com.usyd.capstone.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapper, Offer> implements OfferService {

    @Autowired
    OfferMapper offerMapper;
    @Override
    public Result getOfferListBySellerId(Integer userID) {

        List<OfferProductDTO> offerProductDTOList =  offerMapper.getOfferListBySellerId(Long.valueOf(userID));
        return Result.suc(offerProductDTOList);
    }
}
