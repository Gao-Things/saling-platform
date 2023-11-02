package com.usyd.capstone.common.DTO;


import lombok.Data;


@Data
public class    OfferProductDTO {

    private Long offerId;

    private Long productId;

    private Long buyerId;

    private int offerStatus; //0: send 1: accepted 2: rejected 3:cancelled

    private double offerPrice;

    //备注
    private String note;


    private long timestamp;

    private String productName;

    private double productPrice;

    private String productImage;

    private String productDescription;

    private String buyerName;

}
