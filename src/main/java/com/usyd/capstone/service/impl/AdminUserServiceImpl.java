package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.compents.JwtToken;
import com.usyd.capstone.entity.AdminUser;
import com.usyd.capstone.entity.AdminUserProduct;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.AdminUserMapper;
import com.usyd.capstone.mapper.AdminUserProductMapper;
import com.usyd.capstone.mapper.ProductMapper;
import com.usyd.capstone.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final int requiredAdminUserNumForChangeAProductPrice = 3;

    private final Object lock = new Object();

    @Autowired
    private JwtToken jwtToken;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private AdminUserProductMapper adminUserProductMapper;

    @Override
    public Result updateProductPrice(String token, Long productId, double productPrice, int turnOfRecord) {
        //报价可能存在并发导致线程冲突（例如，每轮的报价次数上限是5，但是同时收到了6个请求），
        // 所以使用 Java 的同步机制，synchronized 关键字，来确保在同一时间只有一个线程可以提出报价
        synchronized (lock) {

            Long adminId = jwtToken.getId(token);
            if (adminId == -1L) {
                return Result.fail("Cannot parse token!");
            }
            AdminUser adminUser = adminUserMapper.selectById(adminId);
            Product product = productMapper.selectById(productId);
            if (adminUser == null || product == null) {
                return Result.fail("Cannot find admin or product!");
            }
            //防止有ADMIN页面没刷新，没看到新价格已经出了，又提交上一轮的报价
            if(turnOfRecord != product.getCurrentTurnOfRecord())
            {
                return Result.fail("Wrong turn number for updating price");
            }
            AdminUserProduct adminUserProductOld = adminUserProductMapper
                    .selectOne(new QueryWrapper<AdminUserProduct>()
                            .eq("admin_id", adminId)
                            .eq("product_id", productId)
                            .eq("turn_of_record", turnOfRecord));
            if (adminUserProductOld != null) {
                adminUserProductOld.setValid(false);
                adminUserProductMapper.updateById(adminUserProductOld);
            }

            AdminUserProduct adminUserProduct = createANewChangingPriceRecord(adminUser, product,
                    productPrice, turnOfRecord);

            checkIfProductPriceShouldBeUpdate(product, turnOfRecord);
            return null;
        }
    }

    private AdminUserProduct createANewChangingPriceRecord(AdminUser adminUser, Product product,
                                                           double productPrice, int turnOfRecord){
        AdminUserProduct adminUserProduct = new AdminUserProduct();
        adminUserProduct.setAdminUser(adminUser);
        adminUserProduct.setProduct(product);
        adminUserProduct.setProductPrice(productPrice);
        adminUserProduct.setRecordTimestamp(System.currentTimeMillis());
        adminUserProduct.setTurnOfRecord(turnOfRecord);
        adminUserProduct.setValid(true);
        return adminUserProduct;
    }

    private void checkIfProductPriceShouldBeUpdate(Product product, int turnOfRecord){
        List<AdminUserProduct> tempList = adminUserProductMapper.selectList(new QueryWrapper<AdminUserProduct>()
                .eq("product_id", product.getId())
                .eq("turn_of_record", turnOfRecord));

        if(tempList.size() >= requiredAdminUserNumForChangeAProductPrice)
        {
            HashMap<Double, Integer> productNewPriceMap = new HashMap<>();
            tempList.forEach(adminUserProduct->{
                productNewPriceMap.merge(adminUserProduct.getProductPrice(), 1, Integer::sum);
            });
            int size = productNewPriceMap.size();
            if(size == 1)
            {
                product.setProductPrice(tempList.get(0).getProductPrice());
                productMapper.updateById(product);
            }
            else
            {
                //找到重复最多的那个报价
                //把剩下的报价设为无效
                //给报无效价格的admin写邮件（包含当前的有效报价）
            }
        }
    }

    //TODO ADMIN的Dashboard应该要能显示（记录）当前报轮的轮数和该轮他提出过的报价（如果有)
}
