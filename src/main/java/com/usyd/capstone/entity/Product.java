package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Set;

import com.usyd.capstone.common.Enums.CATEGORY;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @TableId(value = "id")
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

    @TableField(exist = false)
    private double productExchangePrice;

    @TableField("is_in_resetting_process")
    private boolean isInResettingProcess;

    //初始化为0，super修改后+1，admin一致后+1
    //  这是定价的轮次
    //  报价的轮次=定价的轮次+1
    @TableField("current_turn_of_record")
    private int currentTurnOfRecord;

    @TableField("price_status")
    private int priceStatus; //0:涨 1：跌 2：平
//    如果之后有修改/插入/删除记录的功能 本条记录的后一条要视情况修改这个字段的数值

    @Column(name = "category")
    @TableField(exist = false)
    private int categoryValue;

    @TableField("category")
    @Transient
    private CATEGORY category;

    public void setCategory(int categoryValue) {
        this.category = CATEGORY.findByValue(categoryValue);
    }

    public int getCategory() {
        return category.getValue();
    }

    public CATEGORY getCategoryEnum() {
        return category;
    }

    public String getCategoryName() {
        return category.getName();
    }


    @OneToMany(mappedBy = "product")
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.OneToMany
    @com.github.dreamyoung.mprelation.JoinColumn(name = "id", referencedColumnName = "product_id")
//    @Lazy(false) //false向下查找一层，@OneToMany默认是true
    private Set<AdminUserProduct> adminUserProducts;

    @OneToMany(mappedBy = "product")
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.OneToMany
    @com.github.dreamyoung.mprelation.JoinColumn(name = "id", referencedColumnName = "product_id")
    private Set<ProductPriceRecord> ProductPriceRecords;

}
