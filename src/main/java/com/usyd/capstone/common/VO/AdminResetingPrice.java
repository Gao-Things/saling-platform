package com.usyd.capstone.common.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminResetingPrice {

    @JsonProperty("token")
    private String token;

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("productPrice")
    private double productPrice;

    @JsonProperty("turnOfRecord")
    private int turnOfRecord;
}
