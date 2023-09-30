package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dreamyoung.mprelation.EntityMapper;
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

    @TableField("admin_user_id")
    @Transient
    private Long adminUserId;

    @ManyToOne
    @JoinColumn(name = "admin_user_id", nullable = false)
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.ManyToOne
    @com.github.dreamyoung.mprelation.JoinColumn(name = "admin_user_id", referencedColumnName = "id")
    @EntityMapper
    private AdminUser adminUser;

    @TableField("product_id")
    @Transient
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.ManyToOne
    @com.github.dreamyoung.mprelation.JoinColumn(name = "product_id", referencedColumnName = "id")
    @EntityMapper
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
