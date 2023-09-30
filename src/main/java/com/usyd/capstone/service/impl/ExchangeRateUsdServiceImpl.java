package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.ExchangeRateUsd;
import com.usyd.capstone.mapper.ExchangeRateUsdMapper;
import com.usyd.capstone.service.ExchangeRateUsdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@Service
public class ExchangeRateUsdServiceImpl extends ServiceImpl<ExchangeRateUsdMapper, ExchangeRateUsd> implements ExchangeRateUsdService {
    @Autowired
    ExchangeRateUsdMapper exchangeRateUsdMapper;
    @Override
    public Result getNewestCurrency() {

        List<ExchangeRateUsd>  exchangeRateUsdList = exchangeRateUsdMapper.selectNewestCurrencyList();
        return Result.suc(exchangeRateUsdList);

    }
}
