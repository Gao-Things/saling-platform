package com.usyd.capstone.controller;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.MakeAnOfferRequest;
import com.usyd.capstone.common.VO.Recaptcha;
import com.usyd.capstone.common.VO.SetPriceThresholdRequest;
import com.usyd.capstone.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/normal")
public class NormalUserController {

    @Autowired
    private NormalUserService normalUserService;

    @GetMapping("/hello")
    public String hello(){
        int a = 1;
        return "hello, normal user";
    }

    @PostMapping("/setPriceThreshold")
    public Result setPriceThreshold(@RequestBody SetPriceThresholdRequest setPriceThresholdRequest) {
        return normalUserService.setPriceThresholdSingle(setPriceThresholdRequest.getToken(),
                setPriceThresholdRequest.getProductId(),
                setPriceThresholdRequest.isMinimum(),
                setPriceThresholdRequest.getThreshold());
    }

    @PostMapping("/makeAnOffer")
    public Result makeAnOffer(@RequestBody MakeAnOfferRequest makeAnOfferRequest) {
        return normalUserService.makeAnOffer(makeAnOfferRequest.getToken(),
                makeAnOfferRequest.getProductId(),
                makeAnOfferRequest.getNote(),
                makeAnOfferRequest.getPrice());
    }
}
