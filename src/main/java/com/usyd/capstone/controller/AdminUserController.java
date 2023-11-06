package com.usyd.capstone.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.NotificationDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.AdminResetingPrice;
import com.usyd.capstone.common.VO.OfferVO;
import com.usyd.capstone.common.VO.ProductVO;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.entity.Notification;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.rabbitMq.FanoutSender;
import com.usyd.capstone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public/admin")
public class AdminUserController {
    @GetMapping("/hello")
    public String hello(){
        return "hello, admin user";
    }

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/resettingSingleProductPrice")
    public Result test(@RequestBody AdminResetingPrice adminResetingPrice)
    {
        return adminUserService.updateProductPrice(adminResetingPrice.getToken(),
                adminResetingPrice.getProductId(), adminResetingPrice.getProductPrice(), adminResetingPrice.getTurnOfRecord());
    }


    @GetMapping("/productListAdmin")
    public Result productListAdmin(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam String searchValue) {

        Page<Product> productPage = productService.getProductListAndOffer(pageNum, pageSize, searchValue);
        return Result.suc(productPage);
    }


    @PostMapping("/adminUploadProduct")
    public Result adminUploadProduct(@RequestBody ProductVO productVO){

        Product product;

        if (productVO.getProductId()!=null){
            product = productService.getById(productVO.getProductId());
        }else {
            return Result.fail();
        }

        product.setCategory(productVO.getCategory());

        if(product.getCategoryEnum() == null)
        {
            return Result.fail("invalid category");
        }
        product.setInResettingProcess(false);

        product.setPriceStatus(productVO.getHiddenPrice());
        product.setCategory(productVO.getCategory());
        product.setProductCreateTime(System.currentTimeMillis());
        product.setProductDescription(productVO.getItemDescription());
        product.setProductImage(productVO.getImageUrl());
        product.setProductName(productVO.getItemTitle());
        product.setProductPrice(productVO.getItemPrice());
        product.setProductUpdateTime(System.currentTimeMillis());
        product.setProductWeight(productVO.getItemWeight());
        product.setPurity(productVO.getItemPurity());
        if(product.getCategoryEnum() == null)
        {
            return Result.fail("invalid purity");
        }
        product.setOwnerId(Long.valueOf(productVO.getUserId()));
        product.setSearchCount(0);
        if (productService.saveOrUpdate(product)){

            // save the system notification to datbabase
            Notification notification = new Notification();

            notification.setNotificationType(9);
            notification.setSendUserType(1);
            notification.setSendUserId(0);
            notification.setUserIsRead(0);
            notification.setNotificationTimestamp(System.currentTimeMillis());
            notification.setNotificationContent("Your product information has been modified by the system");

            notificationService.save(notification);

            return Result.suc(product.getId());
        }else {
            return Result.fail();
        }

    }


    @PostMapping("/adminUploadOffer")
    public Result adminUploadOffer(@RequestBody OfferVO offerVO){

        Offer offer;

        if (offerVO.getId()!=null){
            offer = offerService.getById(offerVO.getId());
        }else {
            return Result.fail();
        }

        offer.setPrice(offerVO.getPrice());
        offer.setOfferStatus(offerVO.getOfferStatus());
        offer.setNote(offerVO.getNote());

        if (offerService.saveOrUpdate(offer)){

            return Result.suc();
        }else {
            return Result.fail();
        }

    }

    @GetMapping("/adminDeleteOffer")
    public Result adminDeleteOffer(@RequestParam Integer offerId) {

        return Result.suc(offerService.removeById(offerId));

    }


    @GetMapping("/adminDeleteProduct")
    public Result adminDeleteProduct(@RequestParam Integer productID) {

        Product product = productService.getById(productID);
        product.setProductStatus(3);

        if (productService.updateById(product)){


            // save the system notification to datbabase
            Notification notification = new Notification();

            notification.setNotificationType(9);
            notification.setSendUserType(1);
            notification.setSendUserId(0);
            notification.setUserIsRead(0);
            notification.setNotificationTimestamp(System.currentTimeMillis());
            notification.setNotificationContent("Your product has been canceled by the system");

            notificationService.save(notification);

            return Result.suc();
        }else {
            return Result.fail();
        }

    }


    @GetMapping("/getOfferListAdmin")
    public Result getOfferListAdmin(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam Integer productId,
            @RequestParam String searchValue) {

        Page<Offer> productPage = productService.getOfferListAdmin(pageNum, pageSize,productId, searchValue);
        return Result.suc(productPage);
    }


    @GetMapping("/getProductDetailById")
    public Result getProductDetailById( @RequestParam Integer productId ) {


        return Result.suc(productService.getById(productId));
    }



}



