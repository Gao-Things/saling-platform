package com.usyd.capstone.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.*;
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
    public Result getMyOfferList(@ModelAttribute OfferFilter offerFilter){


        QueryWrapper<Offer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_id", offerFilter.getUserId());

        if (Boolean.TRUE.equals(!offerFilter.getPending())) {
            queryWrapper.ne("offer_status", 0);
        }
        if (Boolean.TRUE.equals(!offerFilter.getAccepted())) {
            queryWrapper.ne("offer_status", 1);
        }
        if (Boolean.TRUE.equals(!offerFilter.getRejected())) {
            queryWrapper.ne("offer_status", 2);
        }
        if (Boolean.TRUE.equals(!offerFilter.getCancelled())) {
            queryWrapper.ne("offer_status", 3);
        }
        if (Boolean.TRUE.equals(!offerFilter.getExpired())) {
            queryWrapper.ne("offer_status", 4);
        }


        List<Offer> userOfferList = offerService.list(queryWrapper);

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
                new QueryWrapper<Offer>().eq("product_id", productId).ne("offer_status", 3)
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
                        .ne("offer_status", 3)
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

    @PostMapping("/makeOrUpdateAnOffer")
    public Result makeAnOffer(@RequestBody MakeOrUpdateAnOfferRequest makeOrUpdateAnOfferRequest) {
        return normalUserService.makeOrUpdateAnOffer(makeOrUpdateAnOfferRequest.getToken(),
                makeOrUpdateAnOfferRequest.getProductId(),
                makeOrUpdateAnOfferRequest.getNote(),
                makeOrUpdateAnOfferRequest.getPrice());
    }

    @PostMapping("/acceptAnOffer")
    public Result acceptAnOffer(@RequestBody AcceptOrCancelOrRejectAnOfferRequest acceptOrCancelOrRejectAnOfferRequest) {
        return normalUserService.acceptAnOffer(acceptOrCancelOrRejectAnOfferRequest.getToken(),
                acceptOrCancelOrRejectAnOfferRequest.getOfferId());
    }

    @PostMapping("/cancelAnOffer")
    public Result cancelAnOffer(@RequestBody AcceptOrCancelOrRejectAnOfferRequest acceptOrCancelOrRejectAnOfferRequest) {
        return normalUserService.cancelAnOffer(acceptOrCancelOrRejectAnOfferRequest.getToken(),
                acceptOrCancelOrRejectAnOfferRequest.getOfferId());
    }

    @PostMapping("/rejectAnOffer")
    public Result rejectAnOffer(@RequestBody AcceptOrCancelOrRejectAnOfferRequest acceptOrCancelOrRejectAnOfferRequest) {
        return normalUserService.rejectAnOffer(acceptOrCancelOrRejectAnOfferRequest.getToken(),
                acceptOrCancelOrRejectAnOfferRequest.getOfferId());
    }

    @PostMapping("/openOrCloseOrCancelSale")
    public Result openOrCloseOrCancelSale(@RequestBody OpenOrCloseOrCancelSaleRequest openOrCloseOrCancelSaleRequest) {
        return normalUserService.openOrCloseOrCancelSale(openOrCloseOrCancelSaleRequest.getToken(),
                openOrCloseOrCancelSaleRequest.getProductId(),
                openOrCloseOrCancelSaleRequest.getProductStatusNew());
    }
}
