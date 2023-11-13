package com.usyd.capstone.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.StatisticsData;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.*;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.Notification;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

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

    @Autowired
    private NormalUserService normalUserService;

    @Autowired
    private S3Client s3Client;

    @Value("#{s3Bucket}")
    private String s3Bucket;

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
    public Result adminUploadProduct(@RequestBody ProductVO2 productVO){

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

        product.setProductStatus(productVO.getProductStatus());

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

    @GetMapping("/getUserListAdmin")
    public Result getUserListAdmin(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam String searchValue) {

        Page<NormalUser> userPage = normalUserService.getUserListAdmin(pageNum, pageSize, searchValue);
        return Result.suc(userPage);
    }

    @PostMapping("/adminUploadUserInfo")
    public Result adminUploadUserInfo(@RequestBody UserVO userVO){

        NormalUser normalUser = normalUserService.findUserInfoById(userVO.getUserId());

        normalUser.setName(userVO.getName());
        normalUser.setGender(userVO.getGender());
        normalUser.setEmail(userVO.getEmail());
        normalUser.setActivationStatus(userVO.isActivationStatus());

        if (normalUserService.updateUserInfo(normalUser)){

            return Result.suc();
        }else {
            return Result.fail();
        }

    }



    @PostMapping("/adminUploadLogo")
    public Result uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("uploadType") int uploadType) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return Result.fail("Please select a file to upload.");
        }

        // Get the original file name
        String originalFilename = file.getOriginalFilename();

        // Check if the file format is .png
        if (!"image/png".equals(file.getContentType())) {
            return Result.fail("Only PNG files are allowed.");
        }

        String newFilename = ""; // Initialize the new file name variable

        // Depending on the uploadType, rename the file to logo1.png or logo2.png
        if (uploadType == 1) {
            newFilename = "logo1.png";
        } else if (uploadType == 2) {
            newFilename = "logo2.png";
        } else {
            // Handle the case where uploadType is neither 1 nor 2
            return Result.fail("Invalid upload type. Please choose a valid option.");
        }

        try {
            // Create a PutObjectRequest with the new file name instead of the original
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(newFilename) // Use the new file name here
                    .contentType(file.getContentType())
                    .build();

            // Convert the file's bytes to a RequestBody object
            software.amazon.awssdk.core.sync.RequestBody requestBody = software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes());
            // Upload the file to S3 with the new file name
            s3Client.putObject(objectRequest, requestBody);

            // Return success result and the new file name
            return Result.suc(newFilename);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }


    @GetMapping("/genderStatistic")
    public Result getGenderStatistics() {
        List<StatisticsData> statistics = normalUserService.getGenderStatistics();
        return Result.suc(statistics);
    }

    @GetMapping("/productStatistic")
    public Result productStatistic(@RequestParam Integer category ) {
        List<StatisticsData> statistics = productService.productStatistic(category);
        return Result.suc(statistics);
    }

    @GetMapping("/getHotProductStatistic")
    public Result getHotProductStatistic() {
        List<StatisticsData> statistics = productService.getHotProductStatistic();
        return Result.suc(statistics);
    }



}



