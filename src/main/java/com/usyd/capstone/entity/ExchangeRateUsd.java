package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年08月26日
 */
@Data
@Entity
@TableName("exchange_rate_usd")
public class ExchangeRateUsd implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @TableField("exchange_name")
    private String exchangeName;

    @TableField("exchange_price")
    private Double exchangePrice;

    @TableField("update_time")
    private Timestamp updateTime;


}
