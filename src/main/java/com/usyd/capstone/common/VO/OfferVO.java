package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class OfferVO {

    private Long id;

    private double price;

    private String note;

    private int offerStatus;
}
