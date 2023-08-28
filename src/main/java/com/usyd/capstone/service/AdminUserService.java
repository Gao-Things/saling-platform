package com.usyd.capstone.service;

import com.usyd.capstone.common.DTO.Result;

public interface AdminUserService {

    public Result updateProductPrice(String token, Long productId, double productPrice, int turnOfRecord);
}
