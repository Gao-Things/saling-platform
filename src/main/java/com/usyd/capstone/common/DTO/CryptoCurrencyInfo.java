package com.usyd.capstone.common.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CryptoCurrencyInfo {
    @JsonProperty("s")    //币种名称
    private String name;
    @JsonProperty("S")    //币种符号
    private String symbol;
    @JsonProperty("u")    //价格(USD)
    private double price;
    @JsonProperty("b")    //价格(以比特币计价)
    private double priceInBTC;
    @JsonProperty("v")    //交易量(USD)
    private double volume;
    @JsonProperty("T")    //时间戳(毫秒)
    private long timestamp;
    @JsonProperty("a") //交易量(单位为当前币种)
    private double volumeInCurrentCurrency;
    @JsonProperty("ra")    //报告交易量(单位为当前币种)
    private double reportedVolumeInCurrentCurrency;
    @JsonProperty("rv")    //报告交易量(USD)
    private double reportedVolume;
    @JsonProperty("m")    //市值(USD)
    private double marketValue;
    @JsonProperty("c")    //24小时涨跌幅
    private double riseAndFallInLast24Hours;
    @JsonProperty("h")    //24小时最高价
    private double highestPriceInLast24Hours;
    @JsonProperty("l")    //24小时最低价
    private double lowestPriceInLast24Hours;
    @JsonProperty("cw")    //1周涨跌幅
    private double riseAndFallInLastWeek;
    @JsonProperty("hw")    //1周最高价
    private double highestPriceInLastWeek;
    @JsonProperty("lw")    //1周最低价
    private double lowestPriceInLastWeek;
    @JsonProperty("cm")    //1月涨跌幅
    private double riseAndFallInLastMonth;
    @JsonProperty("hm")    //1月最高价
    private double highestPriceInLastMonth;
    @JsonProperty("lm")    //1月最低价
    private double lowestPriceInLastMonth;
    @JsonProperty("ha")    //历史最高价
    private double highestPriceInHistory;
    @JsonProperty("la")    //历史最低价
    private double lowestPriceInHistory;

}
