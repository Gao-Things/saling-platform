package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.compents.JwtToken;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.PriceThreshold;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.NormalUserMapper;
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

    @Override
    public Result setPriceThresholdSingle(String token, long productId, boolean isMinimum, double threshold) {
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

    private void insertANewPriceThreshold(long normalUserId, long productId, boolean isMinimum, double threshold) {
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

    public Result setPriceThresholdBatch(String token, long productId, boolean isMinimum, double threshold)
    {
        return null;
    }
}
