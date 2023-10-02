package com.usyd.capstone.service;

import com.github.dreamyoung.mprelation.IService;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Offer;

public interface OfferService extends IService<Offer> {

    Result getOfferListBySellerId(Integer userID);
}
