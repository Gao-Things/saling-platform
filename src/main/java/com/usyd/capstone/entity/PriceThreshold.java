package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dreamyoung.mprelation.EntityMapper;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@TableName("price_threshold")
public class PriceThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @TableId(value = "id")
    private Long id;

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

    @TableField("normal_user_id")
    @Transient
    private Long normalUserId;

    @ManyToOne
    @JoinColumn(name = "normal_user_id", nullable = false)
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.ManyToOne
    @com.github.dreamyoung.mprelation.JoinColumn(name = "normal_user_id", referencedColumnName = "id")
    @EntityMapper
    private NormalUser normalUser;

    @TableField("is_minimum")
    private boolean isMinimum;

    @TableField("threshold")
    private double threshold;

    @TableField("is_reached")
    private boolean isReached;
}
