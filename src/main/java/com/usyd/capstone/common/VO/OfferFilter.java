package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class OfferFilter {
    private Integer userId;

    private Boolean accepted;
    private Boolean rejected;
    private Boolean cancelled;
    private Boolean pending;
    private Boolean expired;

}

