package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class ProductVO {
    private Integer productId;
    private Integer category;
    private String itemTitle;
    private String itemDescription;
    private double itemWeight;

    private String imageUrl;
    private Integer userId;

    private Integer hiddenPrice;
    private String itemPurity;
    private double itemPrice;
}
