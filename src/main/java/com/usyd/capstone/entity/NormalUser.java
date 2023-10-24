package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@TableName("normal_user")
public class NormalUser extends NotSuperUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @TableId(value = "id")
    private Long id;

    private int gender;

    private String phone;

    @TableField("avatar_url")
    private String avatarUrl;

    @OneToMany(mappedBy = "owner")
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.OneToMany
    @com.github.dreamyoung.mprelation.JoinColumn(name = "id", referencedColumnName = "owner_id")
    private Set<Product> productsOwned;

    @OneToMany(mappedBy = "buyer")
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.OneToMany
    @com.github.dreamyoung.mprelation.JoinColumn(name = "id", referencedColumnName = "buyer_id")
    private Set<Offer> offers;
}
