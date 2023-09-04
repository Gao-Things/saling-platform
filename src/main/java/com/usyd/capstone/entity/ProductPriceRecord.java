package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年08月30日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("product_price_record")
public class ProductPriceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("product_price")
    private Double productPrice;

    @TableField("record_timestamp")
    private Long recordTimestamp;

    @TableField("turn_of_record")
    private Integer turnOfRecord;

    @TableField("product_id")
    private Long productId;


}
