package com.usyd.capstone.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.MakeAnOfferRequest;
import com.usyd.capstone.common.VO.Recaptcha;
import com.usyd.capstone.common.VO.SetPriceThresholdRequest;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.service.NormalUserService;
import com.usyd.capstone.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/normal")
public class NormalUserController {

    @Autowired
    private NormalUserService normalUserService;

    @Autowired
    private OfferService offerService;

    @GetMapping("/hello")
    public String hello(){
        int a = 1;
        return "hello, normal user";
    }

    // 获取用户所有的offer list
    @GetMapping("/getMyOfferList")
    public Result getMyOfferList(@RequestParam("userId") Integer userID){

        List<Offer> userOfferList = offerService.list(
                new QueryWrapper<Offer>().eq("buyer_id", userID)
        );

        return Result.suc(userOfferList);
    }

    // 获取用户id查所有收到的offer list
    @GetMapping("/getReceivedOfferList")
    public Result getReceivedOfferList(@RequestParam("userId") Integer userID){

        return offerService.getOfferListBySellerId(userID);
    }
    // 根据productId 获取product下的所有offer
    @GetMapping("/getProductOfferList")
    public Result getProductOfferList(@RequestParam("productId") Integer productId){
        List<Offer> productOfferList = offerService.list(
                new QueryWrapper<Offer>().eq("product_id", productId)
        );

        return Result.suc(productOfferList);
    }

    // 根据用户id，productId获取用户在某样商品下的最新offer
    @GetMapping("/getOfferByUserAndProductId")
    public Result getOfferByUserAndProductId(@RequestParam("productId") Integer productId,
                                             @RequestParam("userId") Integer userId){
        List<Offer> productOfferList = offerService.list(
                new QueryWrapper<Offer>()
                        .eq("product_id", productId)
                        .eq("buyer_id", userId)
                        .orderByDesc("timestamp")
                        .last("LIMIT 1")
        );

        return Result.suc(productOfferList);
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
