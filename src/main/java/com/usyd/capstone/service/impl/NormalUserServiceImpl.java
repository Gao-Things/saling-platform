package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.compents.ChatEndpoint;
import com.usyd.capstone.common.compents.JwtToken;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.PriceThreshold;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.NormalUserMapper;
import com.usyd.capstone.mapper.OfferMapper;
import com.usyd.capstone.mapper.PriceThresholdMapper;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Result makeAnOffer(String token, Long productId, String note, double price) {
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

        Offer offer = new Offer();
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
        int notificationStatus = ChatEndpoint.sendMessage(product.getOwnerId(),notificationContent.toString());


        return Result.suc("An new offer has been made.");
    }

    @Override
    public Result acceptAnOffer(String token, Long offerId) {
        return null;
    }

    @Override
    public Result cancelAnOffer(String token, Long offerId) {
        return null;
    }


}
