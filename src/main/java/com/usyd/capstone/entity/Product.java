package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@Getter
@Setter
@Entity
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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

    @TableField("current_turn_of_record")
    private int currentTurnOfRecord;

    @OneToMany(mappedBy = "product")
    private Set<AdminUserProduct> studentCourses = new HashSet<>();

}
