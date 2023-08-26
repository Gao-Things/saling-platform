package com.usyd.capstone.service;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
public interface ExchangeRateUsdService extends IService<ExchangeRateUsd> {

    Result getNewestCurrency();
}
