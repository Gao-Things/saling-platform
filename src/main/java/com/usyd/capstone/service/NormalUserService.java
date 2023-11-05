package com.usyd.capstone.service;

import com.github.dreamyoung.mprelation.IService;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.Product;

public interface NormalUserService {

    public Result setPriceThresholdSingle(String token, Long productId, boolean isMinimum, double threshold);

    public Result setPriceThresholdBatch(String token, Long productId, boolean isMinimum, double threshold);

    public Result makeOrUpdateAnOffer(String token, Long productId, String note, double price);

    public Result acceptAnOffer(String token, Long offerId);

    public Result cancelAnOffer(String token, Long offerId);

    public Result rejectAnOffer(String token, Long offerId);

    public Result openOrCloseOrCancelSale(String token, Long productId, int productStatusNew);

    NormalUser findUserInfoById(Integer userId);

    Boolean updateUserInfo(NormalUser normalUser);

}
