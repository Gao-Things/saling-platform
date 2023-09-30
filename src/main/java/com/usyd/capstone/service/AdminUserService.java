package com.usyd.capstone.service;

import com.github.dreamyoung.mprelation.IService;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.AdminUserProduct;

public interface AdminUserService {

    public Result updateProductPrice(String token, Long productId, double productPrice, int turnOfRecord);
}
