package com.usyd.capstone.common.DTO;

import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.Product;
import lombok.Data;

@Data
public class NotificationDTO {

    private Integer notificationId;
    /**
     * 1.accept offer  2.reject offer
     */
    private Integer messageType;


    /**
     * 1. seller  2. buyer
     */
    private Integer sendUserType;

    private Offer offer;

    private Product product;

    private String notificationContent;
}
