package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.StatisticsData;
import com.usyd.capstone.common.DTO.NotificationDTO;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.compents.JwtToken;
import com.usyd.capstone.entity.*;
import com.usyd.capstone.mapper.*;
import com.usyd.capstone.rabbitMq.FanoutSender;
import com.usyd.capstone.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NormalUserServiceImpl implements NormalUserService {


    @Autowired
    private NormalUserMapper normalUserMapper;

    @Autowired
    private PriceThresholdMapper priceThresholdMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private FanoutSender fanoutSender;

    @Override
    public Result setPriceThresholdSingle(String token, Long productId, boolean isMinimum, double threshold) {
        Long normalUserId = JwtToken.getId(token);
        if(normalUserId == -1L) {
            return Result.fail("Cannot parse the token!");
        }

        NormalUser normalUser = normalUserMapper.selectById(normalUserId);
        Product product = productMapper.selectById(productId);
        if(normalUser == null || product == null)
        {
            return Result.fail("Cannot find the user account or product!");
        }

        if(!normalUser.isActivationStatus()) {
            return Result.fail("The user account isn't active!");
        }

        PriceThreshold priceThreshold = priceThresholdMapper.selectOne(new QueryWrapper<PriceThreshold>()
                .eq("normal_user_id", normalUserId)
                .eq("product_id", productId)
                .eq("is_minimum", isMinimum));

        PriceThreshold priceThreshold1 = priceThresholdMapper.selectOne(new QueryWrapper<PriceThreshold>()
                .eq("normal_user_id", normalUserId)
                .eq("product_id", productId)
                .eq("is_minimum", (isMinimum) ? false : true));

        if(priceThreshold == null)
        {
            if(priceThreshold1 == null)
            {
                insertANewPriceThreshold(normalUserId, productId, isMinimum, threshold);
            }
            else if ((isMinimum && priceThreshold1.getThreshold() > threshold) ||
                    (!isMinimum && priceThreshold1.getThreshold() < threshold))
            {
                insertANewPriceThreshold(normalUserId, productId, isMinimum, threshold);
            }
            else
            {
                return Result.suc("Error with the threshold input");
            }
        }
        else
        {
            if(priceThreshold1 == null)
            {
                updateAPriceThreshold(priceThreshold, threshold);
            }
            else if ((isMinimum && priceThreshold1.getThreshold() > threshold) ||
                    (!isMinimum && priceThreshold1.getThreshold() < threshold))
            {
                updateAPriceThreshold(priceThreshold, threshold);
            }
            else
            {
                return Result.fail("Error with the threshold input");
            }
        }

        return Result.suc("Your price threshold has been set/reset successfully");
    }

    private void insertANewPriceThreshold(Long normalUserId, Long productId, boolean isMinimum, double threshold) {
        PriceThreshold priceThreshold = new PriceThreshold();
        priceThreshold.setNormalUserId(normalUserId);
        priceThreshold.setProductId(productId);
        priceThreshold.setMinimum(isMinimum);
        priceThreshold.setThreshold(threshold);
        priceThreshold.setReached(false);
        priceThresholdMapper.insert(priceThreshold);
    }

    private void updateAPriceThreshold(PriceThreshold priceThreshold, double threshold) {
        priceThreshold.setThreshold(threshold);
        priceThreshold.setReached(false);
        priceThresholdMapper.updateById(priceThreshold);
    }

    public Result setPriceThresholdBatch(String token, Long productId, boolean isMinimum, double threshold)
    {
        return null;
    }

    @Override
    public Result makeOrUpdateAnOffer(String token, Long productId, String note, double price) {
        Long buyerId = JwtToken.getId(token);
        if(buyerId == -1L) {
            return Result.fail("Cannot parse the token!");
        }

        NormalUser buyer = normalUserMapper.selectById(buyerId);
        Product product = productMapper.selectById(productId);
        if(buyer == null || product == null)
        {
            return Result.fail("Cannot find the user account or product!");
        }

        if(!buyer.isActivationStatus()) {
            return Result.fail("The user account isn't active!");
        }

        if(product.getOwnerId() == buyerId) {
            return Result.fail("You cannot buy your own items!");
        }

        if(product.getProductStatus() != 0) {
            return Result.fail("The product isn't for sale!");
        }

        Offer offer = offerMapper.selectOne(new QueryWrapper<Offer>()
                .eq("product_id", productId)
                .eq("buyer_id", buyerId));
        if(offer == null)
        {
            offer = new Offer();
            offer.setBuyerId(buyerId);
            offer.setProductId(productId);
            offer.setPrice(price);
            offer.setNote(note);
            offer.setOfferStatus(0);
            offer.setTimestamp(System.currentTimeMillis());

            offerMapper.insert(offer);

            StringBuilder notificationContent = new StringBuilder();
            notificationContent.append(buyer.getName());
            notificationContent.append(" has made an new offer to your item: ");
            notificationContent.append(product.getProductName());
            notificationContent.append(".");
            sendNotification(3, 1, offer, product, notificationContent.toString());


            return Result.suc("An new offer has been made.");
        }
        else
        {
            if(offer.getOfferStatus() == 1) {
                return Result.fail("The accepted offer cannot be update!");
            }

            if(offer.getOfferStatus() == 4) {
                return Result.fail("The offer had been expired!(The product has been sold or cancelled)");
            }

            offer.setOfferStatus(0);
            offer.setPrice(price);
            offer.setNote(note);
            offer.setTimestamp(System.currentTimeMillis());
            offerMapper.updateById(offer);

            StringBuilder notificationContent = new StringBuilder();
            notificationContent.append(buyer.getName());
            notificationContent.append(" has update his offer to your item: ");
            notificationContent.append(product.getProductName());
            notificationContent.append(".");
            sendNotification(4, 1, offer, product, notificationContent.toString());


            return Result.suc("The offer has been updated.");
        }


    }

    @Override
    public Result acceptAnOffer(String token, Long offerId) {
        Long sellerId = JwtToken.getId(token);
        if(sellerId == -1L) {
            return Result.fail("Cannot parse the token!");
        }

        NormalUser seller = normalUserMapper.selectById(sellerId);
        Offer offer = offerMapper.selectById(offerId);

        if(seller == null || offer == null)
        {
            return Result.fail("Cannot find the seller account or offer!");
        }

        if(!seller.isActivationStatus()) {
            return Result.fail("The seller account isn't active!");
        }

        if(offer.getOfferStatus() != 0) {
            return Result.fail("The offer isn't on pending!");
        }

        Product product = productMapper.selectById(offer.getProductId());
        if(product == null) {
            return Result.fail("The product cannot be found!");
        }
        if(product.getProductStatus() > 1) {
            return Result.fail("The product has been sold or cancelled!");
        }
        if(product.getOwnerId() != sellerId) {
            return Result.fail("Only the product owner can accept the offer!");
        }



        offer.setOfferStatus(1);
        offerMapper.updateById(offer);
        product.setProductStatus(2);
        productMapper.updateById(product);

//        List<Offer> offers = offerMapper.selectList(new QueryWrapper<Offer>()
//                .eq("product_id", product.getId())
//                .ne("id", offerId));
//        offers.forEach(offer1 -> {
//            offer1.setOfferStatus(4);
//            offerMapper.updateById(offer1);
//            StringBuilder notificationContent = new StringBuilder();
//            notificationContent.append("The seller, ");
//            notificationContent.append(seller.getName());
//            notificationContent.append(", has accepted another offer about the item: ");
//            notificationContent.append(product.getProductName());
//            notificationContent.append(". Therefore, your relevant offer is expired.");
//            sendNotification(6, 2, offer, product, notificationContent.toString());
//        });

        StringBuilder notificationContent = new StringBuilder();
        notificationContent.append("The seller, ");
        notificationContent.append(seller.getName());
        notificationContent.append(", has accepted your offer about the item: ");
        notificationContent.append(product.getProductName());
        notificationContent.append(".");
        sendNotification(1, 2, offer, product, notificationContent.toString());
        return Result.suc("The offer has been accepted.");
    }

    @Override
    public Result cancelAnOffer(String token, Long offerId) {
        Long buyerId = JwtToken.getId(token);
        if(buyerId == -1L) {
            return Result.fail("Cannot parse the token!");
        }

        NormalUser buyer = normalUserMapper.selectById(buyerId);
        Offer offer = offerMapper.selectById(offerId);
        if(buyer == null || offer == null)
        {
            return Result.fail("Cannot find the user account or offer!");
        }

        if(!buyer.isActivationStatus()) {
            return Result.fail("The user account isn't active!");
        }

        if(offer.getBuyerId() != buyerId) {
            return Result.fail("The request is invalid!");
        }

        if(offer.getOfferStatus() == 1) {
            return Result.fail("The accepted offer cannot be cancel!");
        }

        if(offer.getOfferStatus() == 3) {
            return Result.fail("The offer had been cancelled before!");
        }

        offer.setOfferStatus(3);
        offerMapper.updateById(offer);
        Product product = productMapper.selectById(offer.getProductId());

        StringBuilder notificationContent = new StringBuilder();
        notificationContent.append(buyer.getName());
        notificationContent.append(" has cancelled his offer to your item: ");
        notificationContent.append(product.getProductName());
        notificationContent.append(".");
        sendNotification(5, 1, offer, product, notificationContent.toString());

        return Result.suc("The offer has been cancelled.");
    }

    @Override
    public Result rejectAnOffer(String token, Long offerId) {
        Long sellerId = JwtToken.getId(token);
        if(sellerId == -1L) {
            return Result.fail("Cannot parse the token!");
        }

        NormalUser seller = normalUserMapper.selectById(sellerId);
        Offer offer = offerMapper.selectById(offerId);

        if(seller == null || offer == null)
        {
            return Result.fail("Cannot find the seller account or offer!");
        }

        if(!seller.isActivationStatus()) {
            return Result.fail("The seller account isn't active!");
        }

        if(offer.getOfferStatus() != 0) {
            return Result.fail("The offer isn't on pending!");
        }

        Product product = productMapper.selectById(offer.getProductId());
        if(product == null) {
            return Result.fail("The product cannot be found!");
        }

        offer.setOfferStatus(2);
        offerMapper.updateById(offer);

        StringBuilder notificationContent = new StringBuilder();
        notificationContent.append("The seller, ");
        notificationContent.append(seller.getName());
        notificationContent.append(", has rejected your offer about the item: ");
        notificationContent.append(product.getProductName());
        notificationContent.append(".");
        sendNotification(2, 2, offer, product, notificationContent.toString());

        return Result.suc("The offer has been rejected.");
    }

    @Override
    public Result openOrCloseOrCancelSale(String token, Long productId, int productStatusNew) {
        Long buyerId = JwtToken.getId(token);
        if(buyerId == -1L) {
            return Result.fail("Cannot parse the token!");
        }

        NormalUser buyer = normalUserMapper.selectById(buyerId);
        Product product = productMapper.selectById(productId);
        if(buyer == null || product == null)
        {
            return Result.fail("Cannot find the user account or product!");
        }

        if(!buyer.isActivationStatus()) {
            return Result.fail("The user account isn't active!");
        }

        switch (productStatusNew)
        {
            case 0:
                if(product.getProductStatus() == 1)
                {
                    product.setProductStatus(productStatusNew);
                    productMapper.updateById(product);
                    return Result.suc("The item, " + product.getProductName() + " is open.");
                }
                else
                {
                    return Result.fail("Invalid action.");
                }
            case 1:
                if(product.getProductStatus() == 0)
                {
                    product.setProductStatus(productStatusNew);
                    productMapper.updateById(product);
                    return Result.suc("The item, " + product.getProductName() + " has close.");
                }
                else
                {
                    return Result.fail("Invalid action.");
                }
            case 3:
                if(product.getProductStatus() <= 2)
                {
                    product.setProductStatus(productStatusNew);
                    productMapper.updateById(product);
                    List<Offer> offers = offerMapper.selectList(new QueryWrapper<Offer>()
                            .eq("product_id", productId));
                    NormalUser seller = normalUserMapper.selectById(product.getId());
                    offers.forEach(offer -> {
                        offer.setOfferStatus(4);
                        offerMapper.updateById(offer);
                        StringBuilder notificationContent = new StringBuilder();
                        notificationContent.append("The seller, ");
                        notificationContent.append(seller.getName());
                        notificationContent.append(", has withdraw the sale about the item: ");
                        notificationContent.append(product.getProductName());
                        notificationContent.append(". Therefore, your relevant offer is expired.");
                        sendNotification(6, 2, offer, product, notificationContent.toString());
                    });
                    return Result.suc("The item, " + product.getProductName() + " is cancelled.");
                }
                else
                {
                    return Result.fail("Invalid action.");
                }
            default:
                return Result.fail("Invalid status input.");
        }
    }

    @Override
    public NormalUser findUserInfoById(Integer userId) {

        NormalUser normalUser = normalUserMapper.selectById(userId);
        return normalUser;
    }

    @Override
    public Boolean updateUserInfo(NormalUser normalUser) {
      return   normalUserMapper.updateById(normalUser) == 1;
    }

    @Override
    public Page<NormalUser> getUserListAdmin(Integer pageNum, Integer pageSize, String searchValue) {

        Page<Product> page = new Page<>(pageNum, pageSize);
        return normalUserMapper.getUserListAdmin(page, searchValue);

    }


    private void sendNotification(Integer type,
                                  Integer userType,
                                  Offer offer,
                                  Product product,
                                  String message){
        Notification notification = new Notification();

        notification.setNotificationType(type);
        notification.setSendUserType(userType);
        notification.setSendUserId(offer.getBuyerId().intValue());
        notification.setUserIsRead(0);
        notification.setNotificationTimestamp(System.currentTimeMillis());
        notification.setNotificationContent(message);
        notificationMapper.insert(notification);


        // push message to buyer
        NotificationDTO notificationDto = new NotificationDTO();
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setNotificationContent(message);

        // 接受此消息用户的类型
        notificationDto.setSendUserType(userType);

        // 消息类型
        notificationDto.setMessageType(type);

        notificationDto.setOffer(offer);
        notificationDto.setProduct(product);

        String result = JSONObject.toJSONString(notificationDto);


        // send message to seller
        if(userType == 1){

            /**
             *
             * 把推送消息发送到rabbitMQ
             *
             */

            Map<Integer, String> rabbitMessageList = new HashMap<>();
            rabbitMessageList.put(product.getOwnerId().intValue(), result);

            fanoutSender.sendMessage(rabbitMessageList);

//            NotificationServer.sendMessage(result, product.getOwnerId().intValue());

        } else{

            Map<Integer, String> rabbitMessageList = new HashMap<>();
            rabbitMessageList.put(offer.getBuyerId().intValue(), result);
//            NotificationServer.sendMessage(result, offer.getBuyerId().intValue());

            fanoutSender.sendMessage(rabbitMessageList);
        }

    }


    public List<StatisticsData> getGenderStatistics() {
        return normalUserMapper.selectGenderStatistics();
    }


}
