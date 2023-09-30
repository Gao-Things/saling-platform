package com.usyd.capstone.common.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Data
public class productDetailWithPrice {
    private Integer id;

    private String productName;

    private double productPrice;

    private String productImage;

    private LocalDateTime productCreateTime;

    private LocalDateTime productUpdateTime;

    private String productDescription;

    private Map<String, Double> convertedPrices = new HashMap<>();;
}
