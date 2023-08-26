package com.usyd.capstone.mapper;

import com.usyd.capstone.entity.ExchangeRateUsd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@Mapper
public interface ExchangeRateUsdMapper extends BaseMapper<ExchangeRateUsd> {

    List<ExchangeRateUsd> selectNewestCurrencyList();
}
