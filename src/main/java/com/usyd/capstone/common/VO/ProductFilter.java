package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class ProductFilter {
    private Integer category;

    private String purity;
    private Integer status;

    private double minPrice;
    private double maxPrice;
}
