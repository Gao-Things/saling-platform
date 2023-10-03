package com.usyd.capstone.common.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakeOrUpdateAnOfferRequest {
    @JsonProperty("token")
    private String token;
    @JsonProperty("productOrOfferId")
    private Long productOrOfferId;
    @JsonProperty("note")
    private String note;
    @JsonProperty("price")
    private double price;
}
