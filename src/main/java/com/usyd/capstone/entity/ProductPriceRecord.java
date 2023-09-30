package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.github.dreamyoung.mprelation.EntityMapper;
import com.github.dreamyoung.mprelation.Lazy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.persistence.*;
import javax.persistence.Entity;

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
@Entity
@Accessors(chain = true)
@TableName("product_price_record")
public class ProductPriceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @TableId(value = "id")
    private Long id;

    @TableField("product_price")
    private double productPrice;

    @TableField("record_timestamp")
    private long recordTimestamp;

    @TableField("turn_of_record")
    private int turnOfRecord;

    @TableField("product_id")
    @Transient
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.ManyToOne
    @com.github.dreamyoung.mprelation.JoinColumn(name = "product_id", referencedColumnName = "id")
    @EntityMapper
    @Lazy
    private Product product;
}
