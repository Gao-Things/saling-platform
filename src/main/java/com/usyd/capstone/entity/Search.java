package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mengting
 * @since 2023年10月25日
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@TableName("search")
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id", nullable = false)
    @TableId(value = "search_id")
    private Integer searchId;

    @TableField("user_id")
    private Integer userId;

    @TableField("search_content")
    private String searchContent;

    @TableField("search_time")
    private Long searchTime;

    @TableField("product_id")
    private Integer productId;


}
