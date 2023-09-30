package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dreamyoung.mprelation.EntityMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@TableName("offer")
public class Offer {
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

    @TableField("buyer_id")
    @Transient
    private Long buyerId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.ManyToOne
    @com.github.dreamyoung.mprelation.JoinColumn(name = "buyer_id", referencedColumnName = "id")
    @EntityMapper
    private NormalUser buyer;

    @TableField("price")
    private double price;

    //备注
    @TableField("note")
    private String note;

    @TableField("offer_status")
    private int offerStatus; //0: send 1: accepted 2: rejected 3:cancelled

    @TableField("timestamp")
    private long timestamp;
}
