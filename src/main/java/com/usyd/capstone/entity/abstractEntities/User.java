package com.usyd.capstone.entity.abstractEntities;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String no;
    private String name;
    private String password;

    @TableField("activation_status")
    private boolean activationStatus;
}
