package com.usyd.capstone.common.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.usyd.capstone.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class productAdmin {

    private Product product;
    private List<Long> priceUpdateTime;
    private List<Double> priceUpdateRecord;

}
