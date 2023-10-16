package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class productVO {
    private Integer productId;
    private String itemTitle;
    private String itemDescription;
    private double itemWeight;
    private String selectedValue;
    private String imageUrl;
    private Integer userId;
}
