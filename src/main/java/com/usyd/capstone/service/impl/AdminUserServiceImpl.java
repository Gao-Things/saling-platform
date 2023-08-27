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
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private JwtToken jwtToken;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private AdminUserProductMapper adminUserProductMapper;

    @Override
    public Result modifyProductPrice(String token, Long productId, double productPrice) {

        Long adminId = jwtToken.getId(token);
        if(adminId == -1L)
        {
            return Result.fail("Cannot parse token!");
        }
        AdminUser adminUser = adminUserMapper.selectById(adminId);
        Product product = productMapper.selectById(productId);
        if(adminUser == null || product == null)
        {
            return Result.fail("Cannot find admin or product!");
        }
        int currentTurnOfRecord = product.getCurrentTurnOfRecord() + 1;
        AdminUserProduct adminUserProductOld = adminUserProductMapper
                .selectOne(new QueryWrapper<AdminUserProduct>()
                        .eq("admin_id", adminId)
                        .eq("product_id", productId)
                        .eq("turn_of_record", currentTurnOfRecord));
        if(adminUserProductOld != null)
        {
            adminUserProductOld.setValid(false);
            adminUserProductMapper.updateById(adminUserProductOld);
        }

        AdminUserProduct adminUserProduct = new AdminUserProduct();
        adminUserProduct.setAdminUser(adminUser);
        adminUserProduct.setProduct(product);
        adminUserProduct.setProductPrice(productPrice);
        adminUserProduct.setRecordTimestamp(System.currentTimeMillis());
        adminUserProduct.setTurnOfRecord(currentTurnOfRecord);
        adminUserProduct.setValid(true);
        adminUserProductMapper.updateById(adminUserProduct);

        List<AdminUserProduct> tempList = adminUserProductMapper.selectList(new QueryWrapper<AdminUserProduct>()
                .eq("product_id", productId)
                .eq("turn_of_record", currentTurnOfRecord));

        if(tempList.size() >= 5)
        {
            product.setProductPrice(productPrice);
        }
        return null;
    }
}
