package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@TableName("admin_user_product")
public class AdminUserProduct implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_user_id", nullable = false)
    private AdminUser adminUser;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @TableField("product_price")
    private double productPrice;

    @TableField("record_timestamp")
    private long recordTimestamp;

    @TableField("turn_of_record")
    private int turnOfRecord;

    @TableField("is_valid")
    private boolean isValid;
}
