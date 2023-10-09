package com.usyd.capstone.common.DTO;

import com.usyd.capstone.entity.Offer;
import lombok.Data;

@Data
public class Notification {

    /**
     * 1.accept offer  2.reject offer
     */
    private Integer messageType;


    /**
     * 1. seller  2. buyer
     */
    private Integer sendUserType;

    private Offer offer;
}
