package com.usyd.capstone.service;

import com.github.dreamyoung.mprelation.IService;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.NormalUser;

public interface NormalUserService {

    public Result setPriceThresholdSingle(String token, long productId, boolean isMinimum, double threshold);

    public Result setPriceThresholdBatch(String token, long productId, boolean isMinimum, double threshold);
}
