package com.usyd.capstone.common.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptOrCancelOrRejectAnOfferRequest {
    @JsonProperty("token")
    private String token;
    @JsonProperty("offerId")
    private Long offerId;
}
