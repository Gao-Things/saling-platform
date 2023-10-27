package com.usyd.capstone.common.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.entity.abstractEntities.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class ProductUserDTO {

    private Long id;

    @TableField("product_name")
    private String productName;

    @TableField("product_price")
    private double productPrice;

    @TableField("product_Image")
    private String productImage;

    @TableField("product_create_time")
    private long productCreateTime;

    @TableField("product_update_time")
    private long productUpdateTime;

    @TableField("product_description")
    private String productDescription;

    @TableField("product_weight")
    private Double productWeight;

    @TableField("purity")
    private String purity;

    //    private String no;
    private String name;

    private String email;


}
