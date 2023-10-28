package com.usyd.capstone.common.DTO;

import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.Product;
import lombok.Data;

@Data
public class NotificationDTO {

    private Integer notificationId;
    /**
     * 1.accept an offer 2.reject an offer 3.send an offer 4.update an offer
     * 5.cancel an offer 6.an offer is expired
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
