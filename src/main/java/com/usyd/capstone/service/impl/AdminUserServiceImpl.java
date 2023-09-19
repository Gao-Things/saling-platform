package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.compents.JwtToken;
import com.usyd.capstone.common.compents.SendEmail;
import com.usyd.capstone.entity.AdminUser;
import com.usyd.capstone.entity.AdminUserProduct;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.ProductPriceRecord;
import com.usyd.capstone.service.AdminUserService;
import com.usyd.capstone.service.base.AdminUserBaseService;
import com.usyd.capstone.service.base.AdminUserProductBaseService;
import com.usyd.capstone.service.base.ProductBaseService;
import com.usyd.capstone.service.base.ProductPriceRecordBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final int requiredAdminUserNumForChangeAProductPrice = 3;

    private final Object lock = new Object();

    @Autowired
    private AdminUserBaseService adminUserBaseService;

    @Autowired
    private ProductBaseService productBaseService;

    @Autowired
    private AdminUserProductBaseService adminUserProductBaseService;

    @Autowired
    private ProductPriceRecordBaseService productPriceRecordBaseService;

    @Autowired
    private SendEmail mailSender;

    @Override
    public Result updateProductPrice(String token, Long productId, double productPrice, int turnOfRecord) {
        //报价可能存在并发导致线程冲突（例如，每轮的报价次数上限是5，但是同时收到了6个请求），
        // 所以使用 Java 的同步机制，synchronized 关键字，来确保在同一时间只有一个线程可以提出报价
        // 因为admin操作的并发量不会很高，所以只使用最简单的同步机制
        synchronized (lock) {

            Long adminId = JwtToken.getId(token);
            if (adminId == -1L) {
                return Result.fail("Cannot parse token!");
            }
            Product product = productBaseService.getById(productId);
            AdminUser adminUser = adminUserBaseService.getById(adminId);

            if (adminUser == null || product == null) {
                return Result.fail("Cannot find admin or product!");
            }

            if(!adminUser.isActivationStatus()) {
                return Result.fail("The admin account isn't active!");
            }
            //验证报价轮次是否一致，防止有ADMIN页面没刷新，没看到新价格已经出了，又提交上一轮的报价
            if(turnOfRecord != product.getCurrentTurnOfRecord() + 1)
            {
                return Result.fail("Wrong turn number for updating price");
            }

            AdminUserProduct adminUserProductOld = adminUserProductBaseService
                    .getOne(new QueryWrapper<AdminUserProduct>()
                            .eq("admin_user_id", adminId)
                            .eq("product_id", productId)
                            .eq("turn_of_record", turnOfRecord)
                            .eq("is_valid", true));
            if (adminUserProductOld != null) {
                adminUserProductOld.setValid(false);
                adminUserProductBaseService.updateById(adminUserProductOld);
            }

            AdminUserProduct adminUserProduct = createANewChangingPriceRecord(adminUser, product,
                    productPrice, turnOfRecord);
            adminUserProductBaseService.save(adminUserProduct);
            if (product.isInResettingProcess() == false)
            {
                product.setInResettingProcess(true);
                productBaseService.updateById(product);
            }

            checkIfProductPriceShouldBeUpdate(product, turnOfRecord);
            return Result.suc("Your quotation has been record successfully");
        }
    }

    private AdminUserProduct createANewChangingPriceRecord(AdminUser adminUser, Product product,
                                                           double productPrice, int turnOfRecord){
        AdminUserProduct adminUserProduct = new AdminUserProduct();
        adminUserProduct.setAdminUserId(adminUser.getId());
        adminUserProduct.setAdminUser(adminUser);
        adminUserProduct.setProductId(product.getId());
        adminUserProduct.setProduct(product);
        adminUserProduct.setProductPrice(productPrice);
        adminUserProduct.setRecordTimestamp(System.currentTimeMillis());
        adminUserProduct.setTurnOfRecord(turnOfRecord);
        adminUserProduct.setValid(true);
        return adminUserProduct;
    }

    private void checkIfProductPriceShouldBeUpdate(Product product, int turnOfRecord){
        List<AdminUserProduct> tempList = adminUserProductBaseService.list(new QueryWrapper<AdminUserProduct>()
                .eq("product_id", product.getId())
                .eq("turn_of_record", turnOfRecord)
                .eq("is_valid", true));

        if(tempList.size() >= requiredAdminUserNumForChangeAProductPrice)
        {
            HashMap<Double, Integer> productNewPriceMap = new HashMap<>();
            tempList.forEach(adminUserProduct->{
                productNewPriceMap.merge(adminUserProduct.getProductPrice(), 1, Integer::sum);
            });
            int size = productNewPriceMap.size();
            if(size == 1)
            {
                double priceOld = product.getProductPrice();
                ProductPriceRecord productPriceRecord = new ProductPriceRecord();
                productPriceRecord.setProductId(product.getId());
                productPriceRecord.setProductPrice(priceOld);
                productPriceRecord.setRecordTimestamp(product.getProductUpdateTime());
                productPriceRecord.setTurnOfRecord(product.getCurrentTurnOfRecord());
                productPriceRecordBaseService.save(productPriceRecord);

                double priceNew = tempList.get(0).getProductPrice();
                if(priceOld < priceNew)
                    product.setPriceStatus(0);
                else if(priceOld > priceNew)
                    product.setPriceStatus(1);
                else
                    product.setPriceStatus(2);
                product.setProductPrice(tempList.get(0).getProductPrice());
                product.setCurrentTurnOfRecord(product.getCurrentTurnOfRecord() + 1);
                product.setProductUpdateTime(System.currentTimeMillis());
                product.setInResettingProcess(false);
                productBaseService.updateById(product);
            }
            else
            {
                // 找到最大值
                int maxValue = Collections.max(productNewPriceMap.values());

                // 找到最大值对应的键
                List<Double> maxKeys = new ArrayList<>();
                for (Map.Entry<Double, Integer> entry : productNewPriceMap.entrySet()) {
                    if (entry.getValue() == maxValue) {
                        maxKeys.add(entry.getKey());
                    }
                }

                if(maxKeys.size() > 1){
                    tempList.forEach(adminUserProduct -> {
                        adminUserProduct.setValid(false);
                        mailSender.sentResettingPriceWarning(adminUserProduct);
                    });
                    adminUserProductBaseService.updateBatchById(tempList);
                }
                else
                {
                    double priceWithWidestAgreement = maxKeys.get(0);
                    tempList.forEach(adminUserProduct -> {
                        if(adminUserProduct.getProductPrice() != priceWithWidestAgreement) {
                            adminUserProduct.setValid(false);
                            mailSender.sentResettingPriceWarning(adminUserProduct);
                            adminUserProductBaseService.updateById(adminUserProduct);
                        }
                    });
                }
            }
        }

    }

    //TODO ADMIN的Dashboard应该要能显示（记录）当前报轮的轮数和该轮他提出过的报价（如果有)
}
