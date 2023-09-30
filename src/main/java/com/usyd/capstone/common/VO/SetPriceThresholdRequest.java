package com.usyd.capstone.common.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPriceThresholdRequest {
    @JsonProperty("token")
    private String token;
    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("isMinimum")
    private boolean isMinimum;
    @JsonProperty("threshold")
    private double threshold;
}
