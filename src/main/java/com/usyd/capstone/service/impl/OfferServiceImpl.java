package com.usyd.capstone.service.impl;

import com.github.dreamyoung.mprelation.ServiceImpl;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.mapper.OfferMapper;
import com.usyd.capstone.service.OfferService;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapper, Offer> implements OfferService {

}
